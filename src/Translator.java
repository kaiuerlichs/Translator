import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translator {

    private Dictionary dict;
    private int dir;

    /**
     * Default constructor for the Translator class
     */
    public Translator(){
        dict = null;
        dir = 0;
    }

    /**
     * Translates a String using the dictionary on the current direction of the Translator
     * @param input The String to translate
     * @return The translation
     */
    public String translate(String input){

        // Initialise regex pattern and matcher
        Pattern p = Pattern.compile("[^\\w\\s\\u00c4\\u00e4\\u00d6\\u00f6\\u00dc\\u00fc\\u00df\\u0027]");
        Matcher m = p.matcher(input);

        String output = "";

        int head = 0;

        boolean done = false;
        while(!done) {
            String current;
            if(m.find()){
                current = input.substring(head,m.start()).trim();
                head = m.end();
            }
            else{
                current = input.substring(head).trim();
                done = true;
            }

            while(!current.equals("")){

                output = output + " ";
                String copy = current;
                for(int i = copy.split(" ").length; i > 0; i--){
                    try {
                        output = output + dict.find(copy,dir);
                        current = current.replaceFirst(copy, "").trim();
                        break;
                    } catch (NoTranslationFoundException e) {
                        int end = copy.lastIndexOf(" ");
                        if(end > -1){
                            copy = copy.substring(0, copy.lastIndexOf(" "));
                        }
                        else{
                            output = output + "<span style='color:blue'>" + copy + "</span>";
                            current = current.replaceFirst(copy, "").trim();
                        }
                    } catch (InvalidTranslationDirectionException e) {
                        e.printStackTrace();
                    }
                }

            }

            if(!done) {
                output = output + input.substring(m.start(), m.end());
            }

        }

        // Return trimmed translation
        return output.trim();
    }

    /**
     * Translates a String using the dictionary on the current direction of the Translator, ask user for missing translations
     * @param input The String to translate
     * @return The translation
     */
    public String translateAskUser(String input){

        // Initialise regex pattern and matcher
        Pattern p = Pattern.compile("[^\\w\\s\\u00c4\\u00e4\\u00d6\\u00f6\\u00dc\\u00fc\\u00df\\u0027]");
        Matcher m = p.matcher(input);

        String output = "";

        int head = 0;

        boolean done = false;
        while(!done) {
            String current;
            if(m.find()){
                current = input.substring(head,m.start()).trim();
                head = m.end();
            }
            else{
                current = input.substring(head).trim();
                done = true;
            }

            while(!current.equals("")){

                output = output + " ";
                String copy = current;
                for(int i = copy.split(" ").length; i > 0; i--){
                    try {
                        output = output + dict.find(copy,dir);
                        current = current.replaceFirst(copy, "").trim();
                        break;
                    } catch (NoTranslationFoundException e) {
                        int end = copy.lastIndexOf(" ");
                        if(end > -1){
                            copy = copy.substring(0, copy.lastIndexOf(" "));
                        }
                        else{
                            String translation = getUserTranslation(copy);
                            output = output + translation;
                            current = current.replaceFirst(copy, "").trim();
                        }
                    } catch (InvalidTranslationDirectionException e) {
                        e.printStackTrace();
                    }
                }

            }

            if(!done) {
                output = output + input.substring(m.start(), m.end());
            }

        }

        // Return trimmed translation
        return output.trim();
    }

    /**
     * Return the Dictionary
     * @return the Dictionary
     */
    public Dictionary getDict(){
        return dict;
    }

    /**
     * Set the dictionary
     * @param pDict Set the dictionary
     */
    public void setDict(Dictionary pDict){
        dict = pDict;
    }

    /**
     * Get the current direction
     * @return The current direction
     */
    public int getDir(){
        return dir;
    }

    /**
     * Toggles the direction between 0 and 1
     */
    public void toggleDir(){
        if(dir==0){
            dir = 1;
        }
        else{
            dir = 0;
        }
    }

    /**
     * Get a user-specified translation for a specific word
     * @param word The word to be translated
     * @return The translation
     */
    public String getUserTranslation(String word){
        JFrame f = new JFrame("Translation not found");
        String translation = JOptionPane.showInputDialog(f,"Enter translation for: " + word, "Translation not found...", JOptionPane.QUESTION_MESSAGE);
        while(translation==null || translation.isBlank()){
            translation = JOptionPane.showInputDialog(f,"Enter translation for: " + word, "You have to enter a translation...", JOptionPane.QUESTION_MESSAGE);
        }
        if(dir == 0){
            dict.add(word, translation);
        }
        else{
            dict.add(translation, word);
        }
        return translation;
    }

}

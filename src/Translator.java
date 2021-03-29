import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translator {

    private Dictionary dict;
    private int dir;

    public String translate(String input, int dir){
        Pattern p = Pattern.compile("[^\\w\\s(?:\\u00c4, \\u00e4,\\u00d6,\\u00f6,\\u00dc,\\u00fc,\\u00df,\\u0027)]");
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
                            output = output + copy;
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

        return output.trim();
    }

    public String translate(File input, int dir) throws InvalidFileFormatException {
        String ext = input.getName().substring(input.getName().lastIndexOf("."));
        if(ext.equals(".txt")){
            try {
                String text = Files.readString(Path.of(input.getAbsolutePath()));
                return translate(text, dir);
            } catch (IOException e) {
                throw new InvalidFileFormatException();
            }
        }
        else{
            throw new InvalidFileFormatException();
        }
    }

    public String translateAskUser(String input, int dir){
        Pattern p = Pattern.compile("[^\\w\\s(?:\\u00c4, \\u00e4,\\u00d6,\\u00f6,\\u00dc,\\u00fc,\\u00df,\\u0027)]");
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

        return output.trim();
    }

    public String translateAskUser(File input, int dir) throws InvalidFileFormatException {
        String ext = input.getName().substring(input.getName().lastIndexOf("."));
        if(ext.equals(".txt")){
            try {
                String text = Files.readString(Path.of(input.getAbsolutePath()));
                return translateAskUser(text, dir);
            } catch (IOException e) {
                throw new InvalidFileFormatException();
            }
        }
        else{
            throw new InvalidFileFormatException();
        }
    }

    public Dictionary getDict(){
        return dict;
    }

    public void setDict(Dictionary pDict){
        dict = pDict;
    }

    public int getDir(){
        return dir;
    }

    public void toggleDir(){
        if(dir==0){
            dir = 1;
        }
        else{
            dir = 0;
        }
    }

    public String getUserTranslation(String word){
        JFrame f = new JFrame("Translation not found");
        return JOptionPane.showInputDialog(f,"Enter translation for: " + word);
    }

}

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

/**
 * Class to represent a Dictionary in the Translator program
 * @author Team 2 AC12002 2020-2021
 * @version 1.0
 */
public class Dictionary implements Serializable, Iterable<DictionaryItem> {

    @Serial
    private static final long serialVersionUID = 1646427678450944460L;

    private String name;
    private int size;
    private String languageA;
    private String languageB;
    private BinaryTree<DictionaryItem> treeAToB;
    private BinaryTree<DictionaryItem> treeBToA;

    /**
     * Default constructor for an instance of the Dictionary class
     * @param pName Name of the Dictionary
     * @param pLanguageA Name of the first language
     * @param pLanguageB Name of the second language
     */
    public Dictionary(String pName, String pLanguageA, String pLanguageB) {

        name = pName;
        size = 0;

        languageA = pLanguageA;
        languageB = pLanguageB;

        treeAToB = new BinaryTree<DictionaryItem>();
        treeBToA = new BinaryTree<DictionaryItem>();

    }

    /**
     * Reads in adequately formatted .txt file and generates dictionary
     * @param input the file to be read in
     * @param pName name of the dictionary
     * @param pLanguageA language input
     * @param pLanguageB language to be translated to
     * @throws InvalidFileFormatException if file is not txt
     */
    public Dictionary(File input, String pName, String pLanguageA, String pLanguageB) throws InvalidFileFormatException {
        this(pName, pLanguageA, pLanguageB);

        // Validate file extension
        String ext = input.getName().substring(input.getName().lastIndexOf("."));
        if(ext.equals(".txt")){
            try {
                // Read in and add translations line by line
                String txt = Files.readString(Path.of(input.getAbsolutePath()));
                String[] lines = txt.split("\n");

                for (String line : lines) {
                    String[] words = line.split(";");
                    if(words.length == 2){
                        add(words[0],words[1]);
                    }
                }

            } catch (IOException e) {
                throw new InvalidFileFormatException();
            }
        }
        else{
            throw new InvalidFileFormatException();
        }

    }

    /**
     * Adds a new entry into the dictionary by populating both directional trees
     * @param wordA Word in language A
     * @param wordB Word in language B
     * @return Whether it was successfully added to the dictionary
     */
    public boolean add(String wordA, String wordB) {

        //Add word to both trees
        DictionaryItem entryAToB = new DictionaryItem(wordA, wordB);
        DictionaryItem entryBToA = new DictionaryItem(wordB, wordA);
        try {
            treeAToB.add(entryAToB);
            treeBToA.add(entryBToA);
        } catch (NodeExistsAlreadyException e) {
            return false;
        }

        // Increase size
        size++;
        return true;

    }

    /**
     * Removes a word from the dictionary
     * @param word The word to remove
     * @return Whether the word was successfully removed from the dictionary
     */
    public boolean remove(String word) {

        // Create dummy DictionaryItem
        DictionaryItem key = new DictionaryItem(word,"");
        DictionaryItem key2;
        DictionaryItem found;

        // First try remove it in direction 0, if that is not possible do direction 1
        try {
            found = treeAToB.find(key);
            treeAToB.remove(found);
            key2 = new DictionaryItem(found.getTranslation(),found.getOriginal());
            treeBToA.remove(key2);
        }
        catch (TreeIsEmptyException e) {
            return false;

        } catch (NodeNotFoundException e) {
            try {
                found = treeBToA.find(key);
                treeBToA.remove(found);
                key2 = new DictionaryItem(found.getTranslation(),found.getOriginal());
                treeAToB.remove(key2);
            }
            catch (TreeIsEmptyException | NodeNotFoundException d) {
                return false;
            }
        }

        // Decrease size
        size--;
        return true;
    }

    /**
     * Finds the translation of a word in the specified translation direction
     * @param original The word to translate
     * @param dir The direction to translate in (0 ==> A to B ; 1 ==> B to A)
     * @return The translation of the word
     * @throws NoTranslationFoundException When the translation could not be found
     * @throws InvalidTranslationDirectionException When the translation direction is not 0 or 1
     */
    public String find(String original, int dir) throws NoTranslationFoundException, InvalidTranslationDirectionException {

        // Create dummy DictionaryItem
        DictionaryItem key = new DictionaryItem(original,"");

        if(dir == 0){
            // Search the tree
            try {
                DictionaryItem result = treeAToB.find(key);
                return result.getTranslation();
            } catch (TreeIsEmptyException | NodeNotFoundException e) {
                throw new NoTranslationFoundException();
            }
        }
        else if(dir == 1){
            // Search the tree
            try {
                DictionaryItem result = treeBToA.find(key);
                return result.getTranslation();
            } catch (TreeIsEmptyException | NodeNotFoundException e) {
                throw new NoTranslationFoundException();
            }
        }
        else{
            throw new InvalidTranslationDirectionException();
        }

    }

    /**
     * Returns the name of the Dictionary
     * @return The name of the Dictionary
     */
    public String getName() {

        return name;

    }

    /**
     * Sets the name of the Dictionary
     * @param pName The name for the Dictionary
     */
    public void setName(String pName) {

        name = pName;

    }

    /**
     * Returns the size of the Dictionary
     * @return The size of the Dictionary
     */
    public int getSize() {

        return size;

    }

    /**
     * Returns the first language of the Dictionary
     * @return The first language of the Dictionary
     */
    public String getLanguageA() {

        return languageA;

    }

    /**
     * Returns the second language of the Dictionary
     * @return The second language of the Dictionary
     */
    public String getLanguageB() {

        return languageB;

    }

    /**
     * Retrieves dictionary items one by one
     * @return next item
     */
    @Override
    public Iterator<DictionaryItem> iterator() {
        return treeAToB.iterator();
    }
}

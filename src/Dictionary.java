import java.io.Serializable;

public class Dictionary implements Serializable {

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
     * Adds a new entry into the dictionary by populating both directional trees
     * @param wordA Word in language A
     * @param wordB Word in language B
     * @return Whether it was successfully added to the dictionary
     */
    public boolean add(String wordA, String wordB) {

        DictionaryItem entryAToB = new DictionaryItem(wordA, wordB);
        DictionaryItem entryBToA = new DictionaryItem(wordB, wordA);
        try {
            treeAToB.add(entryAToB);
            treeBToA.add(entryBToA);
        } catch (NodeExistsAlreadyException e) {
            return false;
        }

        size++;
        return true;

    }

    /**
     * Removes a word from the dictionary
     * @param word The word to remove
     * @return Whether the word was successfully removed from the dictionary
     */
    public boolean remove(String word) {

        DictionaryItem key = new DictionaryItem("key","");
        DictionaryItem key2;
        DictionaryItem found;

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

        DictionaryItem key = new DictionaryItem(original,"");

        if(dir == 0){
            try {
                DictionaryItem result = treeAToB.find(key);
                return result.getTranslation();
            } catch (TreeIsEmptyException | NodeNotFoundException e) {
                throw new NoTranslationFoundException();
            }
        }
        else if(dir == 1){
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

    public String getName() {

        return name;

    }

    public void setName(String word) {

        name = word;

    }

    public int getSize() {

        return size;

    }

    public String getLanguageA() {

        return languageA;

    }

    public String getLanguageB() {

        return languageB;

    }

}

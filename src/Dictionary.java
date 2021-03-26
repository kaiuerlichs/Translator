
public class Dictionary implements Serializable<Dictionary> {

    private String name;
    private int size;
    private String languageO;
    private String languageT;
    private BinaryTree<DictonaryItem> treeOToT;
    private BinaryTree<DictionaryItem> treeTToO;

    /**
     * Default constructor
     */
    public Dictionary(String Original, String Translation) {

        languageO = Original;
        languageT = Translation;

    }
    /**
     * add's original and translated words to respective tree's
     */
    public void add(String Original, String Translation) {

        treeOToT.add(Original);
        treeTToO.add(Translation);

    }
    /**
     * remove's specific word from both tree's
     */
    public void remove(String Word) {

        treeOToT.remove(Word);
        treeTToO.remove(Word);

    }
    /**
     * displays something
     */
    public void display() {



    }
    /**
     * find's word on original tree
     */
    public String find(String Original, int dir) {

        treeOToT.find(Original);
        return languageO;
    }

    public String getName() {

        return name;

    }

    public void setName(String Word) {

        name = Word;

    }

    public int getSize() {

        return size;

    }

    public String getLanguageO() {

        return languageO;

    }

    public String getLanguageT() {

        return languageT;

    }

}

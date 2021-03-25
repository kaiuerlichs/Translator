public class DictionaryItem implements Comparable<DictionaryItem> {

    private String original;
    private String translation;

    public DictionaryItem(String pOriginal, String pTranslation){
        original = pOriginal;
        translation = pTranslation;
    }

    public String getOriginal(){
        return original;
    }

    public String getTranslation(){
        return translation;
    }

    @Override
    public int compareTo(DictionaryItem x) {
        return original.compareTo(x.getOriginal());
    }
}

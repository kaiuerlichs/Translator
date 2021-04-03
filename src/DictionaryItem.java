import java.io.Serial;
import java.io.Serializable;
import java.util.Locale;

public class DictionaryItem implements Comparable<DictionaryItem>, Serializable {

    @Serial
    private static final long serialVersionUID = -282205958586708038L;

    private String original;
    private String translation;

    /**
     * Default constructor for a DictionaryItem
     * @param pOriginal The original word
     * @param pTranslation The translated word
     */
    public DictionaryItem(String pOriginal, String pTranslation){
        original = pOriginal;
        translation = pTranslation;
    }

    /**
     * Returns the original word
     * @return The original word
     */
    public String getOriginal(){
        return original;
    }

    /**
     * Returns the translated word
     * @return The translated word
     */
    public String getTranslation(){
        return translation;
    }

    /**
     * Compare the DictionaryItem to another
     * @param x the Item to compare to
     * @return the comparison value
     */
    @Override
    public int compareTo(DictionaryItem x) {
        return original.toLowerCase().compareTo(x.getOriginal().toLowerCase());
    }
}

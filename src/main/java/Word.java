import java.util.HashMap;
import java.util.Map;

public class Word {
    private Map<Language, String> _versions;

    public Word(HashMap<Language, String> versions) {
        _versions = versions;
    }

    public void SetWord(Language language, String word){
        _versions.put(language, word);
    }

    public String getWord(Language language) {
        return _versions.get(language);
    }
}

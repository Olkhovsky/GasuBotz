import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Consumer;

public class Dictionary {
    private Language _from;
    private Language _to;
    private HashSet<Word> _words;
    private HashSet<Consumer<Word>> _listeners = new HashSet<>();


    public Dictionary(Language from, Language to) {
        _words = new HashSet();
    }

    public void Add(String word, String translation) {
        HashMap map = new HashMap<Language, String>();
        map.put(_from, word.toLowerCase());
        map.put(_to, translation.toLowerCase());
        Word newWord = new Word(map);
        if (_words.add(new Word(map))) {
            Update(newWord);
        }
    }

    public void Add(Word word) {
        if (_words.add(word)) {
            Update(word);
        }
    }

    public String GetTranslation(String word) {
        for(Word w : _words) {
            if (w.getWord(_from) == word) {
                return w.getWord(_to);
            }
        }
        return null;
    }

    public void Reverse() {
        Language lang = _from;
        _from = _to;
        _to = lang;
    }

    public Language GetFrom() {
        return _from;
    }

    public Language GetTo() {
        return _to;
    }

    public HashSet<Word> getWords() {
        return _words;
    }
    public void SubscribeOnNewWord(Consumer<Word> listener) {
        _listeners.add(listener);
    }

    private void Update(Word newWord) {
        _listeners.forEach(x -> x.accept(newWord));
    }

}

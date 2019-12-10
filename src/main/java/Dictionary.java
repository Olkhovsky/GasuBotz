import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Consumer;

public class Dictionary {
    private Language _from;
    private Language _to;
    private HashSet<Word> _words;
    private HashSet<Consumer<Word>> _newWordListeners = new HashSet<>();
    private HashSet<Consumer<Word>> _changeWordListeners = new HashSet<>();
    private HashSet<Consumer<Word>> _deleteWordListeners = new HashSet<>();


    public Dictionary(Language from, Language to) {
        _words = new HashSet();
    }

    public void Add(String word, String translation) {
        HashMap map = new HashMap<Language, String>();
        map.put(_from, word.toLowerCase());
        map.put(_to, translation.toLowerCase());
        Word newWord = new Word(map);
        if (_words.add(new Word(map))) {
            UpdateNew(newWord);
        }
    }

    public void Add(Word word) {
        if (_words.add(word)) {
            UpdateNew(word);
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

    public boolean ChangeWord(String word, String translation) {
        Word w = _words.stream().filter(a -> a.getWord(_to) == word).findFirst().get();
        if (w != null && w.getWord(_from) != null) {
            w.SetWord(_from, translation);
            UpdateChange(w);
            return true;
        }
        return false;
    }

    public boolean DeleteWord(String word, String translation) {
        Word w = _words.stream().filter(a -> a.getWord(_to) == word).findFirst().get();
        if (w != null) {
            _words.remove(w);
            UpdateDelete(w);
            return true;
        }
        return false;
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
        _newWordListeners.add(listener);
    }
    public void SubscribeOnChangeWord(Consumer<Word> listener) {
        _changeWordListeners.add(listener);
    }
    public void SubscribeOnDeleteWord(Consumer<Word> listener) {
        _deleteWordListeners.add(listener);
    }

    private void UpdateNew(Word newWord) {
        _newWordListeners.forEach(x -> x.accept(newWord));
    }
    private void UpdateChange(Word changedWord) {
        _changeWordListeners.forEach(x -> x.accept(changedWord));
    }
    private void UpdateDelete(Word changedWord) {
        _deleteWordListeners.forEach(x -> x.accept(changedWord));
    }

}

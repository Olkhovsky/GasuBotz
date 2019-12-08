import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Exercise {
    private final Dictionary _dictionary;

    class WordWatcher {
        private final Word _word;
        private double _score = 0;


        public WordWatcher(Word word) {
            _word = word;
        }

        public void Right() {
            _score++;
        }
        public void Wrong() {
            _score--;
        }
        public void RightWithHint() {
            _score += 1/2;
        }

        public double GetScore() {
            return _score;
        }
    }

    HashSet<WordWatcher> _watchers = new HashSet<>();

    public Exercise(Dictionary dictionary) {
        _dictionary = dictionary;
        _dictionary.SubscribeOnNewWord((w) -> _watchers.add(new WordWatcher(w)));

        for(Word w : dictionary.getWords()) {
            _watchers.add(new WordWatcher(w));
        }
    }

}

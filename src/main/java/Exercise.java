import java.util.*;
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

    private static final float RATE = 0.333f;
    private HashSet<WordWatcher> _watchers = new HashSet<>();
    private List<WordWatcher> _activeWatchers;
    private double _initialScore;
    private int _limit;

    protected Exercise(Dictionary dictionary) {
        _dictionary = dictionary;
        _dictionary.SubscribeOnNewWord((w) -> _watchers.add(new WordWatcher(w)));
        _dictionary.SubscribeOnChangeWord((w) -> _watchers.add(new WordWatcher(w)));
        _dictionary.SubscribeOnDeleteWord((w) -> _watchers.remove(w));

        for(Word w : dictionary.getWords()) {
            _watchers.add(new WordWatcher(w));
        }
    }

    protected void SetUp(int limit) {
        _limit = limit;
        int half = (int) (limit - limit*RATE);
        _activeWatchers = _watchers.stream()
                .sorted(Comparator.comparingDouble(WordWatcher::GetScore))
                .limit(half)
                .collect(Collectors.toCollection(ArrayList::new));

        for(int i = 0; i < limit*RATE; i++) {
            _activeWatchers.add(_activeWatchers.get(0));
        }
        Collections.shuffle(_activeWatchers);
        _initialScore = GetTotalScore(_activeWatchers);
    }


    protected String Result() {
        double total = GetTotalScore(_activeWatchers);
        if (total < _initialScore) {
            return "too bad.";
        }

        float progress = (float)((total -_initialScore)/_limit);
        if (progress < 0.3f) {
            return "bad.";
        }

        if (progress < 0.6f) {
            return  "good.";
        }
        return "excellent.";
    }

    private double GetTotalScore(List<WordWatcher> list) {
        double total = 0;
        for(WordWatcher ww : list) {
            total += ww._score;
        }
        return total;
    }
}

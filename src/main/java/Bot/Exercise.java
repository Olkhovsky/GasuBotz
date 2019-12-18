package Bot;

import java.util.*;
import java.util.stream.Collectors;

public class Exercise implements Iterator<String>  {

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
        public Word GetWord() {
            return _word;
        }
    }

    protected final Dictionary _dictionary;
    protected List<WordWatcher> _activeWatchers;
    protected boolean _isSet;
    protected int currentWatcher;
    private static final float RATE = 0.3f;
    private HashSet<WordWatcher> _watchers = new HashSet<>();
    private double _initialScore;
    private int _limit;

    public Exercise(Dictionary dictionary) {
        _dictionary = dictionary;
        _dictionary.SubscribeOnNewWord((w) -> _watchers.add(new WordWatcher(w)));
        _dictionary.SubscribeOnChangeWord((w) -> {
            WordWatcher ww = _watchers.stream().filter(a -> a._word == w).findAny().get();
            ww._score = 0;
        });
        _dictionary.SubscribeOnDeleteWord((w) -> _watchers.remove(w));

        for(Word w : dictionary.getWords()) {
            _watchers.add(new WordWatcher(w));
        }
        _isSet = false;
    }

    public void Start(int limit) {
        SetUp(limit);
    }

    public void WriteAnswer(String answer) {
        if(!_isSet) {
            return;
        }
        WordWatcher watcher = _activeWatchers.get(currentWatcher);
        String translate = _dictionary.GetToVersion(watcher._word);
        if (answer.toLowerCase().equals(translate)) {
            watcher.Right();
            return;
        }
        watcher.Wrong();
    }


    @Override
    public boolean hasNext() {
        if (currentWatcher + 1 < _activeWatchers.size()) {
            return  true;
        }
        _isSet = false;
        return  false;
    }

    @Override
    public String next() {
        currentWatcher++;
        String word = _dictionary.GetFromVersion(
                _activeWatchers.get(currentWatcher).GetWord()
        );
        return word;
    }

    public String Result() {
        if (_isSet) {
            return null;
        }
        double total = GetTotalScore();
        if (total < _initialScore) {
            return "too bad.";
        }

        float progress = (float)((total -_initialScore)/_limit);
        if (progress < 0.3f) {
            return "bad.";
        }

        if (progress < 0.7f) {
            return  "good.";
        }
        return "excellent.";
    }

    private void SetUp(int limit) {
        _limit = limit;
        int half = (int) (limit - limit*RATE);
        _activeWatchers = _watchers.stream()
                .sorted(Comparator.comparingDouble(WordWatcher::GetScore))
                .limit(half)
                .collect(Collectors.toCollection(ArrayList::new));

        for(int i = 0; i < limit*RATE; i++) {
            _activeWatchers.add(_activeWatchers.get(i));
        }
        Collections.shuffle(_activeWatchers);
        _initialScore = GetTotalScore();
        _isSet = true;
        currentWatcher = -1;
    }

    private double GetTotalScore() {
        double total = 0;
        for(WordWatcher ww : _watchers) {
            total += ww._score;
        }
        return total;
    }
}

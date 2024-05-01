package co.harborbytes.wortedeck.words.aggregation;

import co.harborbytes.wortedeck.words.WordKind;

public interface WordStats {
    WordKind getWordKind();
    Integer getCount();
}

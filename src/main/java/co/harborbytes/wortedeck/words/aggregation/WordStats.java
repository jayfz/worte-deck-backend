package co.harborbytes.wortedeck.words.aggregation;

import co.harborbytes.wortedeck.words.WordType;
import lombok.Getter;
import lombok.Setter;

public interface WordStats {
    WordType getWordType();
    Integer getCount();
}

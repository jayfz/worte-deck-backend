package co.harborbytes.wortedeck.words.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WordStatsDTO {
    private int nounCount = 0;
    private int adjectiveCount = 0;
    private int verbCount = 0;
    private int adverbCount = 0;
    private int commonExpressionCount = 0;
}

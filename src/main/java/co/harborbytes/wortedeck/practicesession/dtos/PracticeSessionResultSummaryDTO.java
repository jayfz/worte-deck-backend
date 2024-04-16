package co.harborbytes.wortedeck.practicesession.dtos;


import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Builder
public class PracticeSessionResultSummaryDTO {
    private Long id;
    private Instant createdAt;
    private Long wordsTestedCount;
    private Long durationInSeconds;
    private BigDecimal score;
}

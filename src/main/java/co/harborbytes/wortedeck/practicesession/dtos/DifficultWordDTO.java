package co.harborbytes.wortedeck.practicesession.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class DifficultWordDTO {
    private Long wordId;
    private String word;
    private String[] englishTranslations;
    private Long leftSwipeCount;
}

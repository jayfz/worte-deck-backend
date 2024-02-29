package co.harborbytes.wortedeck.words.dtos;

import co.harborbytes.wortedeck.words.NounGender;
import co.harborbytes.wortedeck.words.WordType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WordDTO {
    private Long id;
    private String word;
    private WordType type;
    private String[] pronunciations;
    private String[] englishTranslations;
    private String[] recordingURLs;
    private String germanExample;
    private String[] germanExampleRecordingURLs;
    private String englishExample;

    /* adjective properties */
    private Boolean isComparable;
    private String comparative;
    private String superlative;

    /* noun properties */
    private NounGender gender;
    private String plural;

    /* verb properties */
    private Boolean isRegular;
    private Boolean isSeparable;
    private Boolean hasPrefix;
}

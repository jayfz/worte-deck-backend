package co.harborbytes.wortedeck.words.dtos;

import co.harborbytes.wortedeck.words.NounGender;
import co.harborbytes.wortedeck.words.WordKind;
import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@JsonInclude(JsonInclude.Include.ALWAYS)
public class WordDTO {
    private Long id;
    private String word;
    private WordKind type;
    private String[] pronunciations;
    private String[] englishTranslations;
    private String[] recordingURLs;
    private String germanExample;
    private String[] germanExampleRecordingURLs;
    private String englishExample;
    private String[] matches;
    private Boolean isReady;

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

    @JsonGetter("recordingURLs")
    public String[] getRecordingURLs() {
        if(recordingURLs == null)
            return new String[]{};

        return recordingURLs;
    }

    @JsonGetter("germanExampleRecordingURLs")
    public String[] getGermanExampleRecordingURLs(){
        if(germanExampleRecordingURLs == null){
            return new String[]{};
        }
        return germanExampleRecordingURLs;
    }
}

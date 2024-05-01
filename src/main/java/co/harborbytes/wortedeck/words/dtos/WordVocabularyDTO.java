package co.harborbytes.wortedeck.words.dtos;

import co.harborbytes.wortedeck.words.WordKind;
import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class WordVocabularyDTO {
    private Long id;
    private String word;
    private String[] englishTranslations;
    private String[] recordingURLs;
    private String[] pronunciations;
    private WordKind kind;

    @JsonGetter("recordingURLs")
    public String[] getRecordingURLs(){
        if(recordingURLs == null)
            return new String[]{};
        return recordingURLs;
    }
}

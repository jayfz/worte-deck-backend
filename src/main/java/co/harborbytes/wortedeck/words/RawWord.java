package co.harborbytes.wortedeck.words;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RawWord {

        private String word;
        private WordType type;
        private String[] pronunciation;
        private String[]  englishTranslations;
        private NounGender nounGender;
        private String nounPlural;
        private Boolean verbRegular;
        private Boolean verbSeparable;
        private Boolean verbWithPrefix;
        private Boolean adjectiveComparable;
        private String adjectiveComparative;
        private String adjectiveSuperlative;
        private String[] recordingURLs ;
        private ScrapResult scrapResult;
}

enum ScrapResult{
    SUCCESS,
    FAILURE,
    PENDING
}

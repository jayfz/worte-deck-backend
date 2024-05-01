package co.harborbytes.wortedeck.words;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name="dictionary_word")
@Getter
@Setter
public class DictionaryWord {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotNull
        @Size(min=1, max=128)
        @Column
        private String word;

        @NotNull
        @Enumerated(EnumType.STRING)
        @Column
        private WordKind kind;

        @NotNull
        @Column
        private String[] pronunciations;

        @NotNull
        @Column
        private String[]  englishTranslations;

        @Enumerated(EnumType.STRING)
        @Column
        private NounGender nounGender;

        @Column
        private String nounPlural;

        @Column
        private Boolean verbRegular;

        @Column
        private Boolean verbSeparable;

        @Column
        private Boolean verbWithPrefix;

        @Column
        private Boolean adjectiveComparable;

        @Column
        private String adjectiveComparative;

        @Column
        private String adjectiveSuperlative;

        @Column(length = 2048)
        private String[] recordingURLs ;

        @Column
        @Enumerated(EnumType.ORDINAL)
        private ScrapResult scrapResult;


        enum ScrapResult{
                SUCCESS,
                FAILURE,
                PENDING
        }
}



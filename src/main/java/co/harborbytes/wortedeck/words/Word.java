package co.harborbytes.wortedeck.words;

import co.harborbytes.wortedeck.usermanagement.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
//@Table(name = "word", uniqueConstraints = {
//        @UniqueConstraint(name = "word_word_and_kind_unique", columnNames = {
//                "word", "type"
//        })
//})

@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter

public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "word", nullable = false)
    private String word;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WordKind kind;

    @NotNull
    @Column(name =  "pronunciations", nullable = false)
    private String[] pronunciations;

    @NotNull
    @Column(name =  "english_translations", nullable = false)
    private String[] englishTranslations;

//    @NotNull
    @Column(name ="recording_urls")
    private String[] recordingURLs;

    @NotNull
    @Size(min = 2,  max = 256)
    @Column(name ="german_example", nullable = false)
    private String germanExample;

//    @NotNull
    @Column(name ="german_example_recording_url")
    private String[] germanExampleRecordingURLs;

    @NotNull
    @Size(min = 2,  max = 256)
    @Column(name ="english_example", nullable = false )
    private String englishExample;

    @NotNull
    @Column(name="matches")
    private String[] matches;

    @NotNull
    @Column(name= "is_ready")
    private boolean isReady;

    @NotNull
    @ManyToOne(
            optional = false,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
//            foreignKey = @ForeignKey(
//                    name = "FK_user_word"
//            ),
            updatable = false,
            nullable = false
    )
    private User user;

}

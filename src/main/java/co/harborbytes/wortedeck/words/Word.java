package co.harborbytes.wortedeck.words;

import co.harborbytes.wortedeck.usermanagement.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "word", uniqueConstraints = {
        @UniqueConstraint(name = "UniqueWordAndType", columnNames = {
                "word", "type"
        })
})
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter

public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 128)
    @Column(name = "word", nullable = false)
    private String word;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type", nullable = false)
    private WordType type;

    @NotNull
    @Column(name =  "pronunciations", nullable = false)
    private String[] pronunciations;

    @NotNull
    @Column(name =  "english_translations", nullable = false)
    private String[] englishTranslations;

    @NotNull
    @Column(name ="recording_url")
    private String[] recordingURLs;

    @NotNull
    @Size(min = 2,  max = 256)
    @Column(name ="german_example", nullable = false)
    private String germanExample;

    @NotNull
    @Column(name ="german_example_recording_url")
    private String[] germanExampleRecordingURLs;

    @NotNull
    @Size(min = 2,  max = 256)
    @Column(name ="english_example", nullable = false)
    private String englishExample;

    @NotNull
    @ManyToOne(
            optional = false,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "Word.userId_User.id_FK"
            ),
            updatable = false,
            nullable = false
    )
    private User user;

}

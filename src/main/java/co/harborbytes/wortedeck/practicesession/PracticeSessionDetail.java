package co.harborbytes.wortedeck.practicesession;

import co.harborbytes.wortedeck.words.Word;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "practice_session_detail", uniqueConstraints = @UniqueConstraint(
        columnNames = {"word_id", "practice_session_id"}
))
@Getter
@Setter
public class PracticeSessionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(
            optional = false,
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "word_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "PracticeSessionDetail.wordId_Word.id_FK"
            ),
            updatable = false,
            nullable = false
    )
    private Word word;


    @NotNull
    @ManyToOne(
            optional = false
    )
    @JoinColumn(
            name = "practice_session_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "PracticeSessionDetail.practiceSessionId_PracticeSession.id_FK"
            )
    )
    private PracticeSession practiceSession;
}

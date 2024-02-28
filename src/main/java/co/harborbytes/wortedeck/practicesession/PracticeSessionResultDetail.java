package co.harborbytes.wortedeck.practicesession;

import co.harborbytes.wortedeck.words.Word;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "practice_session_result_detail", uniqueConstraints = @UniqueConstraint(
        columnNames = {"practice_session_result_id", "word_id"}
))
@Getter
@Setter
public class PracticeSessionResultDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(
            optional = false
    )
    @JoinColumn(
            name = "practice_session_result_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "PracticeSessionResultDetail.practiceSessionResultId_PracticeSessionResult.id_FK"
            ),
            updatable = false,
            nullable = false
    )
    private PracticeSessionResult practiceSessionResult;

    @NotNull
    @ManyToOne(
            optional = false
    )
    @JoinColumn(
            name = "word_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "PracticeSessionResultDetail.wordId_Word.id_FK"
            ),
            updatable = false,
            nullable = false
    )
    private Word word;

    @NotNull
    @Column(name = "left_swipe", nullable = false, updatable = false)
    private Boolean leftSwipe;

    @NotNull
    @Column(name = "right_swipe", nullable = false, updatable = false)
    private Boolean rightSwipe;
}

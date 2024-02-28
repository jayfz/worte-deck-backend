package co.harborbytes.wortedeck.practicesession;


import co.harborbytes.wortedeck.usermanagement.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "practice_session_result")
@Getter
@Setter
public class PracticeSessionResult {

    public PracticeSessionResult(){
        this.leftSwipesCount = 0L;
        this.rightSwipesCount = 0L;
        this.wordsTestedCount = 0L;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @NotNull
    @Min(value = 0)
    @Max(value = 5)
    @Column(name = "score", nullable = false, updatable = false, precision = 3, scale = 2)
    private BigDecimal score;


    @NotNull
    @PositiveOrZero
    @Column(name = "left_swipes_count", nullable = false, updatable = false)
    private Long leftSwipesCount;

    @NotNull
    @PositiveOrZero
    @Column(name = "right_swipes_count", nullable = false, updatable = false)
    private Long rightSwipesCount;

    @NotNull
    @Positive
    @Column(name = "words_tested_count", nullable = false, updatable = false)
    private Long wordsTestedCount;

    @NotNull
    @Positive
    @Column(name = "duration_in_seconds", nullable = false, updatable = false)
    private Long durationInSeconds;


    @NotNull
    @OneToMany(
            orphanRemoval = true,
            cascade = {CascadeType.ALL},
            mappedBy = "practiceSessionResult"
    )
    private List<PracticeSessionResultDetail> practiceSessionResultDetails;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "PracticeSessionResult.userId_User.id_FK"
            ),
            updatable = false,
            nullable = false
    )
    private User user;
}

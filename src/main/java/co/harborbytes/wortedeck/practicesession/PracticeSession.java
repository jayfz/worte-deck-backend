package co.harborbytes.wortedeck.practicesession;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
@Table( name = "practice_session")
public class PracticeSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToMany(
            mappedBy = "practiceSession",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PracticeSessionDetail> practiceSessionWordList;
}

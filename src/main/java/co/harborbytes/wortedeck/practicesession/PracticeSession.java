package co.harborbytes.wortedeck.practicesession;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table( name = "practice_session")
@Getter
@Setter
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

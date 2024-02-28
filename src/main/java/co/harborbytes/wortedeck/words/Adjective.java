package co.harborbytes.wortedeck.words;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "adjective")
@Getter
@Setter
public class Adjective  extends  Word{

    @NotNull
    @Column(name = "is_comparable", nullable = false)
    private Boolean isComparable;

    @Column(name =  "comparative" )
    private String comparative;

    @Column(name = "superlative")
    private String superlative;
}

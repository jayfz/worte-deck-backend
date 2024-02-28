package co.harborbytes.wortedeck.words;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "noun")
@Getter
@Setter
public class Noun extends  Word{

    @NotNull
    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private NounGender gender;

    @NotNull
    @Size(min=2, max=128)
    @Column(name = "plural", nullable = false)
    private String plural;
}



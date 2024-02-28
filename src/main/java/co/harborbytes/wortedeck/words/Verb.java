package co.harborbytes.wortedeck.words;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "verb")
@Getter
@Setter
public class Verb  extends  Word{

    @NotNull
    @Column(name = "is_regular")
    private Boolean isRegular;

    @NotNull
    @Column(name = "is_separable")
    private Boolean isSeparable;

    @NotNull
    @Column(name = "has_prefix")
    private Boolean hasPrefix;
}

package co.harborbytes.wortedeck.words.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAdjectiveDTO extends BaseCreateWordDTO{

    @NotNull
    private Boolean isComparable;

    @Size( max = 128)
    private String comparative;

    @Size( max = 128)
    private String superlative;
}

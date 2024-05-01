package co.harborbytes.wortedeck.words.dtos;

import co.harborbytes.wortedeck.words.NounGender;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateNounDTO extends BaseCreateWordDTO{

    @NotNull
    private NounGender gender;

    @NotNull
    @Size(min = 2, max = 128)
    private String plural;
}

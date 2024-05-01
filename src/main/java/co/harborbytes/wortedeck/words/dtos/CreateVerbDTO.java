package co.harborbytes.wortedeck.words.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateVerbDTO extends BaseCreateWordDTO {

    @NotNull
    private Boolean isRegular;

    @NotNull
    private Boolean isSeparable;

    @NotNull
    private Boolean hasPrefix;
}

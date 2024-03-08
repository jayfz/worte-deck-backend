package co.harborbytes.wortedeck.practicesession.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePracticeSessionResultsDetailsDTO {

    @NotNull
    @Positive
    Long wordId;

    @NotNull
    @NotEmpty
    String decision;
}

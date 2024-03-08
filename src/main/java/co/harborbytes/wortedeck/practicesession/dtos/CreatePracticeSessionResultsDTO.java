package co.harborbytes.wortedeck.practicesession.dtos;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreatePracticeSessionResultsDTO {

    @NotNull
    @Positive
    Long duration;

    @NotNull
    @Size(min = 1)

    List<CreatePracticeSessionResultsDetailsDTO> movements;
}

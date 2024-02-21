package co.harborbytes.wortedeck.usermanagement.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GoogleUserLoginDTO {

    @NotEmpty
    @Size(min = 2)
    private String idToken;
}

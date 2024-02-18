package co.harborbytes.wortedeck.usermanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDTO {
    @NotEmpty
    @Email
    private String email;

    @NotNull
    @Size(min = 8, max = 64)
    private String password;
}

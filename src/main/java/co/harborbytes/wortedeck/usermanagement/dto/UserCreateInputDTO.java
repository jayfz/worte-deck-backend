package co.harborbytes.wortedeck.usermanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateInputDTO {
    @NotNull
    @Size(min = 2, max = 64)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 64)
    private String lastName;

    @NotNull
    @Size(min = 8, max = 64)
    private String password;

    @NotEmpty
    @Email
    private String email;
}

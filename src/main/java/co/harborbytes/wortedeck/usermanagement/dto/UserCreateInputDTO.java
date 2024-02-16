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
    @Size(min = 2, max = 128)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 128)
    private String lastName;

    @NotEmpty
    private String password;

    @NotNull
    @Email
    private String email;
}

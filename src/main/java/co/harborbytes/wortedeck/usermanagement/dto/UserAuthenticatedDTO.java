package co.harborbytes.wortedeck.usermanagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UserAuthenticatedDTO {

    private UserDTO user;
    private String token;
    private Instant expirationDate;
}

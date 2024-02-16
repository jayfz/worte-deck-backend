package co.harborbytes.wortedeck.usermanagement;


import co.harborbytes.wortedeck.apiresponse.Success;
import co.harborbytes.wortedeck.usermanagement.dto.UserAuthenticatedDTO;
import co.harborbytes.wortedeck.usermanagement.dto.UserCreateInputDTO;
import co.harborbytes.wortedeck.usermanagement.dto.UserLoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService){
        this.userService = userService;
    }


    @PostMapping("/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Success<String> register (@RequestBody @Validated final UserCreateInputDTO user){
        return new Success<>(userService.register(user));
    }

    @PostMapping("/auth/login")
    @ResponseStatus(HttpStatus.CREATED)
    public Success<UserAuthenticatedDTO> login (@RequestBody final UserLoginDTO login){
        return new Success<>(userService.login(login));

    }

    @PostMapping("/auth/google/login")
    @ResponseStatus(HttpStatus.CREATED)
    public Success<UserAuthenticatedDTO> googleLogin(@RequestBody final String token){
        return new Success<>(userService.googleLogin(token));
    }
}

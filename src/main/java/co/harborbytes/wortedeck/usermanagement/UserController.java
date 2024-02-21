package co.harborbytes.wortedeck.usermanagement;


import co.harborbytes.wortedeck.shared.Success;
import co.harborbytes.wortedeck.usermanagement.dto.GoogleUserLoginDTO;
import co.harborbytes.wortedeck.usermanagement.dto.UserAuthenticatedDTO;
import co.harborbytes.wortedeck.usermanagement.dto.UserCreateInputDTO;
import co.harborbytes.wortedeck.usermanagement.dto.UserLoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService){
        this.userService = userService;
    }


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Success<String> register (@RequestBody @Validated final UserCreateInputDTO user){
        return new Success<>(userService.register(user));
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public Success<UserAuthenticatedDTO> login (@RequestBody @Validated final UserLoginDTO login){
        return new Success<>(userService.login(login));

    }

    @PostMapping("/google/login")
    @ResponseStatus(HttpStatus.OK)
    public Success<UserAuthenticatedDTO> googleLogin(@RequestBody @Validated final GoogleUserLoginDTO login){
        return new Success<>(userService.googleLogin(login.getIdToken()));
    }
}

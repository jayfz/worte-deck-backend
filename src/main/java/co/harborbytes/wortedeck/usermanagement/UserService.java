package co.harborbytes.wortedeck.usermanagement;

import co.harborbytes.wortedeck.usermanagement.dto.UserAuthenticatedDTO;
import co.harborbytes.wortedeck.usermanagement.dto.UserCreateInputDTO;
import co.harborbytes.wortedeck.usermanagement.dto.UserLoginDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final static String userCreatedSuccesfullyMessage = "User registered succesfully";
    private final AuthenticationManager authenticationManager;
    private final GoogleTokenVerificationService googleTokenVerificationService;

    @Transactional
    public String register(final UserCreateInputDTO userCreateInputDTO) {

        final User user = userMapper.dtoToUser(userCreateInputDTO);
        user.setPassword(passwordEncoder.encode(userCreateInputDTO.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        return userCreatedSuccesfullyMessage;
    }

    public UserAuthenticatedDTO login(final UserLoginDTO userLoginDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userLoginDTO.getEmail(),
                userLoginDTO.getPassword()
        ));

        final User user = userRepository.findByEmail(userLoginDTO.getEmail()).orElseThrow();
        final String token = jwtTokenUtil.generateToken(user);
        UserAuthenticatedDTO userAuthenticatedDTO = new UserAuthenticatedDTO();
        userAuthenticatedDTO.setUser(userMapper.userToDto(user));
        userAuthenticatedDTO.setToken(token);
        userAuthenticatedDTO.setExpirationDate(jwtTokenUtil.getExpirationDateFromToken(token).toInstant());

        return userAuthenticatedDTO;
    }

    @Transactional
    public UserAuthenticatedDTO googleLogin(final String googleIdToken) {
        final User tokenUser = this.googleTokenVerificationService.verify(googleIdToken);
        if (userRepository.findByEmail(tokenUser.getEmail()).isEmpty()) {
            userRepository.save(tokenUser);
        }

        final String token = jwtTokenUtil.generateToken(tokenUser);
        final UserAuthenticatedDTO userAuthenticatedDTO = new UserAuthenticatedDTO();
        userAuthenticatedDTO.setUser(userMapper.userToDto(tokenUser));
        userAuthenticatedDTO.setToken(token);
        userAuthenticatedDTO.setExpirationDate(jwtTokenUtil.getExpirationDateFromToken(token).toInstant());

        return userAuthenticatedDTO;
    }
}

package co.harborbytes.wortedeck;

import co.harborbytes.wortedeck.usermanagement.Role;
import co.harborbytes.wortedeck.usermanagement.User;
import co.harborbytes.wortedeck.usermanagement.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class AppInit implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public AppInit(final UserRepository userRepository, final PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Application starting...");
        createAppTestUser();
    }

    @Transactional
    private void createAppTestUser(){
        User user = new User();
        user.setFirstName("Carlos");
        user.setLastName("Bacca");
        user.setEmail("carlos.bacca@gmail.com");
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode("12345678"));
        userRepository.save(user);
    }
}

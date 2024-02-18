package co.harborbytes.wortedeck;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public class AppInit implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Application starting...");
    }
}

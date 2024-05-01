package co.harborbytes.wortedeck.usermanagement;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleTokenVerificationService {

    private final String CLIENT_ID;


    @Autowired
    public GoogleTokenVerificationService(@Value("${google-client-id}") final String CLIENT_ID) {
        this.CLIENT_ID = CLIENT_ID;
    }

    public User verify(final String token) throws UsernameNotFoundException {

        try {
            final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();

            final GoogleIdToken idToken = verifier.verify(token);
            final GoogleIdToken.Payload payload = idToken.getPayload();
            final boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());

            final User user = new User();
            user.setResponsibility(Role.USER);
            user.setEmail(payload.getEmail());
            user.setFirstName((String) payload.get("given_name"));
            user.setLastName((String) payload.get("family_name"));
            user.setPassword(payload.getSubject());
            return user;
        } catch (Exception ex) {
            throw new UsernameNotFoundException("Unable to verify google token for user");
        }
    }

}

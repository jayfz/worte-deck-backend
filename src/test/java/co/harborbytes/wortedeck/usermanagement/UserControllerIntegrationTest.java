package co.harborbytes.wortedeck.usermanagement;

import co.harborbytes.wortedeck.Application;
import co.harborbytes.wortedeck.ReplaceCamelCase;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class
)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties"
)

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayNameGeneration(ReplaceCamelCase.class)
class UserControllerIntegrationTest {

    private final MockMvc mvc;
    private final UserRepository userRepository;
    private final ObjectMapper jsonMapper;
    private final PasswordEncoder passwordEncoder;
    private User testUser;

    @Autowired
    public UserControllerIntegrationTest(final MockMvc mvc, final UserRepository userRepository, final ObjectMapper jsonMapper, final PasswordEncoder passwordEncoder) {
        this.mvc = mvc;
        this.jsonMapper = jsonMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

        this.jsonMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
    }

    @BeforeEach
    public void setUpUser() {
        userRepository.deleteAll();
        userRepository.flush();
        User user = new User();
        user.setResponsibility(Role.USER);
        user.setEmail("user@example.com");
        user.setPassword(passwordEncoder.encode("secret43"));
        user.setFirstName("Giga");
        user.setLastName("Chad");
        testUser = user;
        userRepository.save(user);
    }

    @Test
    void contextLoads() {
        assertThat(userRepository).isNotNull();
    }

    @Nested
    class PostRequestsFail {

        @Nested
        class WithUnauthorizedStatus {

            @ParameterizedTest(name = "username: {0} and password: {1}")
            @CsvSource(value = {
                    "jonas@harborbytes.co, betterjonas",
                    "nicolas@harborbytes.co, supersafepassword",
                    "johana@harborbytes.co, superawesome32"
            })
            public void whenAttemptingLoginAndUserDoesNotExist(final String username, final String password) throws Exception {
                Integer expectedErrorCount = 1;
                Map<String, Object> payload = new HashMap<>();
                payload.put("email", username);
                payload.put("password", password);

                failedPostRequest("login", payload, expectedErrorCount, status().isUnauthorized());
            }

            @ParameterizedTest(name = "with password: {0}")

            @ValueSource( strings = {
                    "betterjonas",
                    "supersafepassword",
                    "superawesome32"
            })
            public void whenAttemptingLoginAndUserExistsButPasswordIsIncorrect(final String password) throws Exception {
                Integer expectedErrorCount = 1;
                Map<String, Object> payload = new HashMap<>();
                payload.put("email", testUser.getEmail());
                payload.put("password", password);

                failedPostRequest("login", payload, expectedErrorCount, status().isUnauthorized());
            }
        }

        @Nested
        class WithConflictStatus {
            @Test
            public void whenAttemptingToRegisterAndUserAlreadyExist() throws Exception {
                Map<String, Object> payload = new HashMap<>();
                payload.put("email", testUser.getEmail());
                payload.put("password", "secret43");
                payload.put("firstName", testUser.getUsername());
                payload.put("lastName", testUser.getLastName());

                MockHttpServletRequestBuilder requestBuilder = post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON);
                String accountJsonString = jsonMapper.writeValueAsString(payload);
                requestBuilder.content(accountJsonString);

                mvc.perform(requestBuilder)
                        .andDo(print())
                        .andExpect(status().isConflict())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                        .andExpect(jsonPath("$.outcome", is("fail")))
                        .andExpect(jsonPath("$.status", is(409)));
            }
        }
    }


    @Nested
    class PostRequestsSucceed {

        @Nested
        class WithOkayStatus {

            @Test
            public void whenAttemptingLoginAndUserExists() throws Exception {
                Map<String, Object> payload = new HashMap<>();
                payload.put("email", testUser.getEmail());
                payload.put("password", "secret43");

                MockHttpServletRequestBuilder requestBuilder = post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON);
                String accountJsonString = jsonMapper.writeValueAsString(payload);
                requestBuilder.content(accountJsonString);

                mvc.perform(requestBuilder)
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.outcome", is("success")))
                        .andExpect(jsonPath("$.payload.user", is(notNullValue())))
                        .andExpect(jsonPath("$.payload.token", is(notNullValue())))
                        .andExpect(jsonPath("$.payload.expirationDate", is(notNullValue())));
            }

            @ParameterizedTest(name = "username: {2} and password: {3}")
            @CsvSource(value = {
                    "jonas, viel, jonas@harborbytes.co, betterjonas",
                    "nicolas, wehr, nicolas@harborbytes.co, supersafepassword",
                    "johana, hopfsman, johana@harborbytes.co, secret422"
            })
            public void whenAttemptingToSignupAndUserDoesntExistAlready(final String firstName, final String lastName, final String email, final String password) throws Exception {
                Map<String, Object> payload = new HashMap<>();
                payload.put("email", email);
                payload.put("password", password);
                payload.put("firstName", firstName);
                payload.put("lastName", lastName);

                MockHttpServletRequestBuilder requestBuilder = post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON);
                String accountJsonString = jsonMapper.writeValueAsString(payload);
                requestBuilder.content(accountJsonString);

                mvc.perform(requestBuilder)
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.outcome", is("success")))
                        .andExpect(jsonPath("$.payload", is("User registered succesfully")));
            }
        }
    }

    public void failedPostRequest(final String endpoint, final Object content, final Integer expectedErrorCount, final ResultMatcher expectedHttpStatus) throws Exception {
        final String path = String.format("%s/%s", "/api/v1/auth", endpoint);
        MockHttpServletRequestBuilder requestBuilder = post(path).contentType(MediaType.APPLICATION_JSON);
        doFailedRequest(content, requestBuilder, expectedErrorCount, expectedHttpStatus);
    }

    public void doFailedRequest(final Object content, final MockHttpServletRequestBuilder builder, final Integer expectedErrorCount, final ResultMatcher expectedHttpStatus) throws Exception {

        if (content instanceof byte[] bytes) {
            builder.content(bytes);
        } else {
            String accountJsonString = jsonMapper.writeValueAsString(content);
            builder.content(accountJsonString);
        }

        mvc.perform(builder)
                .andDo(print())
                .andExpect(expectedHttpStatus);

    }
}

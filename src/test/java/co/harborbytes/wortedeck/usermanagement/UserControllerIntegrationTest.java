package co.harborbytes.wortedeck.usermanagement;

import co.harborbytes.wortedeck.Application;
import co.harborbytes.wortedeck.ReplaceCamelCase;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
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

    @BeforeAll
    private void setUpUser() {
        User user = new User();
        user.setRole(Role.USER);
        user.setEmail("user@example.com");
        user.setPassword(passwordEncoder.encode("secret"));
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
                    "johana@harborbytes.co, secret"
            })
            public void whenAttemptingLoginAndUserDoesNotExist(final String username, final String password) throws Exception {
                Integer expectedErrorCount = 1;
                Map<String, Object> payload = new HashMap<>();
                payload.put("email", username);
                payload.put("password", password);

                failedPostRequest("login", payload, expectedErrorCount, status().isUnauthorized());
            }
        }
    }


    @Nested
    class PostRequestsSucceed {

        @Nested
        class WithOkayStatus {

            @Test
            public void whenAttemptingLoginAndUserExists() throws Exception {
                Integer expectedErrorCount = 1;
                Map<String, Object> payload = new HashMap<>();
                payload.put("email", testUser.getEmail());
                payload.put("password", "secret");

                MockHttpServletRequestBuilder requestBuilder = post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON);
                String accountJsonString = jsonMapper.writeValueAsString(payload);
                requestBuilder.content(accountJsonString);

                mvc.perform(requestBuilder)
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status", is("success")))
                        .andExpect(jsonPath("$.data.user", is(notNullValue())))
                        .andExpect(jsonPath("$.data.token", is(notNullValue())))
                        .andExpect(jsonPath("$.data.expirationDate", is(notNullValue())));
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

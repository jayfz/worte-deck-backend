package co.harborbytes.wortedeck.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Success<T> {

    private final String outcome = "success";
    private final T payload;

    public Success(final T payload) {
        this.payload = payload;
    }
}

package co.harborbytes.wortedeck.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Success<T> {

    private final String outcome = "success";
    private final T data;
    private PageSummary page;

    public Success(final T data) {
        this.data = data;
    }

    public Success(final Page pageable) {
        this.data = (T) pageable.getContent();
        this.page = new PageSummary(pageable.getTotalElements(), pageable.getTotalPages(), pageable.isFirst(), pageable.isLast(),pageable.getSort().toString(), pageable.getNumber());

    }
    private record PageSummary(long totalElements, long totalPages, boolean first, boolean last, String order, int number) {}
}
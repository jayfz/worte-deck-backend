package co.harborbytes.wortedeck.shared;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class SuccessPage<T extends Page<?>> extends Success<List<?>> {

    private final PageSummary page;

    public SuccessPage(T content) {
        super(content.getContent());
        this.page = new PageSummary(content.getTotalElements(), content.getTotalPages(), content.isFirst(), content.isLast(), content.getSort().toString(), content.getNumber());
    }

    private record PageSummary(long totalElements, long totalPages, boolean first, boolean last, String order,
                               int number) {
    }
}

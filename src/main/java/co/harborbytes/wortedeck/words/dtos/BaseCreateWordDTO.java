package co.harborbytes.wortedeck.words.dtos;

import co.harborbytes.wortedeck.words.WordKind;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateNounDTO.class, name = "NOUN"),
        @JsonSubTypes.Type(value = CreateAdjectiveDTO.class, name = "ADJECTIVE"),
        @JsonSubTypes.Type(value = CreateVerbDTO.class, name = "VERB"),
        @JsonSubTypes.Type(value = CreateAdverbDTO.class, name = "ADVERB"),
        @JsonSubTypes.Type(value = CreateCommonExpressionDTO.class, name = "COMMON_EXPRESSION"),
})
public abstract class BaseCreateWordDTO {

    @NotNull
    @Size(min = 2, max = 128)
    private String word;

    @NotNull
    private WordKind kind;

    @NotNull
    @NotEmpty
    private String[] pronunciations;

    @NotEmpty
    private String[] englishTranslations;

    @NotNull
    @Size(min = 2, max = 256)
    private String germanExample;

    @NotNull
    @Size(min = 2, max = 256)
    private String englishExample;

    @NotEmpty
    private String[] matches;
}

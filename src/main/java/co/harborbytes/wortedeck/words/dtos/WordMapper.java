package co.harborbytes.wortedeck.words.dtos;

import co.harborbytes.wortedeck.words.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring" )
public abstract class WordMapper {
    public WordDTO wordToDto(Word source) {
        if (source instanceof Noun word) return nounToDTO(word);
        if (source instanceof Adjective word) return adjectiveToDTO(word);
        if (source instanceof Verb word) return verbToDTO(word);
        if (source instanceof Adverb word) return adverbToDTO(word);
        if (source instanceof CommonExpression word) return commonExpressionToDto(word);

        return baseWordToDto(source);
    }

    public abstract WordDTO nounToDTO(Noun noun);

    public abstract WordDTO adjectiveToDTO(Adjective adjective);

    public abstract WordDTO verbToDTO(Verb verb);

    public abstract WordDTO adverbToDTO(Adverb adverb);

    public abstract WordDTO commonExpressionToDto(CommonExpression commonExpression);

    public abstract WordDTO baseWordToDto(Word word);

}

package co.harborbytes.wortedeck.words.dtos;

import co.harborbytes.wortedeck.words.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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


    @Mapping(source = "kind", target = "type")
    public abstract WordDTO nounToDTO(Noun noun);

    @Mapping(source = "kind", target = "type")
    public abstract WordDTO adjectiveToDTO(Adjective adjective);

    @Mapping(source = "kind", target = "type")
    public abstract WordDTO verbToDTO(Verb verb);

    @Mapping(source = "kind", target = "type")
    public abstract WordDTO adverbToDTO(Adverb adverb);

    @Mapping(source = "kind", target = "type")
    public abstract WordDTO commonExpressionToDto(CommonExpression commonExpression);

    @Mapping(source = "kind", target = "type")
    public abstract WordDTO baseWordToDto(Word word);


    @Mapping(source = "nounGender", target = "gender")
    @Mapping(source = "nounPlural", target = "plural")
    @Mapping(source = "verbRegular", target = "isRegular")
    @Mapping(source = "verbSeparable", target = "isSeparable")
    @Mapping(source = "verbWithPrefix", target = "hasPrefix")
    @Mapping(source = "adjectiveComparable", target = "isComparable")
    @Mapping(source = "adjectiveComparative", target = "comparative")
    @Mapping(source = "adjectiveSuperlative", target = "superlative")
    @Mapping(source = "kind", target = "type")
    public abstract WordDTO rawWordToDTO(DictionaryWord dictionaryWord);


    public  Word createWordToWord(BaseCreateWordDTO baseWord){
        if(baseWord instanceof  CreateNounDTO createNounDTO) return createNounDTOToWord(createNounDTO);
        if(baseWord instanceof CreateAdjectiveDTO createAdjectiveDTO) return createAdjectiveDTOToWord(createAdjectiveDTO);
        if(baseWord instanceof CreateVerbDTO createVerbDTO) return createVerbDTOToWord(createVerbDTO);
        if(baseWord instanceof  CreateAdverbDTO createAdverbDTO) return createAdverbDTOToWord(createAdverbDTO);
        if(baseWord instanceof CreateCommonExpressionDTO createCommonExpressionDTO ) return createCommonExpressionDTOToWord(createCommonExpressionDTO);

        throw new RuntimeException("Unknown word type: " + baseWord.getKind().toString());

    }

    public abstract Noun createNounDTOToWord(CreateNounDTO createNounDTO);
    public abstract Adjective createAdjectiveDTOToWord(CreateAdjectiveDTO createAdjectiveDTO);
    public abstract Verb createVerbDTOToWord(CreateVerbDTO createVerbDTO);
    public abstract Adverb createAdverbDTOToWord(CreateAdverbDTO createAdverbDTO);
    public abstract CommonExpression createCommonExpressionDTOToWord(CreateCommonExpressionDTO createCommonExpressionDTO);

}

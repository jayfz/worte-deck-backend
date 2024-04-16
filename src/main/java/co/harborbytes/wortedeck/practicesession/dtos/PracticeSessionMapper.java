package co.harborbytes.wortedeck.practicesession.dtos;

import co.harborbytes.wortedeck.practicesession.PracticeSession;
import co.harborbytes.wortedeck.practicesession.PracticeSessionResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class PracticeSessionMapper {

    public PracticeSessionDTO practiceSessionToDTO(PracticeSession source){
        if(source == null) return null;
        PracticeSessionDTO practiceSessionDTO = new PracticeSessionDTO();
        practiceSessionDTO.setPracticeSessionId(source.getId());
        return practiceSessionDTO;
    }

    public abstract PracticeSessionResultSummaryDTO practiceSessionResultToSummaryDTO(PracticeSessionResult source);
}

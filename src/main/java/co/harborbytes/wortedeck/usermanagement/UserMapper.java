package co.harborbytes.wortedeck.usermanagement;


import co.harborbytes.wortedeck.usermanagement.dto.UserCreateInputDTO;
import co.harborbytes.wortedeck.usermanagement.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User dtoToUser(final UserCreateInputDTO dto);
    UserDTO userToDto(final User user);
}

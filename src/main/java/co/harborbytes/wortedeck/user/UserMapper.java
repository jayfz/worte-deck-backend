package co.harborbytes.wortedeck.user;


import co.harborbytes.wortedeck.user.dto.UserCreateInputDTO;
import co.harborbytes.wortedeck.user.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User dtoToUser(UserCreateInputDTO dto);
    UserDTO userToDto(User user);
}

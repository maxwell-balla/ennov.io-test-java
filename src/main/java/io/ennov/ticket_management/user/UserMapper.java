package io.ennov.ticket_management.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToUserDto(User user);

    @Mapping(target = "id", ignore = true)
    User userDtoToUser(UserDto dto);
}



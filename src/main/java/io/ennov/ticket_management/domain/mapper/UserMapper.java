package io.ennov.ticket_management.domain.mapper;

import io.ennov.ticket_management.domain.User;
import io.ennov.ticket_management.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToUserDto(User user);
}



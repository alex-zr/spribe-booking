package co.spribe.mapper;

import co.spribe.controller.dto.UserDto;
import co.spribe.model.User;
import org.mapstruct.Mapper;

@Mapper(config = Config.class)
public interface UserMapper {

  UserDto toDto(User user);

}

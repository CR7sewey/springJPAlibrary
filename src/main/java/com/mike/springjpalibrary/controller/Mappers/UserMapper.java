package com.mike.springjpalibrary.controller.Mappers;

import com.mike.springjpalibrary.model.User;
import com.mike.springjpalibrary.model.dto.UserDTO;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO userToUserDTO(User user);
    User userDTOToUser(UserDTO userDTO);


}

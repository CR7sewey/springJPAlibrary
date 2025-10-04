package com.mike.springjpalibrary.controller;

import com.mike.springjpalibrary.controller.Mappers.UserMapper;
import com.mike.springjpalibrary.model.User;
import com.mike.springjpalibrary.model.dto.UserDTO;
import com.mike.springjpalibrary.repository.UserRepository;
import com.mike.springjpalibrary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements GeneralisedController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<Void> addUser(@RequestBody UserDTO userDTO) {

        var user = userMapper.userDTOToUser(userDTO);
        userService.save(user);
        URI uri = generateURI(user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).location(uri).build();
    }

}

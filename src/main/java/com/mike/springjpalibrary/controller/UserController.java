package com.mike.springjpalibrary.controller;

import com.mike.springjpalibrary.controller.Mappers.UserMapper;
import com.mike.springjpalibrary.model.User;
import com.mike.springjpalibrary.model.dto.UserDTO;
import com.mike.springjpalibrary.repository.UserRepository;
import com.mike.springjpalibrary.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements GeneralisedController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<Void> addUser(@RequestBody @Valid UserDTO userDTO) {

        var user = userMapper.userDTOToUser(userDTO);
        userService.save(user);
        URI uri = generateURI(user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).location(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        var users = userService.getUsers();
        var usersDTO = users.stream().map(userMapper::userToUserDTO).toList();
        return ResponseEntity.ok(usersDTO);
    }

    @DeleteMapping("{username}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userService.deleteByUsername(username);
        return ResponseEntity.noContent().build();

    }

}

package com.mike.springjpalibrary.service;

import com.mike.springjpalibrary.model.User;
import com.mike.springjpalibrary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // bean in security Configuratipn

    public void save(User user) {
        var pass = user.getPassword();
        pass = passwordEncoder.encode(pass);
        user.setPassword(pass);
        userRepository.save(user);
    }

    public User getByUsername(String login) {
        return userRepository.findByUsername(login);
    }

    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }


}

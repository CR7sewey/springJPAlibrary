package com.mike.springjpalibrary.service;

import com.mike.springjpalibrary.exceptions.DuplicateRegister;
import com.mike.springjpalibrary.model.RegisteredClient;
import com.mike.springjpalibrary.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisteredClient findByClientId(String clientId) {
        return clientRepository.findByClientId(clientId).orElse(null);
    }

    public void save(RegisteredClient registeredClient) {
        if (registeredClient.getId() != null && clientRepository.findById(registeredClient.getId()).isPresent()) {
            throw new DuplicateRegister("Client with id " + registeredClient.getId() + " already exists");
        }
        if (validateFieldRegister(registeredClient)) {
            throw new DuplicateRegister("Client with id " + registeredClient.getClientId() + " already exists");
        }
        registeredClient.setClientSecret(passwordEncoder.encode(registeredClient.getClientSecret()));
        clientRepository.save(registeredClient);
    }

    private boolean validateFieldRegister(RegisteredClient registeredClient) {
        return clientRepository.existsByClientId(registeredClient.getClientId());
    }

}

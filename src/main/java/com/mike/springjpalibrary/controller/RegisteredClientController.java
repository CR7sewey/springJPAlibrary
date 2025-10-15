package com.mike.springjpalibrary.controller;

import com.mike.springjpalibrary.model.RegisteredClient;
import com.mike.springjpalibrary.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class RegisteredClientController implements GeneralisedController {

    private final ClientService clientService;


    @GetMapping("/{clientId}")
    public ResponseEntity<RegisteredClient> findById(@PathVariable("clientId") String id) {
        //var uuid = UUID.fromString(id);
        RegisteredClient rc = clientService.findByClientId(id);
        return ResponseEntity.ok(rc);

    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> save(@RequestBody RegisteredClient registeredClient) {

        clientService.save(registeredClient);
        log.info("Client saved with id {} with scope {}", registeredClient.getClientId() , registeredClient.getScope());
        URI location = generateURI(registeredClient.getId());
        return ResponseEntity.created(location).build();

    }


}

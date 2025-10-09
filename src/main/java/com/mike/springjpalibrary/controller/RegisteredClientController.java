package com.mike.springjpalibrary.controller;

import com.mike.springjpalibrary.model.RegisteredClient;
import com.mike.springjpalibrary.service.RegisteredClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class RegisteredClientController implements GeneralisedController {

    private final RegisteredClientService clientService;


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
        URI location = generateURI(registeredClient.getId());
        return ResponseEntity.created(location).build();

    }


}

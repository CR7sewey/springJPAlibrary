package com.mike.springjpalibrary.repository;

import com.mike.springjpalibrary.model.RegisteredClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<RegisteredClient, UUID> {

    boolean existsByClientId(String clientId);

    Optional<RegisteredClient> findByClientId(String clientId);
}

package com.mike.springjpalibrary.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

//https://docs.spring.io/spring-authorization-server/reference/core-model-components.html
@Entity
@Data
@Table(name = "tb_client")
public class RegisteredClient implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String clientId; // The client identifier

    @Column(nullable = false, length = 400)
    private String clientSecret; // The client’s secret. The value should be encoded using Spring Security’s PasswordEncoder

    @Column(nullable = false, length = 200)
    private String redirectUri; // uri to where the code from Authorization Server goes

    @Column(length = 50)
    private String scope; // actuation scope, allowed requests


}
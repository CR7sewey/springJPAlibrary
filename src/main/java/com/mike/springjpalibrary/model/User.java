package com.mike.springjpalibrary.model;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "tb_user")
@EntityListeners(AuditingEntityListener.class) // listening for some anotations (created etc)
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false, length = 150)
    private String email;

    @Column(nullable = false, unique = true, length = 20)
    private String username;

    @Column(nullable = false, length = 300)
    private String password;

    @Type(ListArrayType.class) // traduction from list to array
    @Column(name = "roles", columnDefinition = "varchar[]")
    private List<String> roles;

    @CreatedDate
    @Column(name = "register_date")
    private LocalDateTime registerDate;

    @LastModifiedDate
    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    public User() {}

    public User(String username, String hash_password) {
        this.username = username;
        this.password = hash_password;
    }


}

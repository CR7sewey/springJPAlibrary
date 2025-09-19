package com.mike.springjpalibrary.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_author", schema = "public")
@Getter // get and set created on compile time - lombok
@Setter
@EntityListeners(AuditingEntityListener.class) // listening for some anotations (created etc)
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "nome", nullable = false, length = 200)
    private String nome;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(name = "nationality", nullable = false, length = 50)
    private String nationality;

    @CreatedDate
    @Column(name = "register_date")
    private LocalDateTime registerDate;

    @LastModifiedDate
    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    //@CreatedBy
    @Column(name = "id_user")
    private UUID idUser;

    /**
     * Data Cadastro
     * Data Ultima Atualização
     * Usuário Ultima Atualização
     */

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // 1 author can have several books - mapping; lazy is default
    //@Transient
    private List<Book> books = new ArrayList<>();

    @Deprecated
    public Author() {}

    public Author(UUID id, String nome, LocalDate birthDate, String nationality) {
        this.id = id;
        this.nome = nome;
        this.birthDate = birthDate;
        this.nationality = nationality;
    }

   /* public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }*/

    @Override
    public String toString() {
        return "Author{" + "id=" + id + " name=" + nome + " birthDate=" + birthDate + " nationality=" + nationality + '}';
    }
}

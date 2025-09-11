package com.mike.springjpalibrary.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;
    private String nome;
    @Column(name = "birth_date")
    private Date birthDate;
    private String nationality;

    public Author() {}

    public Author(UUID id, String nome, Date birthDate, String nationality) {
        this.id = id;
        this.nome = nome;
        this.birthDate = birthDate;
        this.nationality = nationality;
    }

    public UUID getId() {
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "Author{" + "id=" + id + " name=" + nome + " birthDate=" + birthDate + " nationality=" + nationality + '}';
    }
}

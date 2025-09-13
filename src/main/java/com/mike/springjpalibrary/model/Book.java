package com.mike.springjpalibrary.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_book")
@Data // get and setter and others (ToString, equals and hascode nad RequiredArgsConstructor)
@ToString(exclude = "author")
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 20)
    private String isbn;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, name = "data_publicacao")
    private LocalDate dataPublicacao;

    @Enumerated(EnumType.STRING) // .ORDINAL is the position of the enumeration
    @Column(nullable = false, length = 30)
    private Genero genero;

    @Column(precision = 20, scale = 2)
    private BigDecimal preco;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) // 1 author can have mutliple book - current table; fetch is EAGER by default
    @JoinColumn(name = "id_author")
    private Author author;

    @Deprecated
    public Book() {}

    public Book(String isbn, String titulo, LocalDate dataPublicacao, Author author) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.dataPublicacao = dataPublicacao;
        this.author = author;
    }




}

package com.mike.springjpalibrary.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_book")
@Data // get and setter and others (ToString, equals and hascode nad RequiredArgsConstructor)
@ToString(exclude = "author")
@EntityListeners(AuditingEntityListener.class)
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
    @Column(length = 30)
    private Genero genero;

    @Column(precision = 20, scale = 2)
    private BigDecimal preco;

    @CreatedDate
    @Column(name = "register_date")
    private LocalDateTime registerDate;

    @LastModifiedDate
    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    //@CreatedBy
    @Column(name = "id_user")
    private UUID idUser;

    @ManyToOne(fetch = FetchType.LAZY) // 1 author can have mutliple book - current table; fetch is EAGER by default
    @JoinColumn(name = "id_author", nullable = false)
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
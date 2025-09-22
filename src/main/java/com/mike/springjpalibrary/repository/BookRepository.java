package com.mike.springjpalibrary.repository;

import com.mike.springjpalibrary.model.Author;
import com.mike.springjpalibrary.model.Book;
import com.mike.springjpalibrary.model.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    // https://www.google.com/search?q=jpa+query+methods+spring&rlz=1C1ONGR_pt-PTPT1162PT1164&oq=jpa+query+methods+spring&gs_lcrp=EgZjaHJvbWUyCwgAEEUYHhg5GKkGMgkIARAAGB4YqQYyCQgCEAAYHhipBjIICAMQABgWGB4yCAgEEAAYFhgeMggIBRAAGBYYHjIICAYQABgWGB4yCAgHEAAYFhgeMggICBAAGBYYHjIKCAkQABiABBiiBNIBCDc1NjBqMGo0qAIAsAIB&sourceid=chrome&ie=UTF-8
    List<Book> findByAuthor(Author author);
    boolean existsByAuthor(Author author);
    List<Book> findByTitulo(String Titulo);
    Book findByIsbn(String Isbn);
    List<Book> findByTituloAndPreco(String Title, BigDecimal preco);


    // select * from tb_book where dataPublicacao between x and y
    List<Book> findByDataPublicacaoBetweenOrderByDataPublicacao(LocalDate dataPublicacaoAfter, LocalDate dataPublicacaoBefore);

    List<Book> findByTituloLike(String Titulo);

    // JPQL
    @Query(" select l from Book as l order by l.titulo, l.preco")
    List<Book> mostrarTodosOsLivros();

    @Query("""
        select l.genero
        from Book l join l.author a where a.nationality = 'Portuguesa'
        order by l.genero
""")
    List<String> mostrarGenerosAutoresPortugueses();

    // named
    @Query("select l from Book l where l.genero = :genero")
    List<Book> mostraLivrosPorGenero(@Param("genero") Genero genero);

    //positional
    @Query("select l from Book l where l.genero = ?1 order by ?2")
    List<Book> mostraLivrosPorGenero2(Genero genero, String titulo);

    @Modifying // bcs we are writing/changing on db
    @Transactional // to be allowed to make changes on db
    @Query(" delete from Book l where l.genero = :genero")
    void deleteByGenero(@Param("genero") Genero genero);

    @Modifying // bcs we are writing/changing on db
    @Transactional // to be allowed to make changes on db
    @Query(" update Book b set b.dataPublicacao = ?1")
    void updataDataPub(LocalDate dataPublicacao);

}

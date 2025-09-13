package com.mike.springjpalibrary.repository;

import com.mike.springjpalibrary.model.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void saveAuthor() {
        Author author = new Author();
        author.setNome("MiguelDelete");
        author.setBirthDate(LocalDate.of(1980, 1, 1));
        author.setNationality("Portuguese");
        var author1 = authorRepository.save(author);
        System.out.println(author1);
    }

    @Test
    public void updateAuthor() {
        var id = UUID.fromString("5ce335ec-0744-4ada-8092-fb127374ac48");

        var author = authorRepository.findById(id);
        if (author.isPresent()) {

            author.get().setId(id);
            author.get().setNome("Miguel 2");
            var author1 = authorRepository.save(author.get());
            System.out.println(author1);
        }
        else  {
            System.out.println("Author not found");
        }
    }

    @Test
    public void deleteAuthor() {

        var author = authorRepository.findByNome("MiguelDelete");
        if (author != null) {
            authorRepository.delete(author);
        }
        else  {
            System.out.println("Author not found");
        }
    }

    @Test
    public void findFirst10() {
        List<Author> authors = authorRepository.findAll();
        var printableList = authors.subList(1, Math.min(authors.size(), 10));
        printableList.forEach(System.out::println);
    }
}

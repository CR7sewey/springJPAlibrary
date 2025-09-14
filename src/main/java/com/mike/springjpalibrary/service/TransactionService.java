package com.mike.springjpalibrary.service;

import com.mike.springjpalibrary.model.Author;
import com.mike.springjpalibrary.repository.AuthorRepository;
import com.mike.springjpalibrary.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service // bean gerenciado
public class TransactionService {

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;

    @Transactional
    public void executeTransaction() {
        var author =  new Author();
        author.setNome("Miguel Is not going to be save");
        author.setBirthDate(LocalDate.of(1980, 1, 1));
        author.setNationality("Portuguese");

        var author2 = new Author();
        author2.setNome("Miguel 2 Is not going to be save");
        author2.setBirthDate(LocalDate.of(1980, 1, 1));
        // not nationality -> so exception

        try {
            // state transient to managed
            authorRepository.save(author);
            authorRepository.save(author2);
            // commit/flush
        }
        catch (Exception e) {
            // rollback
            throw new RuntimeException(e.getMessage());

        }
    }


    @Transactional
    public void atualizarSemAtualizar() {
        var book = bookRepository.findById(UUID.fromString("d222f2d4-6fca-4b59-9fc0-ee397041bf22")).orElse(null);
        // managed state in this point
        assert book != null;
        book.setDataPublicacao(LocalDate.of(2025,1,1));

        // bookRepository.save(book); // not needed bcs transactional and this ententity book is committed

        // util in the next scenario
        /**
         * save book - id é gerado msm sem ter sido commitado (so o é qunaod o metodo transactional acaba)!
         * repository.save(book);
         *
         * id book
         * var id = book.getId();
         *
         * save in cloud bucket
         * bucketService.save(book.getPic(), id + ".png");
         *
         * update name
         * book.setNomeArquivoFoto(id + ".png);
         * rpository.save()
         *
         * como neste estado managed, nao preciso de chamar os repository.save
         * pois como esotu numa transacao, qq alteracao feita à entidade qunaod esta esta
         * no estado managed é persistido no final quando commited para a database
         */

    }


}

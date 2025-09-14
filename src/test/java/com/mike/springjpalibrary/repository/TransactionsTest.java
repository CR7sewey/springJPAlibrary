package com.mike.springjpalibrary.repository;

import com.mike.springjpalibrary.model.Author;
import com.mike.springjpalibrary.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@SpringBootTest
public class TransactionsTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TransactionService transactionService;

    /**
     * Commit - > confirm changes
     * Rollback - > undo changes
     */
    @Test
    @Transactional // open transaction - in sql is begin; execution; commit (to changes be applied) or rollback
    void simpleTransaction() {

        transactionService.executeTransaction();
    }

    @Test
    @Transactional
    void simpleTransaction2() {
        transactionService.atualizarSemAtualizar();
    }



}

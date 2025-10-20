package com.mike.springjpalibrary.controller;

import com.mike.springjpalibrary.controller.Mappers.AuthorMapper;
import com.mike.springjpalibrary.model.Author;
import com.mike.springjpalibrary.model.User;
import com.mike.springjpalibrary.service.AuthorService;
import com.mike.springjpalibrary.service.UserService;
import com.mike.springjpalibrary.validator.AuthorValidator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@TestPropertySource(locations="classpath:application-test.yml")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthorController.class) // contexto para testar esta parte da API
public class AuthorControllerTest {

    @Autowired
    MockMvc mockMvc; // client

    @MockitoBean // mockar o bean dentro do contexto do spring
    AuthorService authorService;

    @MockitoBean
    AuthorMapper  authorMapper;

    @MockitoBean
    AuthorValidator authorValidator;

    @MockitoBean
    UserService userService;

    Author author;

    @BeforeEach
    public void setup() {
        author = new Author();
        author.setId(UUID.fromString("92d5d373-143d-4813-80a1-17c3bd3078b1"));
        author.setNome("Miguel");
        author.setNationality("Portuguese");
        author.setBirthDate(LocalDate.of(1999, 7, 7));
    }

    @Test
    void saveAuthor() throws Exception {

        Mockito.when(authorValidator.validar(Mockito.any())).thenReturn(true);
        Mockito.when(authorMapper.authorDTOToAuthor(Mockito.any())).thenReturn(author);

        Mockito.when(authorService.save(Mockito.any())).thenReturn(author);

        String json = """
                {
                    "name": "Miguel",
                    "nationality": "Portuguese"
                    "birthDate": "1999-07-07"
                }
                """;

        // execution
        ResultActions perform = mockMvc.perform(
                post("/authors")
                        .content(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(json)
        );

        // verification
        perform
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Miguel"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nationality").value("Portuguese"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate").value("1999-07-07"));


    }

    @Test
    void getAuthorById() throws Exception {


        Mockito.when(authorService.findById(Mockito.any())).thenReturn(Optional.ofNullable(author));


        // execution
        ResultActions perform = mockMvc.perform(
                get("/authors/{id}", author.getId())
        );

        // verification
        perform
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(author.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Miguel"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nationality").value("Portuguese"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate").value("1999-07-07"));
    }

    @Test
    void getAuthorByIdNotFound() throws Exception {


        Mockito.when(authorService.findById(Mockito.any())).thenThrow(
            EntityNotFoundException.class
        );


        // execution
        ResultActions perform = mockMvc.perform(
                get("/authors/{id}", author.getId())
        );

        // verification
        perform
                .andExpect(MockMvcResultMatchers.status().isNotFound())
               ;
    }

    @Test
    void deleteAuthor() throws Exception {


        Mockito.doNothing().when(authorService).delete(Mockito.any());

        // execution
        ResultActions perform = mockMvc.perform(
                delete("/authors/{id}", author.getId())
        );

        // verification
        perform
                .andExpect(MockMvcResultMatchers.status().isNoContent())
        ;
    }

    @Test
    void deleteAuthorNotExist() throws Exception {


        Mockito.doThrow(EntityNotFoundException.class).when(authorService).delete(Mockito.any());

        // execution
        ResultActions perform = mockMvc.perform(
                delete("/authors/{id}", author.getId())
        );

        // verification
        perform
                .andExpect(MockMvcResultMatchers.status().isNotFound())
        ;
    }

    @Test
    void findByNameOrNationality()  throws Exception {

        Mockito.when(authorService
                .findByExample("", LocalDate.of(1999,7,7),"")).thenReturn(List.of(author));


// execution
        ResultActions perform = mockMvc.perform(
                get("/authors")
                        .param("name", "Miguel")
                        .param("nationality", "Portuguese")
                        .param("birthDate", "1999-07-07")
        );

        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nome").value("Miguel"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nationality").value("Portuguese"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].birthDate").value("1999-07-07"));


    }

}

package com.example.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstore.dto.bookdto.BookDto;
import com.example.bookstore.dto.bookdto.CreateBookRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class BookControllerIntegrationTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource,
                          @Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        teardown(dataSource);
    }

    @AfterEach
     void afterEach(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/delete-for-book-category-tests.sql")
            );
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/delete-for-book-category-tests.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Create and save a new book")
    void saveBook_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto requestDto = createBook();
        BookDto expected = createBookDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual, "id");

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createBook_InValidRequestDto_Failed() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Odyssey")
                .setAuthor("Homer");
        String requestJson = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(post("/books")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    @DisplayName("Get all books")
    @Sql(scripts = "classpath:database/add-for-book-category-tests.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAllBooks_Ok() throws Exception {
        MvcResult result = mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andReturn();
        List<BookDto> expected = new ArrayList<>();
        expected.add(createBookDto().setId(1L));
        BookDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto[].class);
        assertEquals(expected, List.of(actual));
    }

    @Test
    @WithMockUser
    @Sql(scripts = "classpath:database/add-for-book-category-tests.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getBooksByCategoryId_Success() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/1/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto[].class);
        assertEquals(1, actual.length);
        assertEquals(actual[0].getTitle(), "Odyssey");
    }

    @Test
    @WithMockUser
    @Sql(scripts = "classpath:database/add-for-book-category-tests.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Get book by ID")
    void getBookById_Ok() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse()
                        .getContentAsString(), BookDto.class);
        assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals("Odyssey", actual.getTitle());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/add-for-book-category-tests.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Create and save a new book")
    void updateBook_Ok() throws Exception {
        long id = 1;
        BookDto updatedBookRequest = createBookDto().setIsbn("12345678");
        BookDto responseDto = createBookDto().setIsbn("12345678").setId(1L);
        String request = objectMapper.writeValueAsString(updatedBookRequest);
        MvcResult result = mockMvc.perform(put("/books/1")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(result
                .getResponse()
                .getContentAsString(), BookDto.class);
        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(responseDto, actual);
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void updateBook_NotOk_NotFound() throws Exception {
        long id = 5;
        BookDto updateBookRequest = createBookDto()
                .setIsbn("121212343434").setTitle("Updated book");
        String request = objectMapper.writeValueAsString(updateBookRequest);
        mockMvc.perform(
                        put("/books/{id}", id)
                                .content(request))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Delete a book from DB by ID")
    void deleteBook_ValidId_Success() throws Exception {
        mockMvc.perform(delete("/books/delete/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @WithMockUser
    @DisplayName("Returns 'Forbidden' status if user tries to go to admin`s endpoints")
    @Sql(scripts = "classpath:database/add-for-book-category-tests.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void userGoesToAdminsEndpoints_ReturnsForbiddenStatus() throws Exception {
        BookDto requestDto = createBookDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();

        mockMvc.perform(put("/books/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();

        mockMvc.perform(delete("/books/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    private static CreateBookRequestDto createBook() {
        return new CreateBookRequestDto()
                .setTitle("Odyssey")
                .setAuthor("Homer")
                .setIsbn("98765432")
                .setPrice(BigDecimal.valueOf(251))
                .setCategoryIds(Set.of(1L));
    }

    private static BookDto createBookDto() {
        return new BookDto()
                .setTitle("Odyssey")
                .setAuthor("Homer")
                .setIsbn("98765432")
                .setPrice(BigDecimal.valueOf(251))
                .setCategoryIds(Set.of(1L));
    }
}

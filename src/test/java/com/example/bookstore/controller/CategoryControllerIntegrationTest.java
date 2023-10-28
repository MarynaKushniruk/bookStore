package com.example.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstore.dto.categorydto.CategoryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
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
public class CategoryControllerIntegrationTest {
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
    void createCategory_ValidRequestDto_Success() throws Exception {
        CategoryDto categoryDto = new CategoryDto()
                .setId(1L)
                .setName("Detective");
        String requestJson = objectMapper.writeValueAsString(categoryDto);
        CategoryDto expected = new CategoryDto()
                .setId(1L)
                .setName("Detective");
        MvcResult result = mockMvc.perform(post("/categories")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);
        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createCategory_InValidData_BadRequest() throws Exception {
        CategoryDto invalidCategoryRequest = new CategoryDto();
        String requestJson = objectMapper.writeValueAsString(invalidCategoryRequest);
        mockMvc.perform(post("/categories")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Find and return list of existing categories")
    @Sql(scripts = "classpath:database/add-for-category-tests.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAll_ValidRequest_ReturnListOfCategories() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andReturn();
        List<CategoryDto> expected = new ArrayList<>();
        expected.add(new CategoryDto().setId(1L).setName("Fantasy"));
        CategoryDto[] actual = objectMapper.readValue(result
                .getResponse().getContentAsString(), CategoryDto[].class);
        assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @WithMockUser
    @DisplayName("Get category by id")
    @Sql(scripts = "classpath:database/add-for-category-tests.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getById_ValidId_ReturnCategory() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);
        assertNotNull(actual);
        assertEquals(actual.getId(), 1L);
        assertEquals(actual.getName(), "Fantasy");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Update existing category by Id")
    @Sql(scripts = "classpath:database/add-for-category-tests.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void updateCategory_validId_Success() throws Exception {
        long id = 1;
        CategoryDto updateCategoryRequest = new CategoryDto()
                .setName("Historical");
        CategoryDto expected = new CategoryDto()
                .setId(1L)
                .setName("Historical");
        String request = objectMapper.writeValueAsString(updateCategoryRequest);
        MvcResult result = mockMvc.perform(put("/categories/{id}", id)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/add-for-category-tests.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteCategory_Success() throws Exception {
        mockMvc.perform(delete("/categories/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()).andReturn();
    }

    @Test
    @WithMockUser
    @DisplayName("Returns 'Forbidden' status if user tries to go to admin`s endpoints")
    @Sql(scripts = "classpath:database/add-for-category-tests.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void userGoesToAdminsEndpoints_ReturnsForbiddenStatus() throws Exception {
        CategoryDto requestDto = new CategoryDto()
                .setName("Detective")
                .setDescription("Description");
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();

        mockMvc.perform(put("/categories/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();

        mockMvc.perform(delete("/categories/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }
}

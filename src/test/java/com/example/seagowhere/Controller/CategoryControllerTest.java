package com.example.seagowhere.Controller;

import com.example.seagowhere.Model.Category;
import com.example.seagowhere.Repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String API_ENDPOINT = "/api/category";

    private Category category1, category2;
    private List<Category> categoryList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();

        category1 = Category.builder()
                .name("Category 1")
                .build();

        category2 = Category.builder()
                .name("Category 2")
                .build();

        categoryList.add(category1);
        categoryList.add(category2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createCategory() throws Exception{
        String requestBody = objectMapper.writeValueAsString(category1);

        ResultActions resultActions = mockMvc.perform(post(API_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        resultActions.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.name").value(category1.getName()))
                .andExpect(result -> assertNotNull(result.getResponse().getContentAsString()));
    }

    @Test
    void getCategoryById() throws Exception{
        categoryRepository.save(category1);

        ResultActions resultActions = mockMvc.perform(get(API_ENDPOINT.concat("/{id}"), category1.getId()));

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value(category1.getName()));
    }

    @Test
    void getAllCategory() throws Exception{
        categoryRepository.saveAll(categoryList);

        ResultActions resultActions = mockMvc.perform(get(API_ENDPOINT));

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()").value(categoryList.size()));
    }

    @Test
    void updateCategory() throws Exception{
        categoryRepository.save(category1);
        Category updateCat1 = categoryRepository.findById(category1.getId()).get();
        updateCat1.setName("Updated Category 1");

        String requestBody = objectMapper.writeValueAsString(updateCat1);

        ResultActions resultActions = mockMvc.perform(put(API_ENDPOINT.concat("/{id}"), category1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value(updateCat1.getName()));

    }

    @Test
    void deleteCategory() throws Exception{
        categoryRepository.save(category1);
        Category deleteCat1 = categoryRepository.findById(category1.getId()).get();
        String expectedResponse = String.format("%s has been successfully deleted.", deleteCat1.getName());

        ResultActions resultActions = mockMvc.perform(delete(API_ENDPOINT.concat("/{id}"), category1.getId())
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(result -> assertEquals(expectedResponse, result.getResponse().getContentAsString()));

    }
}
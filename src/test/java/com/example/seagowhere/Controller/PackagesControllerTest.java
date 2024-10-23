package com.example.seagowhere.Controller;

import com.example.seagowhere.Model.Category;
import com.example.seagowhere.Model.Packages;
import com.example.seagowhere.Model.Users;
import com.example.seagowhere.Repository.CategoryRepository;
import com.example.seagowhere.Repository.PackagesRepository;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.seagowhere.Model.EnumRole.ADMIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
class PackagesControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private PackagesRepository packagesRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String ADMIN_API_ENDPOINT = "/admin/api/packages";
    private static final String PUBLIC_API_ENDPOINT = "/public/api/packages";

    private Packages package1, package2;
    private List<Packages> packagesList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        packagesRepository.deleteAll();
        categoryRepository.deleteAll();

        Category category = Category.builder()
                .name("Category 1")
                .build();

        categoryRepository.save(category);

        // Build package1
        package1 = Packages.builder()
                .category(category)
                .name("Adventure in the Alps")
                .country("Switzerland")
                .blurb("Experience the ultimate adventure in the Swiss Alps.")
                .desc("A thrilling 7-day trip filled with skiing, snowboarding, and sightseeing.")
                .price(BigDecimal.valueOf(1500.00))
                .start_date(LocalDate.of(2024, 1, 5))
                .end_date(LocalDate.of(2024, 1, 12))
                .no_of_days(7)
                .no_of_nights(6)
                .image_url("alps.jpg")
                .image_1("alps1.jpg")
                .image_2("alps2.jpg")
                .image_3("alps3.jpg")
                .image_4("alps4.jpg")
                .build();

        // Build package2
        package2 = Packages.builder()
                .category(category)
                .name("Bangkok Culinary Journey")
                .country("Thailand")
                .blurb("Discover the vibrant culture of Bangkok.")
                .desc("A 5-day culinary tour through Bangkok's famous markets and eateries.")
                .price(BigDecimal.valueOf(800.00))
                .start_date(LocalDate.of(2024,3,6))
                .end_date(LocalDate.of(2024, 3, 10))
                .no_of_days(5)
                .no_of_nights(4)
                .image_url("bangkok.jpg")
                .image_1("bangkok1.jpg")
                .image_2("bangkok2.jpg")
                .image_3("bangkok3.jpg")
                .image_4("bangkok4.jpg")
                .build();

        // Add packages to the list
        packagesList.add(package1);
        packagesList.add(package2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createPackage() throws Exception {
        Users adminUser = new Users();
        adminUser.setFirstName("Admin");
        adminUser.setLastName("Test");
        adminUser.setEmail("admin@test.com");
        adminUser.setNumber("99999999");
        adminUser.setPassword("password");
        adminUser.setRole(ADMIN);

        String requestBody = objectMapper.writeValueAsString(package1);

        ResultActions resultActions = mockMvc.perform(post(ADMIN_API_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .header("Authorization", "Bearer <JWT or Token>")
        );

        resultActions.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.name").value(package1.getName()))
                .andExpect(jsonPath("$.category.id").value(package1.getCategory().getId()))
                .andExpect(jsonPath("$.country").value(package1.getCountry()))
                .andExpect(jsonPath("$.blurb").value(package1.getBlurb()))
                .andExpect(jsonPath("$.desc").value(package1.getDesc()))
                .andExpect(jsonPath("$.price").value(package1.getPrice()))
                .andExpect(jsonPath("$.start_date").value(package1.getStart_date().toString()))
                .andExpect(jsonPath("$.end_date").value(package1.getEnd_date().toString()))
                .andExpect(jsonPath("$.no_of_days").value(package1.getNo_of_days()))
                .andExpect(jsonPath("$.no_of_nights").value(package1.getNo_of_nights()))
                .andExpect(jsonPath("$.image_url").value(package1.getImage_url()))
                .andExpect(jsonPath("$.image_1").value(package1.getImage_1()))
                .andExpect(jsonPath("$.image_2").value(package1.getImage_2()))
                .andExpect(jsonPath("$.image_3").value(package1.getImage_3()))
                .andExpect(jsonPath("$.image_4").value(package1.getImage_4()))
                .andExpect(result -> assertNotNull(result.getResponse().getContentAsString()));
    }


    @Test
    void getPackageById() throws Exception {
        packagesRepository.save(package1);

        ResultActions resultActions = mockMvc.perform(get(PUBLIC_API_ENDPOINT.concat("/{id}"), package1.getId()));

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value(package1.getName()))
                .andExpect(jsonPath("$.category.id").value(package1.getCategory().getId()))
                .andExpect(jsonPath("$.country").value(package1.getCountry()))
                .andExpect(jsonPath("$.blurb").value(package1.getBlurb()))
                .andExpect(jsonPath("$.desc").value(package1.getDesc()))
                .andExpect(jsonPath("$.price").value(package1.getPrice()))
                .andExpect(jsonPath("$.start_date").value(package1.getStart_date().toString()))
                .andExpect(jsonPath("$.end_date").value(package1.getEnd_date().toString()))
                .andExpect(jsonPath("$.no_of_days").value(package1.getNo_of_days()))
                .andExpect(jsonPath("$.no_of_nights").value(package1.getNo_of_nights()))
                .andExpect(jsonPath("$.image_url").value(package1.getImage_url()))
                .andExpect(jsonPath("$.image_1").value(package1.getImage_1()))
                .andExpect(jsonPath("$.image_2").value(package1.getImage_2()))
                .andExpect(jsonPath("$.image_3").value(package1.getImage_3()))
                .andExpect(jsonPath("$.image_4").value(package1.getImage_4()));
    }

    @Test
    void getAllPackages() throws Exception{
        packagesRepository.saveAll(packagesList);

        ResultActions resultActions = mockMvc.perform(get(PUBLIC_API_ENDPOINT));

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()").value(packagesList.size()));
    }

    @Test
    void getPackagesWithPagination() throws Exception{
        packagesRepository.saveAll(packagesList);

        ResultActions resultActions = mockMvc.perform(get(PUBLIC_API_ENDPOINT)
                .param("page", "1")
                .param("perPage", "6"));

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()").value(packagesList.size()));
    }

    @Test
    void getPackagesByCategoryId() throws Exception{
        packagesRepository.saveAll(packagesList);

        ResultActions resultActions = mockMvc.perform(get(PUBLIC_API_ENDPOINT.concat("/category/{categoryId}"), package1.getCategory().getId()));

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()").value(packagesList.size()))
                .andExpect(jsonPath("$[0].name").value(package1.getName()))
                .andExpect(jsonPath("$[1].name").value(package2.getName()));
    }

    @Test
    void updatePackage() throws Exception{
        Users adminUser = new Users();
        adminUser.setFirstName("Admin");
        adminUser.setLastName("Test");
        adminUser.setEmail("admin@test.com");
        adminUser.setNumber("99999999");
        adminUser.setPassword("password");
        adminUser.setRole(ADMIN);

        Category category = Category.builder()
                .name("Category 1")
                .build();
        categoryRepository.save(category);

        packagesRepository.save(package1);
        Packages updatePackage1 = packagesRepository.findById(package1.getId()).get();
        updatePackage1.setCategory(category);
        updatePackage1.setName("Updated Package 1");
        updatePackage1.setCountry("Update");
        updatePackage1.setBlurb("update");
        updatePackage1.setDesc("update");
        updatePackage1.setPrice(BigDecimal.valueOf(100));
        updatePackage1.setStart_date(LocalDate.of(2025,10,10));
        updatePackage1.setEnd_date(LocalDate.of(2025,10,20));
        updatePackage1.setNo_of_days(10);
        updatePackage1.setNo_of_nights(10);
        updatePackage1.setImage_url("update.jpg");
        updatePackage1.setImage_1("update1.jpg");
        updatePackage1.setImage_2("update2.jpg");
        updatePackage1.setImage_3("update3.jpg");
        updatePackage1.setImage_4("update4.jpg");

        String requestBody = objectMapper.writeValueAsString(updatePackage1);

        ResultActions resultActions = mockMvc.perform(put(ADMIN_API_ENDPOINT.concat("/{id}"), package1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.category.name").value(category.getName()))
                .andExpect(jsonPath("$.name").value("Updated Package 1"))
                .andExpect(jsonPath("$.country").value("Update"))
                .andExpect(jsonPath("$.blurb").value("update"))
                .andExpect(jsonPath("$.desc").value("update"))
                .andExpect(jsonPath("$.price").value(100))
                .andExpect(jsonPath("$.start_date").value("2025-10-10"))
                .andExpect(jsonPath("$.end_date").value("2025-10-20"))
                .andExpect(jsonPath("$.no_of_days").value(10))
                .andExpect(jsonPath("$.no_of_nights").value(10))
                .andExpect(jsonPath("$.image_url").value("update.jpg"))
                .andExpect(jsonPath("$.image_1").value("update1.jpg"))
                .andExpect(jsonPath("$.image_2").value("update2.jpg"))
                .andExpect(jsonPath("$.image_3").value("update3.jpg"))
                .andExpect(jsonPath("$.image_4").value("update4.jpg"));
    }

    @Test
    void deletePackage() throws Exception {
        packagesRepository.save(package1);
        Packages deletePackage1 = packagesRepository.findById(package1.getId()).get();
        String expectedResponse = String.format("%s has been successfully deleted.", deletePackage1.getName());

        ResultActions resultActions = mockMvc.perform(delete(ADMIN_API_ENDPOINT.concat("/{id}"), package1.getId())
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value(deletePackage1.getName()))
                .andExpect(result -> {
                    String responseContent = result.getResponse().getContentAsString();
                    Packages deletedPackage = objectMapper.readValue(responseContent, Packages.class);
                    String actualResponse = String.format("%s has been successfully deleted.", deletedPackage.getName());
                    assertEquals(expectedResponse, actualResponse);
                });
    }
}
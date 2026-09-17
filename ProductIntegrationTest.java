package com.example.productservice;

import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ProductIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer =
            new MongoDBContainer("mongo:7.0");

    @DynamicPropertySource
    static void configureMongoDB(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri",
                mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void shouldCreateProduct() throws Exception {
        String json = "{\"name\":\"Laptop\",\"description\":\"Dell Laptop\",\"price\":65000}";

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(65000));
    }

    @Test
    void shouldGetAllProducts() throws Exception {
        productRepository.save(new Product("Phone", "Android Phone", 30000));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Phone"));
    }

    @Test
    void shouldGetProductById() throws Exception {
        Product product = productRepository.save(
                new Product("Tablet", "Android Tablet", 25000));

        mockMvc.perform(get("/api/products/" + product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tablet"));
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        Product product = productRepository.save(
                new Product("Laptop", "Old Laptop", 50000));

        String json = "{\"name\":\"Gaming Laptop\",\"description\":\"High performance laptop\",\"price\":90000}";

        mockMvc.perform(put("/api/products/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gaming Laptop"))
                .andExpect(jsonPath("$.price").value(90000));
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        Product product = productRepository.save(
                new Product("Mouse", "Wireless Mouse", 1000));

        mockMvc.perform(delete("/api/products/" + product.getId()))
                .andExpect(status().isNoContent());
    }
}

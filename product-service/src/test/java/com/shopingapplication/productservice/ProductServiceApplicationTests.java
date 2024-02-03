package com.shopingapplication.productservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopingapplication.productservice.controller.ProductController;
import com.shopingapplication.productservice.dto.ProductRequest;
import com.shopingapplication.productservice.dto.ProductResponse;
import com.shopingapplication.productservice.repository.ProductRepository;
import com.shopingapplication.productservice.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer( "mongo:4.4.2");
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper; //This will convert pojo object into json and json into pojo
	@Autowired
	private ProductRepository productRepository;
	@Mock
	private ProductService productService;

	@InjectMocks
	private ProductController productController;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("Spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();

		//Mock
		doNothing().when(productService).createProduct(eq(productRequest));
		//Convert productRequest into String
		String productRequestString = objectMapper.writeValueAsString(productRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
						.contentType(MediaType.APPLICATION_JSON)
						.content(productRequestString) ) //content is the string type
				.andExpect(status().isCreated());

		//Assertion or verify
        Assertions.assertEquals(1, productRepository.findAll().size());

	}

	@Test
	void shouldGetAllProducts() throws Exception {
		List<ProductResponse> productResponses = getProductResponse();

		//mock the services
		when(productService.getAllProducts()).thenReturn(productResponses);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
				.andExpect(status().isOk())
				.andDo(print());

		// Assert the size based on the mocked response
		Assertions.assertEquals(3, productResponses.size());
	}

	private List<ProductResponse> getProductResponse(){
		List<ProductResponse> productResponses = new ArrayList<ProductResponse>();

		productResponses.add(new ProductResponse("1","Iphone 12", "Iphone 12",BigDecimal.valueOf(1200)));
		productResponses.add(new ProductResponse("2","Iphone 11", "Iphone 11",BigDecimal.valueOf(1050)));
		productResponses.add(new ProductResponse("3","Iphone x", "Iphone x",BigDecimal.valueOf(900)));

		return  productResponses;

	}

	@Test
	void shouldGetProductById() throws Exception {
		ProductResponse productResponse = new ProductResponse("1","Iphone 12", "Iphone 12",BigDecimal.valueOf(1200));
		String id = "1";

		when(productService.getProductById(id)).thenReturn(productResponse);
		ResponseEntity<ProductResponse> res = productController.getProductById(id);

		this.mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/product/{id}", id))
				.andExpect(status().isFound())
				.andDo(print());

		Assertions.assertEquals(HttpStatus.FOUND, res.getStatusCode() );
		Assertions.assertEquals(id, Objects.requireNonNull(res.getBody()).getId());
	}

	@Test
	void shouldGetProductByName() throws Exception {
		ProductResponse productResponse = new ProductResponse("1","Iphone 12", "Iphone 12",BigDecimal.valueOf(1200));
		String name = "Iphone 12";

		when(productService.getProductByName(name)).thenReturn(productResponse);
		ResponseEntity<ProductResponse> res = productController.getProductByName(name);

		this.mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/product").param("name","Iphone 12"))
				.andExpect(status().isOk())
				.andDo(print());

		Assertions.assertEquals(HttpStatus.OK, res.getStatusCode() );
		Assertions.assertEquals(name, Objects.requireNonNull(res.getBody()).getName());
	}

	@Test
	void shouldUpdateProduct() throws Exception {
		String id = "1";
		ProductRequest productRequest = getProductRequest();

		//Convert productRequest into String
		String productRequestString = objectMapper.writeValueAsString(productRequest);

		mockMvc.perform(MockMvcRequestBuilders.put("/api/product/{id}",id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(productRequestString) ) //content is the string type
				.andExpect(status().isOk());

	}

	private ProductRequest getProductRequest(){
		return ProductRequest.builder()
				.name("Iphone 12")
				.description("Iphone collection")
				.price(BigDecimal.valueOf(1200))
				.build();
	}

	@Test
	void shouldDeleteProduct() throws Exception {
		ProductResponse productResponse = new ProductResponse("1","Iphone 12", "Iphone 12",BigDecimal.valueOf(1200));
		String id = "1";

		this.mockMvc.perform(MockMvcRequestBuilders.delete("http://localhost:8080/api/product/{id}",id))
				.andExpect(status().isNoContent())
				.andDo(print());
	}

}

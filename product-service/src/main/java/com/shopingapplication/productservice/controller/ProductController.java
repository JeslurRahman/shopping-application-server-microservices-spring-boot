package com.shopingapplication.productservice.controller;

import com.shopingapplication.productservice.dto.ProductRequest;
import com.shopingapplication.productservice.dto.ProductResponse;
import com.shopingapplication.productservice.model.Product;
import com.shopingapplication.productservice.repository.ProductRepository;
import com.shopingapplication.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/product")
public class ProductController {

    //used the annotation of lombok @RequiredArgsConstructor to inject the classes automatically instead of creating manually
    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest){
        try{
            productService.createProduct(productRequest);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ProductResponse>> getAllProducts(){
        try{
            List<ProductResponse> productResponse = productService.getAllProducts();
            return new ResponseEntity<List<ProductResponse>>(productResponse, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String id){
        try{
            ProductResponse productResponse = productService.getProductById(id);
            return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.FOUND);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/name")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProductResponse> getProductByName(@RequestParam(value="name") String productName ) {
        try {
            ProductResponse productResponse = productService.getProductByName(productName);
            return new ResponseEntity<>(productResponse, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable(value="id") String id, @RequestBody ProductRequest productRequest){
        try{
            productService.updateProduct(id,productRequest);
            ProductResponse updatedProduct = productService.getProductById(id);
            return new ResponseEntity<ProductResponse>(updatedProduct, HttpStatus.OK);
        }catch (NoSuchElementException e) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable(value="id") String id){
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (NoSuchElementException e) {
            // Handle the case where the product with the given id is not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}


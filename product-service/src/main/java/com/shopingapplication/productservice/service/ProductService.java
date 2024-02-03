package com.shopingapplication.productservice.service;

import com.shopingapplication.productservice.dto.ProductRequest;
import com.shopingapplication.productservice.dto.ProductResponse;
import com.shopingapplication.productservice.model.Product;
import com.shopingapplication.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j //Logs from lombok
public class ProductService {

    private final ProductRepository productRepository;

    //After declaration of ProductRepository, need to initialize the ProductRepository
    //Instead of initializing manually like below for all the classes, we can add the annotation from lombok "@RequiredArgsConstructor"
    /*
    public ProductService(ProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }
    */
    public void createProduct(ProductRequest productRequest){

        //Mapping the productRequest DTO to the Product model
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
        log.info("Product {} is saved", product.getId()); // @Slf4j - Logs from lombok
    }

    public List<ProductResponse> getAllProducts(){
       List<Product> products = productRepository.findAll();

        //Mapping the ProductResponse DTO to the Product model
        return products.stream().map(this::mapToProductResponse).toList();
    }

    public ProductResponse getProductById(String id){
        List<Product> products = productRepository.findAll();
        ProductResponse productResponse  = null;

        for(Product product: products)
        {
            if(product.getId().equals(id) )
            {
                productResponse = mapToProductResponse(product);
                break;
            }
        }
        return productResponse;
    }

    public ProductResponse getProductByName(String productName){
        List<Product> products = productRepository.findAll();
        ProductResponse productResponse  = null;

        for(Product product: products)
        {
            if(product.getName().equalsIgnoreCase(productName) )
            {
                productResponse = mapToProductResponse(product);
                break;
            }
        }
        return productResponse;
    }

    private ProductResponse mapToProductResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

    public void updateProduct(String id, ProductRequest productRequest){
        // Retrieve the existing product from the repository
        Optional<Product> optionalProduct  = productRepository.findById(id);
        if (optionalProduct .isPresent()) {
            Product existingProduct = optionalProduct.get();

            // Update the existing product with the new data
            existingProduct.setName(productRequest.getName());
            existingProduct.setDescription(productRequest.getDescription());
            existingProduct.setPrice(productRequest.getPrice());

            // Save the updated product back to the repository
            productRepository.save(existingProduct);
            log.info("Product {} is updated", existingProduct.getId());
        }

    }

    public void deleteProduct(String id){
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product productToDelete = optionalProduct.get();
            productRepository.delete(productToDelete);
            log.info("Product {} is deleted", productToDelete.getId());
        }
    }
}

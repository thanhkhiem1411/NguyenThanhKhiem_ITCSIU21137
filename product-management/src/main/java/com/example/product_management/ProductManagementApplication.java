package com.example.product_management;

import com.example.product_management.repository.ProductRepository;
import com.example.product_management.entity.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class ProductManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductManagementApplication.class, args);
    }

    // Temporary test - remove after verification
    @Bean
    CommandLineRunner test(ProductRepository repository) {
        return args -> {
            System.out.println("=== Testing Repository ===");

            long count = repository.count();
            System.out.println("Total products: " + count);

            List<Product> products = repository.findAll();
            products.forEach(System.out::println);

            List<Product> electronics = repository.findByCategory("Electronics");
            System.out.println("\nElectronics: " + electronics.size());

            System.out.println("=== Test Complete ===");
        };
    }
}

package com.shopease.config;

import com.shopease.entity.*;
import com.shopease.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeData(
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // Initialize only if data doesn't exist
            if (categoryRepository.count() > 0) {
                System.out.println("Database already initialized. Skipping data initialization.");
                return;
            }

            System.out.println("Initializing database with sample data...");

            // Create Categories
            Category necklaces = categoryRepository.save(Category.builder()
                    .name("Necklaces")
                    .description("Elegant and stylish necklaces for all occasions")
                    .build());

            Category bracelets = categoryRepository.save(Category.builder()
                    .name("Bracelets")
                    .description("Beautiful bracelets made with premium materials")
                    .build());

            Category earrings = categoryRepository.save(Category.builder()
                    .name("Earrings")
                    .description("Stunning earrings to complement your style")
                    .build());

            Category rings = categoryRepository.save(Category.builder()
                    .name("Rings")
                    .description("Exquisite rings perfect for special moments")
                    .build());

            Category anklets = categoryRepository.save(Category.builder()
                    .name("Anklets")
                    .description("Delicate anklets for a fashionable look")
                    .build());

            // Create Products - Necklaces
            productRepository.saveAll(List.of(
                    Product.builder()
                            .name("Gold Necklace")
                            .description("Elegant 14k gold chain necklace with pendant")
                            .tagline("Timeless elegance")
                            .price(new BigDecimal("299.99"))
                            .originalPrice(new BigDecimal("399.99"))
                            .image("https://via.placeholder.com/300?text=Gold+Necklace")
                            .badge("Best Seller")
                            .category(necklaces)
                            .stock(15)
                            .build(),
                    Product.builder()
                            .name("Silver Chain")
                            .description("Classic sterling silver chain for everyday wear")
                            .tagline("Pure elegance")
                            .price(new BigDecimal("149.99"))
                            .originalPrice(new BigDecimal("199.99"))
                            .image("https://via.placeholder.com/300?text=Silver+Chain")
                            .badge("Popular")
                            .category(necklaces)
                            .stock(25)
                            .build(),
                    Product.builder()
                            .name("Diamond Pendant")
                            .description("Stunning diamond pendant necklace")
                            .tagline("Premium quality")
                            .price(new BigDecimal("799.99"))
                            .originalPrice(new BigDecimal("999.99"))
                            .image("https://via.placeholder.com/300?text=Diamond+Pendant")
                            .badge("Premium")
                            .category(necklaces)
                            .stock(8)
                            .build(),
                    Product.builder()
                            .name("Pearl Necklace")
                            .description("Beautiful freshwater pearl necklace")
                            .tagline("Sophisticated style")
                            .price(new BigDecimal("189.99"))
                            .originalPrice(new BigDecimal("249.99"))
                            .image("https://via.placeholder.com/300?text=Pearl+Necklace")
                            .category(necklaces)
                            .stock(20)
                            .build()
            ));

            // Create Products - Bracelets
            productRepository.saveAll(List.of(
                    Product.builder()
                            .name("Gold Bracelet")
                            .description("Elegant 14k gold bracelet with intricate design")
                            .tagline("Luxury wear")
                            .price(new BigDecimal("349.99"))
                            .originalPrice(new BigDecimal("449.99"))
                            .image("https://via.placeholder.com/300?text=Gold+Bracelet")
                            .badge("Best Seller")
                            .category(bracelets)
                            .stock(12)
                            .build(),
                    Product.builder()
                            .name("Silver Bracelet")
                            .description("Classic sterling silver bracelet")
                            .tagline("Timeless beauty")
                            .price(new BigDecimal("129.99"))
                            .originalPrice(new BigDecimal("179.99"))
                            .image("https://via.placeholder.com/300?text=Silver+Bracelet")
                            .badge("Popular")
                            .category(bracelets)
                            .stock(30)
                            .build(),
                    Product.builder()
                            .name("Diamond Bracelet")
                            .description("Tennis bracelet with brilliant diamonds")
                            .tagline("Luxurious")
                            .price(new BigDecimal("1299.99"))
                            .originalPrice(new BigDecimal("1599.99"))
                            .image("https://via.placeholder.com/300?text=Diamond+Bracelet")
                            .badge("Premium")
                            .category(bracelets)
                            .stock(5)
                            .build()
            ));

            // Create Products - Earrings
            productRepository.saveAll(List.of(
                    Product.builder()
                            .name("Pearl Earrings")
                            .description("Freshwater pearl stud earrings")
                            .tagline("Elegant simplicity")
                            .price(new BigDecimal("199.99"))
                            .originalPrice(new BigDecimal("249.99"))
                            .image("https://via.placeholder.com/300?text=Pearl+Earrings")
                            .category(earrings)
                            .stock(20)
                            .build(),
                    Product.builder()
                            .name("Diamond Earrings")
                            .description("1 carat diamond stud earrings")
                            .tagline("Brilliant sparkle")
                            .price(new BigDecimal("1499.99"))
                            .originalPrice(new BigDecimal("1999.99"))
                            .image("https://via.placeholder.com/300?text=Diamond+Earrings")
                            .badge("Premium")
                            .category(earrings)
                            .stock(6)
                            .build(),
                    Product.builder()
                            .name("Gold Hoops")
                            .description("Classic 14k gold hoop earrings")
                            .tagline("Timeless style")
                            .price(new BigDecimal("249.99"))
                            .originalPrice(new BigDecimal("349.99"))
                            .image("https://via.placeholder.com/300?text=Gold+Hoops")
                            .badge("Popular")
                            .category(earrings)
                            .stock(18)
                            .build()
            ));

            // Create Products - Rings
            productRepository.saveAll(List.of(
                    Product.builder()
                            .name("Diamond Ring")
                            .description("1 carat diamond engagement ring")
                            .tagline("Forever love")
                            .price(new BigDecimal("4999.99"))
                            .originalPrice(new BigDecimal("6999.99"))
                            .image("https://via.placeholder.com/300?text=Diamond+Ring")
                            .badge("Premium")
                            .category(rings)
                            .stock(5)
                            .build(),
                    Product.builder()
                            .name("Gold Ring")
                            .description("Beautiful 14k gold band ring")
                            .tagline("Classic design")
                            .price(new BigDecimal("399.99"))
                            .originalPrice(new BigDecimal("599.99"))
                            .image("https://via.placeholder.com/300?text=Gold+Ring")
                            .badge("Best Seller")
                            .category(rings)
                            .stock(22)
                            .build(),
                    Product.builder()
                            .name("Silver Ring")
                            .description("Elegant sterling silver ring")
                            .tagline("Simple elegance")
                            .price(new BigDecimal("149.99"))
                            .originalPrice(new BigDecimal("199.99"))
                            .image("https://via.placeholder.com/300?text=Silver+Ring")
                            .category(rings)
                            .stock(28)
                            .build(),
                    Product.builder()
                            .name("Sapphire Ring")
                            .description("Stunning sapphire ring with diamond accents")
                            .tagline("Luxurious")
                            .price(new BigDecimal("799.99"))
                            .originalPrice(new BigDecimal("1099.99"))
                            .image("https://via.placeholder.com/300?text=Sapphire+Ring")
                            .badge("Premium")
                            .category(rings)
                            .stock(7)
                            .build()
            ));

            // Create Products - Anklets
            productRepository.saveAll(List.of(
                    Product.builder()
                            .name("Gold Anklet")
                            .description("Delicate 14k gold anklet")
                            .tagline("Summer style")
                            .price(new BigDecimal("199.99"))
                            .originalPrice(new BigDecimal("299.99"))
                            .image("https://via.placeholder.com/300?text=Gold+Anklet")
                            .badge("Popular")
                            .category(anklets)
                            .stock(16)
                            .build(),
                    Product.builder()
                            .name("Silver Anklet")
                            .description("Beautiful sterling silver anklet")
                            .tagline("Perfect for summer")
                            .price(new BigDecimal("99.99"))
                            .originalPrice(new BigDecimal("149.99"))
                            .image("https://via.placeholder.com/300?text=Silver+Anklet")
                            .category(anklets)
                            .stock(32)
                            .build()
            ));

            // Create Sample Users
            userRepository.saveAll(List.of(
                    User.builder()
                            .email("admin@shopease.com")
                            .password(passwordEncoder.encode("admin123"))
                            .fullName("Admin User")
                            .phone("1234567890")
                            .address("123 Admin St, City")
                            .enabled(true)
                            .role("ADMIN")
                            .build(),
                    User.builder()
                            .email("customer@shopease.com")
                            .password(passwordEncoder.encode("password123"))
                            .fullName("John Customer")
                            .phone("9876543210")
                            .address("456 Customer Ave, Town")
                            .enabled(true)
                            .role("USER")
                            .build()
            ));

            System.out.println("Database initialized successfully!");
            System.out.println("Test credentials:");
            System.out.println("  Admin: admin@shopease.com / admin123");
            System.out.println("  Customer: customer@shopease.com / password123");
        };
    }
}

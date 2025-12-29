package org.example.productcatelogservice.repositories;

import org.example.productcatelogservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product save(Product product);

    Optional<Product> findById(Long aLong);

    List<Product> findAll();

    List<Product> findProductByPriceBetween(Double minPrice, Double maxPrice);

    List<Product> findProductByIsPrime(Boolean isPrime);

    List<Product> findProductByOrderByPriceAsc();

    @Query("select p.description from Product p where p.id = :id")
    String findProductDescriptionById(Long id);
}

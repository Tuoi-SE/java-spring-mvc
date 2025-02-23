package vn.hoidanit.laptopshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hoidanit.laptopshop.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
    List<Product> findAll();

    Product save(Product tuoiproduct);

    // Product findById(long id);

    void deleteById(long id);
}

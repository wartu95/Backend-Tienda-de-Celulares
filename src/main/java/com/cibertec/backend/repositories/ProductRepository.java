package com.cibertec.backend.repositories;

import com.cibertec.backend.entites.Product;
import com.cibertec.backend.entites.StateProduct;
import com.cibertec.backend.utils.enums.EStateProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> {

    @Query("SELECT p FROM producto p WHERE p.stateProduct.state = :state")
    List<Product> findByStateProductState(@Param("state") EStateProduct state);


    @Query("SELECT p FROM producto p WHERE p.imei = :imei AND (p.stateProduct.state = 'VENDIDO' OR p.stateProduct.state = 'REEMPLAZO')")
    Optional<Product> findProductsByImeiAndState(@Param("imei") String imei);

    List<Product> findByBrandAndModelAndStateProduct(String brand, String model, StateProduct stateProduct);

    @Query("SELECT p FROM producto p WHERE p.stateProduct.state = 'NUEVO' AND p.price <= (SELECT pr.price * 1.10 FROM producto pr WHERE pr.imei = :imei)")
    List<Product> findNewProductsUnder110PercentOfPriceByImeiAndStateProductNew(@Param("imei") String imei);



}

package com.cibertec.backend.repositories;

import com.cibertec.backend.entites.StateProduct;
import com.cibertec.backend.utils.enums.EStateProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StateProductRepository extends JpaRepository<StateProduct, String> {


    Optional<StateProduct> findByState(EStateProduct state);
}
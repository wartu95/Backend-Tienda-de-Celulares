package com.cibertec.backend.repositories;

import com.cibertec.backend.entites.Customer;
import com.cibertec.backend.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends BaseRepository<Customer,Long> {


    Optional<Customer> findByNumDoc(String numDoc);


}

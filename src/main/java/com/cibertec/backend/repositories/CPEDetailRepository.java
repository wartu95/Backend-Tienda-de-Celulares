package com.cibertec.backend.repositories;

import com.cibertec.backend.entites.CPEDetail;
import com.cibertec.backend.entites.CPEDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CPEDetailRepository extends JpaRepository<CPEDetail, CPEDetailId> {

    Optional<CPEDetail> findByProductImei(@Param("imei") String imei);
}
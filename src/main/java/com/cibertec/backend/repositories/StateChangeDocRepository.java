package com.cibertec.backend.repositories;

import com.cibertec.backend.entites.StateChangeDoc;

import com.cibertec.backend.utils.enums.EStateChangeDoc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StateChangeDocRepository extends JpaRepository<StateChangeDoc, String> {

    Optional<StateChangeDoc> findByState(EStateChangeDoc state);
}
package com.cibertec.backend.repositories;

import com.cibertec.backend.entites.ChangeDoc;
import com.cibertec.backend.repositories.base.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChangeDocRepository extends BaseRepository<ChangeDoc,Long> {

    Optional<ChangeDoc> findByProductImei(@Param("imei") String imei);
    Optional<ChangeDoc> findByTicketProductImei(@Param("imei") String imei);

}

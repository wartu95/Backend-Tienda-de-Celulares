package com.cibertec.backend.repositories;

import com.cibertec.backend.entites.CPE;
import com.cibertec.backend.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Repository
public interface CPERepository extends BaseRepository<CPE,Long> {

    Optional<CPE> findByTypeCpeAndSerieAndCorrelative(String typeCpe,String serie,String correlative);

}

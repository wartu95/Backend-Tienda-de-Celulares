package com.cibertec.backend.repositories;

import com.cibertec.backend.entites.Ticket;
import com.cibertec.backend.repositories.base.BaseRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends BaseRepository<Ticket,Long> {

    Optional<Ticket> findByProductImei(String imei);
}

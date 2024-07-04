package com.cibertec.backend.repositories.base;

import com.cibertec.backend.entites.base.BaseEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;


@NoRepositoryBean
public interface BaseRepository<E extends BaseEntityId,ID extends Serializable> extends JpaRepository<E,ID> {


}

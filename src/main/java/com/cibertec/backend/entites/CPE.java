package com.cibertec.backend.entites;


import com.cibertec.backend.entites.base.BaseEntityId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "cpe")
public class CPE extends BaseEntityId {

    @Column(columnDefinition = "char(2)")
    private String typeCpe;

    @Column(columnDefinition = "char(4)")
    private String serie;

    @Column(columnDefinition = "char(8)")
    private String correlative;

    private Timestamp date;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Customer customer;

    @OneToMany(mappedBy = "cpe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CPEDetail> cpeDetails;
}

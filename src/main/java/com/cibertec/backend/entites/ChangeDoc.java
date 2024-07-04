package com.cibertec.backend.entites;

import com.cibertec.backend.entites.base.BaseEntityId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name="documento_cambio")
@EqualsAndHashCode(callSuper = true)
public class ChangeDoc extends BaseEntityId {

    private Timestamp date;

    @ManyToOne
    @JoinColumn(name = "state_change_doc_id")
    private StateChangeDoc stateChangeDoc;


    @OneToOne
    @JoinColumn(name = "id_ticket")
    private Ticket ticket;

    private Timestamp dateLastState;

    @OneToOne
    @JoinColumn(name = "imei_entregado")
    private Product product;

    private String comment;

}

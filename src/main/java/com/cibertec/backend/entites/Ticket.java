package com.cibertec.backend.entites;

import com.cibertec.backend.entites.base.BaseEntityId;
import com.cibertec.backend.utils.enums.ETicketState;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(callSuper = true)
public class Ticket extends BaseEntityId {

    private Timestamp date;

    private String comments;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Customer customers;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne(mappedBy = "ticket")
    private ChangeDoc changeDoc;

    @Enumerated(EnumType.STRING)
    private ETicketState eTicketState;
}

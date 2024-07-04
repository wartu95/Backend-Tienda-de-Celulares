package com.cibertec.backend.entites;

import com.cibertec.backend.entites.base.BaseEntityId;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;


@Data
@NoArgsConstructor//java: constructor StateProduct() is already defined in class com.cibertec.backend.entites.StateProduct
@AllArgsConstructor
@Builder
@Entity(name="cliente")
@EqualsAndHashCode(callSuper = true)
public class Customer extends BaseEntityId {

    @Column(length = 11,unique = true)
    private String numDoc;

    private String name;

    private String phone;

    private String mail;

    @OneToMany(mappedBy = "customers", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<CPE> cpes;



}

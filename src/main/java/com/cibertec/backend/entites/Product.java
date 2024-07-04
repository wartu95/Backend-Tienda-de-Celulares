package com.cibertec.backend.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "producto")
public class Product {

    @Id
    @Column(columnDefinition = "char(15)")
    private String imei;

    private String model;

    private String brand;

    private Double price;

    @ManyToOne
    @JoinColumn(name = "state_product_id")
    @JsonIgnore
    private StateProduct stateProduct;

    @OneToOne(mappedBy = "product")
    @JsonIgnore
    private Ticket ticket;

    @JsonIgnore
    @OneToOne(mappedBy = "product")
    private ChangeDoc changeDoc;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonIgnore
    private List<CPEDetail> cpeDetails;
}

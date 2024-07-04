package com.cibertec.backend.entites;

import com.cibertec.backend.utils.enums.EStateProduct;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor//java: constructor StateProduct() is already defined in class com.cibertec.backend.entites.StateProduct
@AllArgsConstructor
@Builder
@Entity(name="estados_producto")
public class StateProduct {

    @Id
    @Column(columnDefinition = "char(2)")
    private String id;

    @Enumerated(EnumType.STRING)
    private EStateProduct state;


    @OneToMany(mappedBy = "stateProduct", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Product> products;


}

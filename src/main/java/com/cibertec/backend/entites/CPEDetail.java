package com.cibertec.backend.entites;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "cpe_detail")
public class CPEDetail {


    @EmbeddedId
    private CPEDetailId id;

    //@JoinColumn(name = "imei")
    @ManyToOne
    @MapsId("productImei")//define que es FK + PK compuesta
    @JoinColumn(name = "imei", referencedColumnName = "imei", insertable = false, updatable = false)
    private Product product;


   @JoinColumn(name = "cpe_id", referencedColumnName = "id", insertable = false, updatable = false)
   @ManyToOne
   @MapsId("cpeId")//define que es FK + PK compuesta
   //@JoinColumn(name = "cpe_id")
    private CPE cpe;

    private Double unitPrice;

}

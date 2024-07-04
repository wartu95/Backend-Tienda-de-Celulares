package com.cibertec.backend.entites;



import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;


@Embeddable
public class CPEDetailId implements Serializable {

    private String productImei;
    private Long cpeId;

    public CPEDetailId() {}

    public CPEDetailId(String productImei, Long cpeId) {
        this.productImei = productImei;
        this.cpeId = cpeId;
    }

    // Getters y Setters
    public String getProductImei() {
        return productImei;
    }

    public void setProductImei(String productImei) {
        this.productImei = productImei;
    }

    public Long getCpeId() {
        return cpeId;
    }

    public void setCpeId(Long cpeId) {
        this.cpeId = cpeId;
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CPEDetailId that = (CPEDetailId) o;
        return Objects.equals(productImei, that.productImei) &&
                Objects.equals(cpeId, that.cpeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productImei, cpeId);
    }
}


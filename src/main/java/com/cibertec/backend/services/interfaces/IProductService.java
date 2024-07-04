package com.cibertec.backend.services.interfaces;

import com.cibertec.backend.entites.Product;

import java.util.List;

public interface IProductService {

    public List<Product> buscarProductoReemplazoMismaModeloMismaMarca(String imei);

    public List<Product> buscarProductoReemplazoDiezPorCiento(String imei);
    public List<Product> findAll();
}

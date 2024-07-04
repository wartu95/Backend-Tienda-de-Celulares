package com.cibertec.backend.services;

import com.cibertec.backend.entites.Product;
import com.cibertec.backend.repositories.ProductRepository;
import com.cibertec.backend.repositories.StateProductRepository;
import com.cibertec.backend.services.interfaces.IProductService;
import com.cibertec.backend.utils.enums.EStateProduct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {



    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StateProductRepository stateProductRepository;
    @Override
    @Transactional
    public List<Product> buscarProductoReemplazoMismaModeloMismaMarca(String imei) {

        try {

            Product product= productRepository.findById(imei).get();
            //se requiere misma marca, modelo y estado NUEVO
            List<Product> products= productRepository.findByBrandAndModelAndStateProduct(product.getBrand(),product.getModel(),stateProductRepository.findByState(EStateProduct.NUEVO).get());
            return products;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ArrayList<Product>();
        }

    }

    @Override
    @Transactional
    public List<Product> buscarProductoReemplazoDiezPorCiento(String imei){

        try {
            Product product= productRepository.findById(imei).get();

            //se requiere misma marca, modelo y estado NUEVO
            List<Product> products= productRepository.findNewProductsUnder110PercentOfPriceByImeiAndStateProductNew(product.getImei());
            return products;
        }catch (Exception e){
            return new ArrayList<Product>();
        }


    }

    @Override
    public List<Product> findAll() {
        List<Product> products = productRepository.findAll();
        return products;
    }


}

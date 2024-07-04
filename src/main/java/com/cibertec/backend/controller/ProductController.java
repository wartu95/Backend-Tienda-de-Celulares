package com.cibertec.backend.controller;

import com.cibertec.backend.repositories.ProductRepository;
import com.cibertec.backend.services.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping(value = "/api/v1/producto")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductServiceImpl productService;


    @PreAuthorize("hasRole('SALE')")
    @GetMapping("/buscar-reemplazo")
    public ResponseEntity<?> buscarProductoReemplazoMismaModeloMismaMarca(@RequestParam String imei){


        try {
            return new ResponseEntity<>(productService.buscarProductoReemplazoMismaModeloMismaMarca(imei),HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('SALE')")
    @GetMapping("/buscar-reemplazo-similar")
    public ResponseEntity<?> buscarProductoReemplazoDiezPorCiento(@RequestParam String imei){

        try {
            return new ResponseEntity<>(productService.buscarProductoReemplazoDiezPorCiento(imei),HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }


    @PreAuthorize("hasRole('SALE')")
    @GetMapping("/lista")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findAll());
    }

}

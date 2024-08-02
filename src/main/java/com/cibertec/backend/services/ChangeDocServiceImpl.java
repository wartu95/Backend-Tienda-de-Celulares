package com.cibertec.backend.services;

import com.cibertec.backend.entites.*;
import com.cibertec.backend.repositories.*;
import com.cibertec.backend.services.interfaces.IChangeDocService;
import com.cibertec.backend.utils.dto.ChangeDocRDTO;
import com.cibertec.backend.utils.enums.EStateChangeDoc;
import com.cibertec.backend.utils.enums.EStateProduct;
import com.cibertec.backend.utils.enums.ETicketState;
import jakarta.transaction.Transactional;
import org.apache.el.lang.ELArithmetic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class ChangeDocServiceImpl implements IChangeDocService {


    @Autowired
    private ChangeDocRepository changeDocRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private StateChangeDocRepository stateChangeDocRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StateProductRepository stateProductRepository;

    @Autowired
    private CPEDetailRepository cpeDetailRepository;

    @Transactional
    public int registraDocumentoCambio(String imei,String comment){

        //consultar si IMEI existe
        // si existe, verificar si  esta vendido o reemplazado,


        Optional<Product>  product= productRepository.findById(imei);

        if(product.isPresent()){

            Product p2=product.get();


            if(!p2.getStateProduct().getState().equals(EStateProduct.VENDIDO) && !p2.getStateProduct().getState().equals(EStateProduct.REEMPLAZO)){
                return 3;//IMEI EN PROCESO DE CAMBIO O EVALUACION
            }
            //si cumple, consultar si está asociado a un comprobante o documento de cambio
            Optional<CPEDetail> cpeDetailIds = cpeDetailRepository.findByProductImei(imei);
            Optional<ChangeDoc> changeDocIds = changeDocRepository.findByProductImei(imei);

            if (cpeDetailIds.isEmpty() && changeDocIds.isEmpty()) {

                return 0;//"IMEI NO EXISTE"
            }else{

                if(!changeDocIds.isEmpty()){    //si existe IMEI EN DOCUMENTO DE CAMBIO

                    LocalDate dateChangeDoc = changeDocIds.get().getDateLastState().toLocalDateTime().toLocalDate();//fecha último estado de documento de cambio
                    LocalDate now = LocalDateTime.now().toLocalDate();
                    LocalDate oneYearAgo = now.minus(1, ChronoUnit.YEARS);

                    if(dateChangeDoc.isBefore(oneYearAgo)){

                         ticketRepository.save(
                                Ticket.builder()
                                        .date(Timestamp.valueOf(LocalDateTime.now()))
                                        .customers(changeDocIds.get().getTicket().getCustomers())
                                        .product(changeDocIds.get().getProduct())
                                        .comments(comment)
                                        .eTicketState(ETicketState.RECHAZADO)
                                        .build()
                        );
                        return 1;//IMEI FUERA DE GARANTÍA

                    }else{

                        Ticket ticket= ticketRepository.save(
                                Ticket.builder()
                                        .date(Timestamp.valueOf(LocalDateTime.now()))
                                        .customers(changeDocIds.get().getTicket().getCustomers())
                                        .product(changeDocIds.get().getProduct())
                                        .comments(comment)
                                        .eTicketState(ETicketState.APROBADO)
                                        .build()
                        );

                        ticket= ticketRepository.findById(ticket.getId()).get();
                        StateChangeDoc stateChangeDoc= stateChangeDocRepository.findByState(EStateChangeDoc.GENERADO).get();
                        changeDocRepository.save(
                                ChangeDoc.builder()
                                        .date(Timestamp.valueOf(LocalDateTime.now()))
                                        .stateChangeDoc(stateChangeDoc)
                                        .ticket(ticket)
                                        .dateLastState(Timestamp.valueOf(LocalDateTime.now()))
                                        .build()
                        );

                        p2.setStateProduct(stateProductRepository.findByState(EStateProduct.EVALUACION).get());
                        productRepository.save(p2);
                        return 2;//IMEI DENTRO DE GARANTÍA
                    }

                }else{

                    LocalDate dateCpe=cpeDetailIds.get().getCpe().getDate().toLocalDateTime().toLocalDate();

                    LocalDate now = LocalDateTime.now().toLocalDate();
                    LocalDate oneYearAgo = now.minus(1, ChronoUnit.YEARS);

                    if(dateCpe.isBefore(oneYearAgo)){

                        ticketRepository.save(
                                Ticket.builder()
                                        .date(Timestamp.valueOf(LocalDateTime.now()))
                                        .customers(cpeDetailIds.get().getCpe().getCustomer())
                                        .product(cpeDetailIds.get().getProduct())
                                        .comments(comment)
                                        .eTicketState(ETicketState.RECHAZADO)
                                        .build()
                        );
                        return 1;//IMEI FUERA DE GARANTÍA
                    }else{

                        Ticket ticket= ticketRepository.save(
                                Ticket.builder()
                                        .date(Timestamp.valueOf(LocalDateTime.now()))
                                        .customers(cpeDetailIds.get().getCpe().getCustomer())
                                        .product(cpeDetailIds.get().getProduct())
                                        .comments(comment)
                                        .eTicketState(ETicketState.APROBADO)
                                        .build()
                        );//IMEI DENTRO DE GARANTÍA

                        ticket= ticketRepository.findById(ticket.getId()).get();
                        StateChangeDoc stateChangeDoc= stateChangeDocRepository.findByState(EStateChangeDoc.GENERADO).get();
                        changeDocRepository.save(
                                ChangeDoc.builder()
                                        .date(Timestamp.valueOf(LocalDateTime.now()))
                                        .stateChangeDoc(stateChangeDoc)
                                        .ticket(ticket)
                                        .dateLastState(Timestamp.valueOf(LocalDateTime.now()))
                                        .build()
                        );


                        p2.setStateProduct(stateProductRepository.findByState(EStateProduct.EVALUACION).get());
                        productRepository.save(p2);
                        return 2;//IMEI DENTRO DE GARANTÍA
                    }

                }

            }

        }else{

            return 0;//"IMEI NO EXISTE";

        }

    }

    @Override
    @Transactional
    public int actualizarEstadoTecnicoRevision(String imei) {

        try {
            //buscar por imei, esto debe retornar la solicitud de cambio
            ChangeDoc changeDoc= ticketRepository.findByProductImei(imei).get().getChangeDoc();

            if(changeDoc.getStateChangeDoc().getState().equals(EStateChangeDoc.GENERADO)){
                changeDoc.setStateChangeDoc(stateChangeDocRepository.findByState(EStateChangeDoc.REVISION).get());
                changeDoc.setDateLastState(Timestamp.valueOf(LocalDateTime.now()));
                changeDocRepository.save(changeDoc);
                return 0;
            }else{
                return 1;//solo pueden modificar estados generados.
            }


            //estado EVALUACION se generará en el registro del documento de cambio.
            //Product product=productRepository.findById(imei).get();
            //product.setStateProduct(stateProductRepository.findByState(EStateProduct.EVALUACION).get());
            //productRepository.save(product);

        }catch (Exception e){
            return -1;
        }



    }


    @Override
    @Transactional
    public int actualizarEstadoTecnicoAprobado(String imei) {

        try {


            //buscar por imei, esto debe retornar la solicitud de cambio
            ChangeDoc changeDoc= ticketRepository.findByProductImei(imei).get().getChangeDoc();

            if(changeDoc.getStateChangeDoc().getState().equals(EStateChangeDoc.REVISION)){
                changeDoc.setStateChangeDoc(stateChangeDocRepository.findByState(EStateChangeDoc.APROBADO).get());
                changeDoc.setDateLastState(Timestamp.valueOf(LocalDateTime.now()));
                changeDocRepository.save(changeDoc);

                //actualizar estado del IMEI averiado

                Product product=productRepository.findById(imei).get();
                product.setStateProduct(stateProductRepository.findByState(EStateProduct.DEFECTUOSO).get());
                productRepository.save(product);

                return 0;
            }else{
                return 1;
            }

        }catch (Exception e){
            return -1;
        }

    }

    @Override
    @Transactional
    public int registrarCambio(String oldImei, String newImei) {

        try {

            Product oldProduct= productRepository.findById(oldImei).get();
            //se requiere misma marca, modelo y estado NUEVO
            List<Product> products1= productRepository.findByBrandAndModelAndStateProduct(oldProduct.getBrand(),oldProduct.getModel(),stateProductRepository.findByState(EStateProduct.NUEVO).get());
            List<Product> products2= productRepository.findNewProductsUnder110PercentOfPriceByImeiAndStateProductNew(oldProduct.getImei());

            if(products1.isEmpty() && products2.isEmpty()){
                return 1;
            }else{

                ChangeDoc changeDoc= ticketRepository.findByProductImei(oldImei).get().getChangeDoc();

                if(changeDoc.getStateChangeDoc().getState().equals(EStateChangeDoc.APROBADO) || changeDoc.getStateChangeDoc().getState().equals(EStateChangeDoc.ESPERA) ){
                    changeDoc.setProduct(productRepository.findById(newImei).get());//imei entregado
                    changeDoc.setDateLastState(Timestamp.valueOf(LocalDateTime.now()));
                    changeDoc.setStateChangeDoc(stateChangeDocRepository.findByState(EStateChangeDoc.EJECUTADO).get());
                    changeDoc.setComment(" -o" +
                            "");
                    changeDocRepository.save(changeDoc);

                    Product product=productRepository.findById(newImei).get();
                    product.setStateProduct(stateProductRepository.findByState(EStateProduct.REEMPLAZO).get());
                    productRepository.save(product);
                    return 0;
                }else {
                    return 2;
                }

            }

        }catch (Exception e){
            return -1;
        }
    }

    @Transactional
    public int actualizarEstadoTecnicoRechazado(String imei, String comment){

        try {
            //buscar por imei, esto debe retornar la solicitud de cambio
            ChangeDoc changeDoc= ticketRepository.findByProductImei(imei).get().getChangeDoc();

            if(changeDoc.getStateChangeDoc().getState().equals(EStateChangeDoc.REVISION)){

                changeDoc.setStateChangeDoc(stateChangeDocRepository.findByState(EStateChangeDoc.RECHAZADO).get());
                changeDoc.setDateLastState(Timestamp.valueOf(LocalDateTime.now()));
                changeDoc.setComment(comment);
                changeDocRepository.save(changeDoc);

                //ACUTALIZAR
                Product product=productRepository.findById(imei).get();
                product.setStateProduct(stateProductRepository.findByState(EStateProduct.RECHAZADO).get());
                productRepository.save(product);


                return 0;
            }else {
                return 1;
            }



        }catch (Exception e){
            return -1;
        }

    }

    @Override
    @Transactional
    public int actualizarEstadoClienteEsperaLlegadaNuevoEquipo(String imei, String comment) {

        try {
            //buscar por imei, esto debe retornar la solicitud de cambio
            ChangeDoc changeDoc= ticketRepository.findByProductImei(imei).get().getChangeDoc();
            if(changeDoc.getStateChangeDoc().getState().equals(EStateChangeDoc.APROBADO)){
                changeDoc.setStateChangeDoc(stateChangeDocRepository.findByState(EStateChangeDoc.ESPERA).get());
                changeDoc.setDateLastState(Timestamp.valueOf(LocalDateTime.now()));
                changeDoc.setComment(comment);
                changeDocRepository.save(changeDoc);
                return 0;
            }else {
                return 1;
            }

        }catch (Exception e){
            return -1;
        }


    }
    @Override
    @Transactional
    public ChangeDocRDTO obtenerDocCambioDeTicketPorImei(String imei){

        Optional<ChangeDoc> changeDoc= changeDocRepository.findByTicketProductImei(imei);

        if(changeDoc.isPresent()){

            ChangeDoc changeDoc1=changeDoc.get();

            return ChangeDocRDTO.builder()
                    .id(Math.toIntExact(changeDoc1.getId()))
                    .state(String.valueOf(changeDoc1.getStateChangeDoc().getState()))
                    .ticket(changeDoc1.getTicket().getId())
                    .createdDate(changeDoc1.getDate())
                    .updateDate(changeDoc1.getDateLastState())
                    .ticketDate(changeDoc1.getTicket().getDate())
                    .comment(changeDoc1.getComment())
                    .imeiInRevision(changeDoc1.getTicket().getProduct().getImei()).build();
        }else {
            return null;
 }


}

}

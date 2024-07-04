package com.cibertec.backend.controller;

import com.cibertec.backend.entites.ChangeDoc;
import com.cibertec.backend.repositories.ChangeDocRepository;
import com.cibertec.backend.services.ChangeDocServiceImpl;
import com.cibertec.backend.utils.dto.ChangeDocDTO;
import com.cibertec.backend.utils.dto.ChangeDocRDTO;
import com.cibertec.backend.utils.dto.TicketDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = {"api/v1/documento-cambio"})
public class ChangeDocController {

    @Autowired
    private ChangeDocServiceImpl changeDocService;

    @Autowired
    private ChangeDocRepository changeDocRepository;

    @PreAuthorize("hasRole('SALE')")
    @PostMapping("/registrar-ticket-doc-cambio")
    public ResponseEntity<?> registraDocumentoCambio(@RequestBody TicketDTO ticketDTO){

        try {
            return new ResponseEntity<>(changeDocService.registraDocumentoCambio(ticketDTO.getImei(),ticketDTO.getComment()), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('TECH')")
    @PutMapping("/actualizar-revision")
    public ResponseEntity<?> actualizarEstadoTecnicoRevision(@RequestBody ChangeDocDTO changeDocDTO){

        try {
            return new ResponseEntity<>(changeDocService.actualizarEstadoTecnicoRevision(changeDocDTO.getImei()), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('TECH')")
    @PutMapping("/actualizar-aprobado")
    public ResponseEntity<?> actualizarEstadoTecnicoAprobado(@RequestBody ChangeDocDTO changeDocDTO){

        try {
            return new ResponseEntity<>(changeDocService.actualizarEstadoTecnicoAprobado(changeDocDTO.getImei()), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('TECH')")
    @PutMapping("/actualizar-rechazado")
    public ResponseEntity<?> actualizarEstadoTecnicoRechazado(@RequestBody ChangeDocDTO changeDocDTO){

        try {
            return new ResponseEntity<>(changeDocService.actualizarEstadoTecnicoRechazado(changeDocDTO.getImei(), changeDocDTO.getComment()), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }



    @PreAuthorize("hasRole('SALE')")
    @PutMapping("/registra-cambio-equipo")
    public ResponseEntity<?> registrarCambio(@RequestBody ChangeDocDTO changeDocDTO){

        try {
            return new ResponseEntity<>(changeDocService.registrarCambio(changeDocDTO.getOldImei(), changeDocDTO.getNewImei()), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('SALE')")
    @PutMapping("/actualizar-espera")
    public ResponseEntity<?> actualizarEstadoClienteEsperaLlegadaNuevoEquipo(@RequestBody ChangeDocDTO changeDocDTO){

        try {
            return new ResponseEntity<>(changeDocService.actualizarEstadoClienteEsperaLlegadaNuevoEquipo(changeDocDTO.getImei()), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }



    @PreAuthorize("hasRole('SALE')")
    @GetMapping("doc-cambio-tikcet-imei")
    public ResponseEntity<?> obtenerDocCambioDeTicketPorIemi(@RequestParam String imei){
        return new ResponseEntity<>(changeDocService.obtenerDocCambioDeTicketPorImei(imei),HttpStatus.NOT_FOUND);
}




}

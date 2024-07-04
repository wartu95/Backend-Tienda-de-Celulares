package com.cibertec.backend.services.interfaces;

import com.cibertec.backend.utils.dto.ChangeDocRDTO;

public interface IChangeDocService {

    public int registraDocumentoCambio(String imei,String comment);

    public int actualizarEstadoTecnicoRevision(String imei);

    public int actualizarEstadoTecnicoAprobado(String imei);

    public int registrarCambio(String oldImei,String newImei);
    public int actualizarEstadoTecnicoRechazado(String imei, String comment);

    public int actualizarEstadoClienteEsperaLlegadaNuevoEquipo(String imei);
    ChangeDocRDTO obtenerDocCambioDeTicketPorImei(String imei);

}

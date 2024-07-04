package com.cibertec.backend.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeDocRDTO {

    private Integer id;

    private String state;

    private Long ticket;

    private Timestamp createdDate;

    private Timestamp updateDate;

    private Timestamp ticketDate;

    private String imeiInRevision;
    private String comment;

}


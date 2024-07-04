package com.cibertec.backend.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeDocDTO {

    private String imei;

    private String oldImei;

    private String newImei;
    private String comment;

}

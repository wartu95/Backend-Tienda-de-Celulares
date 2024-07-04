package com.cibertec.backend.entites;


import com.cibertec.backend.utils.enums.EStateChangeDoc;
import com.cibertec.backend.utils.enums.EStateProduct;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name="estados_documento_cambio")
public class StateChangeDoc {

    @Id
    @Column(columnDefinition = "char(2)")
    private String id;

    @Enumerated(EnumType.STRING)
    private EStateChangeDoc state;


    @OneToMany(mappedBy = "stateChangeDoc", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<ChangeDoc> changeDocs;

}

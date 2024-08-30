package org.iesvdm.preproyectoapirest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.iesvdm.preproyectoapirest.domain.Status;

@Data
@AllArgsConstructor
public class RequestDTO {
    private String receiver;
    private Long sender;
    private Status status;
}

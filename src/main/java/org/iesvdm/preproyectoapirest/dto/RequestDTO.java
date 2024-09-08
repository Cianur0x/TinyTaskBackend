package org.iesvdm.preproyectoapirest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.iesvdm.preproyectoapirest.domain.Status;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {
    private Long id;
    private Long sender;
    private String receiver;
    private Status status;
}

package org.iesvdm.preproyectoapirest.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.iesvdm.preproyectoapirest.domain.State;

@Data
@AllArgsConstructor
public class UserDTO {
    private Long id;

    private String username;

    private String imgProfile;

    @Enumerated(EnumType.STRING)
    private State State;
}

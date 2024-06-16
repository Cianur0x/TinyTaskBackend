package org.iesvdm.preproyectoapirest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.iesvdm.preproyectoapirest.domain.Role;

import java.util.Set;

@Data
@AllArgsConstructor
public class ManageUserDTO {
    private Long id;
    private String username;
    private String email;
    private Set<Role> rol;
    private String lastConnection;
}

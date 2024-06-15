package org.iesvdm.preproyectoapirest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EditUserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String newPass;
    private String state;
    private String bio;
}

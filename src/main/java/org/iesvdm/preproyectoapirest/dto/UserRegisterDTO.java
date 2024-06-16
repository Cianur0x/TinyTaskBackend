package org.iesvdm.preproyectoapirest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
@Data
@AllArgsConstructor
public class UserRegisterDTO {
    private String username;
    private String password;
    private String email;
    private Set<String> roles;
}

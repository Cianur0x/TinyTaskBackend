package org.iesvdm.preproyectoapirest.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
@Data
@AllArgsConstructor
public class UserRegistrerDTO {
    private String username;
    private String password;
    private String email;
    private Set<String> roles;
}

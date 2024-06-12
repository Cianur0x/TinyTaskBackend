package org.iesvdm.preproyectoapirest.exception;

import org.springframework.security.core.AuthenticationException;

public class IdUserNotFoundException  extends AuthenticationException {
    public IdUserNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public IdUserNotFoundException(String msg) {
        super(msg);
    }
}

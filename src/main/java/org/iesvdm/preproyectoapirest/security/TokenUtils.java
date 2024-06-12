package org.iesvdm.preproyectoapirest.security;


import org.iesvdm.preproyectoapirest.service.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenUtils {

    @Autowired
    EncryptionUtil encryptionUtil;

    private static final Logger logger = LoggerFactory.getLogger(TokenUtils.class);

    public String generateToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return encryptionUtil.encrypt(new Date().getTime() + "#" + userDetails.getId());
    }

    public Object[] getTimeCreationUsername(String token) {
        String decrypt = encryptionUtil.decrypt(token);
        int i = decrypt.indexOf("#");

        return new Object[]{Long.parseLong(decrypt.substring(0, i)), decrypt.substring(i + 1, decrypt.length())};
    }

}


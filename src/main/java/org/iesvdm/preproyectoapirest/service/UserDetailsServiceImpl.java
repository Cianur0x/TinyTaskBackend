package org.iesvdm.preproyectoapirest.service;

import jakarta.transaction.Transactional;
import org.iesvdm.preproyectoapirest.domain.User;
import org.iesvdm.preproyectoapirest.exception.IdUserNotFoundException;
import org.iesvdm.preproyectoapirest.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con username: " + username));

        return UserDetailsImpl.build(user);
    }

    @Transactional
    public UserDetails loadUserById(Long idUser) throws IdUserNotFoundException {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new IdUserNotFoundException("Usuario no encontrado con id: " + idUser));

        return UserDetailsImpl.build(user);
    }
}

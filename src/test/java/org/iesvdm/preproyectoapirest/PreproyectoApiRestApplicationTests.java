package org.iesvdm.preproyectoapirest;

import org.iesvdm.preproyectoapirest.domain.*;
import org.iesvdm.preproyectoapirest.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
class PreproyectoApiRestApplicationTests {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    ThemeRepository themeRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;


    @Test
    void contextLoads() {
    }

    @Test
    void crearData() {
        Role role = new Role(1L, ARole.ROL_ADMIN, new HashSet<>());
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        Role role2 = new Role(2L, ARole.ROL_USER, new HashSet<>());
        Set<Role> roleSet2 = new HashSet<>();
        roleSet2.add(role2);

        User user1 = User.builder()
                .username("Carlos")
                .password(encoder.encode("luffy1"))
                .email("carlos@gmail.com")
                .lastConnection(new Date(2019 - 1900, 1, 29))
                .roles(roleSet)
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .username("Maria")
                .password(encoder.encode("water2"))
                .email("maria@gmail.com")
                .lastConnection(new Date(2019 - 1900, 12, 29))
                .roles(roleSet2)
                .build();
        userRepository.save(user2);

        User user3 = User.builder()
                .username("Jose")
                .password(encoder.encode("match3"))
                .email("jose@gmail.com")
                .lastConnection(new Date(2019 - 1900, 9, 29))
                .roles(roleSet2)
                .build();
        userRepository.save(user3);

        User user4 = User.builder()
                .username("Laura")
                .password(encoder.encode("liquid4"))
                .email("laura@gmail.com")
                .lastConnection(new Date(2019 - 1900, 6, 29))
                .roles(roleSet2)
                .build();
        userRepository.save(user4);

        User user5 = User.builder()
                .username("Andres")
                .password(encoder.encode("breath5"))
                .email("andres@gmail.com")
                .lastConnection(new Date(2019 - 1900, 5, 29))
                .roles(roleSet2)
                .build();
        userRepository.save(user5);

        User user6 = User.builder()
                .username("Isabel")
                .password(encoder.encode("salt6"))
                .email("isabel@gmail.com")
                .lastConnection(new Date(2019 - 1900, 4, 29))
                .roles(roleSet2)
                .build();
        userRepository.save(user6);

        User user7 = User.builder()
                .username("Javier")
                .password(encoder.encode("clean7"))
                .email("javier@gmail.com")
                .lastConnection(new Date(2019 - 1900, 3, 29))
                .roles(roleSet2)
                .build();
        userRepository.save(user7);

        User user8 = User.builder()
                .username("Sofia")
                .password(encoder.encode("fruit8"))
                .email("sofia@gmail.com")
                .lastConnection(new Date(2019 - 1900, 2, 29))
                .roles(roleSet2)
                .build();
        userRepository.save(user8);

        User user9 = User.builder()
                .username("Pedro")
                .password(encoder.encode("bone9"))
                .email("pedro@gmail.com")
                .lastConnection(new Date(2019 - 1900, 1, 29))
                .roles(roleSet2)
                .build();
        userRepository.save(user9);

        User user10 = User.builder()
                .username("Claudia")
                .password(encoder.encode("mineral10"))
                .email("claudia@gmail.com")
                .lastConnection(new Date(2019 - 1900, 1, 29))
                .roles(roleSet2)
                .build();
        userRepository.save(user10);

        User user11 = User.builder()
                .username("Roberto")
                .password(encoder.encode("metal11"))
                .email("roberto@gmail.com")
                .lastConnection(new Date(2019 - 1900, 5, 29))
                .roles(roleSet)
                .build();
        userRepository.save(user11);

        User user12 = User.builder()
                .username("Anastasia")
                .password(encoder.encode("wire12"))
                .email("ana@gmail.com")
                .lastConnection(new Date(2019 - 1900, 5, 29))
                .roles(roleSet)
                .build();
        userRepository.save(user12);

        User user13 = User.builder()
                .username("Luis")
                .password(encoder.encode("silver13"))
                .email("luis@gmail.com")
                .lastConnection(new Date(2019 - 1900, 5, 29))
                .roles(roleSet)
                .build();
        userRepository.save(user13);

        User user14 = User.builder()
                .username("Raquel")
                .password(encoder.encode("gold14"))
                .email("raquel@gmail.com")
                .lastConnection(new Date(2019 - 1900, 5, 29))
                .roles(roleSet)
                .build();
        userRepository.save(user14);

        User user15 = User.builder()
                .username("Fernando")
                .password(encoder.encode("nuclear15"))
                .email("fernando@gmail.com")
                .lastConnection(new Date(2019 - 1900, 5, 29))
                .roles(roleSet)
                .build();
        userRepository.save(user15);

    }
}



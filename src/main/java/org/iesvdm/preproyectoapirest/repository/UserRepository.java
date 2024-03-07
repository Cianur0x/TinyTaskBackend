package org.iesvdm.preproyectoapirest.repository;

import org.iesvdm.preproyectoapirest.domain.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findUsersByUsernameContainingIgnoreCase(String username, Sort sort);
}

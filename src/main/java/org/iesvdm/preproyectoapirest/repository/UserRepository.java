package org.iesvdm.preproyectoapirest.repository;

import org.iesvdm.preproyectoapirest.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

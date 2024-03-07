package org.iesvdm.preproyectoapirest.repository;

import org.iesvdm.preproyectoapirest.domain.Role;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findRoleByRoleNameContainingIgnoreCase(String name, Sort sort);
}

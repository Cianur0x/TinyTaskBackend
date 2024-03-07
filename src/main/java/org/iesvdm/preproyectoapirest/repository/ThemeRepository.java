package org.iesvdm.preproyectoapirest.repository;

import org.iesvdm.preproyectoapirest.domain.Theme;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {
    List<Theme> findThemeByPrimaryColorContainingIgnoreCase(String color, Sort sort);
}

package org.iesvdm.preproyectoapirest.repository;

import org.iesvdm.preproyectoapirest.domain.Tag;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findTagsByNameContainingIgnoreCase(String name, Sort sort);
}

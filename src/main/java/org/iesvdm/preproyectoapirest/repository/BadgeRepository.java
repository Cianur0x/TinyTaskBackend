package org.iesvdm.preproyectoapirest.repository;

import org.iesvdm.preproyectoapirest.domain.Badge;
import org.iesvdm.preproyectoapirest.domain.UBadge;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
    List<Badge> findBadgeByBadgeNameContainingIgnoreCase(String name, Sort sort);

    Optional<Badge> findBadgeByBadgeName(UBadge badge);
}

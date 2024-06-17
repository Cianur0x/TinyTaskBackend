package org.iesvdm.preproyectoapirest.repository;

import org.iesvdm.preproyectoapirest.domain.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findUsersByUsernameContainingIgnoreCase(String username, Sort sort);

    Optional<User> findByUsernameIgnoreCase(String username);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.friendList f WHERE f.id = :id OR u.id = :id")
    Set<User> findFriendsByUserIdOrFriendId(Long id);
}

package org.iesvdm.preproyectoapirest.repository;

import org.iesvdm.preproyectoapirest.domain.FriendRequest;
import org.iesvdm.preproyectoapirest.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    Optional<FriendRequest> findBySender_IdAndReceiver_Id(Long senderId, Long receiverId);

    List<FriendRequest> findAllBySender_Id(Long senderId);
    List<FriendRequest> findAllByReceiver_Id(Long receiverId);
}

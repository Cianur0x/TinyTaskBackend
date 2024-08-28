package org.iesvdm.preproyectoapirest.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "friend_request")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class FriendRequest {

    @Id
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender_id", referencedColumnName = "id", nullable = false)
    private User sender;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "receiver_id", referencedColumnName = "id", nullable = false)
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "sent_at", nullable = false)
    LocalDateTime sentAt;

    @Column(name = "responded_at", nullable = false)
    LocalDateTime respondedAt; // se actualiza cuando se cambia el estado entre ACCEPTED o DECLINED

    public FriendRequest(final User sender, final User receiver) {
        this.id = 0L;
        this.sender = sender;
        this.receiver = receiver;
        this.status = Status.PENDING;
        this.sentAt = LocalDateTime.now();
    }
}

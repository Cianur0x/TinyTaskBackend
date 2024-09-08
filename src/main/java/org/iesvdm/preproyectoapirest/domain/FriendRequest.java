package org.iesvdm.preproyectoapirest.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "friend_request",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"sender_id", "receiver_id"})
        })
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender_id", referencedColumnName = "id", nullable = false)
    private User sender;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "receiver_id", referencedColumnName = "id", nullable = false)
    private User receiver;

    @Enumerated(EnumType.STRING)
    @ToString.Include
    @Column(name = "status", nullable = false)
    private Status status;

    public FriendRequest(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = Status.PENDING;
    }
}

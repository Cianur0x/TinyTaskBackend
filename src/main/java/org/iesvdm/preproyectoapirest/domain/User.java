package org.iesvdm.preproyectoapirest.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class User {
    public enum State {
        ACTIVE,
        BUSY,
        AWAY,
        OFFLINE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @ToString.Include
    private String username;

    @ToString.Include
    private String email;

    private String password;

    @ToString.Include
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private Date lastConnection;

    private String imgProfile;

    // TODO HACER UN ENMUN COMO VALOR POR DEFECTO A UN ESTADO
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private State State;

    @ToString.Include
    private String biography;

    @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Task> tareasCreadas = new HashSet<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany()
    @JoinTable(
            name = "friend_list",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id", referencedColumnName = "id")
    )
    private Set<User> friendList = new HashSet<>();

    @ManyToMany()
    @JoinTable(
            name = "task_viewer",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private Set<Task> viewedTasks = new HashSet<>();

    @ManyToOne()
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @ManyToOne()
    @JoinColumn(name = "role_id")
    private Role role;

}

package org.iesvdm.preproyectoapirest.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    @Email
    @ToString.Include
    private String email;

    @NotBlank
    @Size(max = 120)
    @Size(min = 6)
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}

package org.iesvdm.preproyectoapirest.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.iesvdm.preproyectoapirest.dto.UserDTO;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "task")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String title;

    private Boolean taskDone;

    private String taskDuration;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private Date deadLine;

    @Temporal(TemporalType.TIME)
    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    private Date startTime;

    @Temporal(TemporalType.TIME)
    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    private Date endingTime;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToMany(mappedBy = "viewedTasks", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("viewedTasks")
    @JsonIgnore
    private Set<User> viewers = new HashSet<>();

    @Transient
    private Set<UserDTO> watchers = new HashSet<>();
}

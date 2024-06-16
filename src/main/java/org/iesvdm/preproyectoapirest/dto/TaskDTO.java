package org.iesvdm.preproyectoapirest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private Long id;
    private String owner;
    private String title;
    private Boolean taskDone;
    private String description;
    private String deadLine;
}

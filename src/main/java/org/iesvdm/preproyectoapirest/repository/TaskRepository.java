package org.iesvdm.preproyectoapirest.repository;

import org.iesvdm.preproyectoapirest.domain.Task;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findTaskByTagName(String tagName, Sort sort);

    List<Task> findTaskByTagName(String tagName);

    List<Task> findAllByTagName(Sort sort);
}

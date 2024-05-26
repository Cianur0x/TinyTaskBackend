package org.iesvdm.preproyectoapirest.repository;

import org.iesvdm.preproyectoapirest.domain.Task;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findTaskByTag_NameContainingIgnoreCase(String tagName, Sort sort);

    List<Task> findTaskByDeadLineAndUser_IdOrderByTitleAsc(Date deadLine, Long user_id);

    List<Task> findTaskByTaskDoneAndUser_IdAndTag_IdOrderByTitleAsc(Boolean taskDone, Long tagId, Long userId);

    @Query(value = "select T from Task  T Where T.deadLine between :startDate AND :endDate AND T.user.id = :userId")
    List<Task> getTasksByUserIDAndDeadlineBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("userId") Long userId);
}

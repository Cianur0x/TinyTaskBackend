package org.iesvdm.preproyectoapirest.repository;

import org.iesvdm.preproyectoapirest.domain.Tag;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findTagsByNameContainingIgnoreCase(String name, Sort sort);

    List<Tag> findTagsByUser_id(Long id);

    @Query(value = "SELECT EXTRACT(MONTH FROM t.deadLine) AS mes, COUNT(t) AS total, SUM(CASE WHEN t.taskDone = true THEN 1 ELSE 0 END) AS completedCount" +
            " FROM Task t WHERE t.user.id = :ownerId AND t.tag.id = :tagId AND t.deadLine BETWEEN :startDate AND :endDate GROUP BY EXTRACT(MONTH FROM t.deadLine)")
    List<Object[]> findMonthlyTaskCountByTag(@Param("ownerId") Long ownerId, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("tagId") Long tagId);

    @Query(value = """
            select t.id as idtag, count(ta.id) as totaltask
            from Tag t
            LEFT JOIN Task ta ON t.id = ta.tag.id
            where t.user.id = :ownerId OR t.user.id IS NULL and ta.user.id = :ownerId and ta.deadLine between :startDate and :endDate
            group by t.id order by t.id""")
    List<Object[]> tasksGroupByTagId(@Param("ownerId") Long ownerId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}

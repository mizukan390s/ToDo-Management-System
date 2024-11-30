package com.dmm.task.data.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dmm.task.data.entity.Tasks;

public interface TasksRepository extends JpaRepository<Tasks, Long> {

	@Query("select a from Tasks a where a.date=:date and name = :name")
	List<Tasks> findByDateBetween(@Param("date") LocalDate date, @Param("name") String name);

	@Query("select a from Tasks a where a.date=:date")
	List<Tasks> findAllByDateBetween(@Param("date") LocalDate date);

}
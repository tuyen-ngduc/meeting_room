package com.virtual_assistant.meet.repository;

import com.virtual_assistant.meet.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
    @Query("select d.name from Department d")
    List<String> findAllDepartmentNames();
    boolean existsByName(String name);
    Optional<Department> findByName(String name);



}

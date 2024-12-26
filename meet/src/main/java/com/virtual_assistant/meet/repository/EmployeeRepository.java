package com.virtual_assistant.meet.repository;
import com.virtual_assistant.meet.domain.Employee;
import com.virtual_assistant.meet.dto.response.EmployeeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    Optional<Employee> findByUsername(String username);
    Optional<Employee> findByIdEmployee(String idEmployee);


    boolean existsById(String id);

    @Query("select new com.virtual_assistant.meet.dto.response.EmployeeDTO(e.idEmployee, e.name, d.name) " + " from Employee e " + "JOIN e.department d")
    List<EmployeeDTO> findEmployee();

}

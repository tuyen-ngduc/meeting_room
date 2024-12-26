package com.virtual_assistant.meet.service;

import com.virtual_assistant.meet.domain.Employee;
import com.virtual_assistant.meet.repository.EmployeeRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    public CustomUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Người dùng không tồn tại"));

        String role = "ROLE_" + employee.getPosition().getName().toUpperCase();
        return User.builder()
                .username(employee.getUsername())
                .password(employee.getPassword())
                .roles(role)
                .build();
    }
}

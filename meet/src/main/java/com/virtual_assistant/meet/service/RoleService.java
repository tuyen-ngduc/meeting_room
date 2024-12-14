package com.virtual_assistant.meet.service;

import com.virtual_assistant.meet.domain.Role;
import com.virtual_assistant.meet.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;


    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }

}

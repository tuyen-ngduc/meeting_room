package com.virtual_assistant.meet.service;

import com.virtual_assistant.meet.dto.response.RoomDTO;
import com.virtual_assistant.meet.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    @Autowired
    RoomRepository roomRepository;

    public List<RoomDTO> getAllRoom() {
        return roomRepository.findAllRoom();
    }
}

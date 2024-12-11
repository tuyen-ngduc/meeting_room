package com.virtual_assistant.meet.controller;

import com.virtual_assistant.meet.dto.response.RoomDTO;
import com.virtual_assistant.meet.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    RoomService roomService;
    @GetMapping
    public List<RoomDTO> getAllRoom() {
        return roomService.getAllRoom();
    }
}

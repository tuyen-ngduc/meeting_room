package com.virtual_assistant.meet.service;

import com.virtual_assistant.meet.domain.Room;
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

    // Thêm phòng họp mới
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    // Cập nhật phòng họp
    public Room updateRoom(Long id, Room updatedRoom) {
        Room room = roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room not found"));
        room.setName(updatedRoom.getName());
        room.setCapacity(updatedRoom.getCapacity());
        room.setDescription(updatedRoom.getDescription());
        return roomRepository.save(room);
    }

    // Xóa phòng họp
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
}

package com.virtual_assistant.meet.repository;

import com.virtual_assistant.meet.domain.Room;
import com.virtual_assistant.meet.dto.response.RoomDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("select new com.virtual_assistant.meet.dto.response.RoomDTO(r.id, r.name, r.capacity)"
            + " from Room r ")
    List<RoomDTO> findAllRoom();
}

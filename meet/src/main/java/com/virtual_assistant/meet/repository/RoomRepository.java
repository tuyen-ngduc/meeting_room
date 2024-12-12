package com.virtual_assistant.meet.repository;

import com.virtual_assistant.meet.domain.Room;
import com.virtual_assistant.meet.dto.response.RoomDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("select new com.virtual_assistant.meet.dto.response.RoomDTO(r.id, r.name, r.capacity)"
            + " from Room r ")

    List<RoomDTO> findAllRoom();

    // Repository kiểm tra phòng họp có bị trùng lịch không
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Meeting m WHERE m.room.id = :roomId AND " +
            "((:startTime BETWEEN m.startTime AND m.endTime) OR " +
            "(:endTime BETWEEN m.startTime AND m.endTime) OR " +
            "(m.startTime BETWEEN :startTime AND :endTime))")
    boolean existsByRoomAndTimeRange(@Param("idRoom") Long idRoom,
                                     @Param("startTime") LocalDateTime startTime,
                                     @Param("endTime") LocalDateTime endTime);
    Optional<Room> findByName(String name);
}

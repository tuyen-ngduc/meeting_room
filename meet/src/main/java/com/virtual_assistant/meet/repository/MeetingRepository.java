package com.virtual_assistant.meet.repository;

import com.virtual_assistant.meet.domain.Employee;
import com.virtual_assistant.meet.domain.Meeting;
import com.virtual_assistant.meet.dto.response.MemberDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    Optional<Meeting> findById(long id);
    @Query("SELECT new com.virtual_assistant.meet.dto.response.MemberDTO(m.employee.idEmployee, m.employee.name, m.employee.department.name, m.role.name) " +
            "FROM Meeting mt JOIN mt.members m " +
            "WHERE mt.id = :idMeeting")
    List<MemberDTO> findMembersByMeetingId(@Param("idMeeting") Long idMeeting);

//    @Query("SELECT m FROM Meeting m WHERE m.room.id = :roomId " +
//            "AND ((:startTime BETWEEN m.startTime AND m.expectedEndTime) " +
//            "OR (:expectedEndTime BETWEEN m.startTime AND m.expectedEndTime) " +
//            "OR (m.startTime BETWEEN :startTime AND :expectedEndTime))")
//    List<Meeting> findConflictingMeetings(@Param("roomId") Long roomId,
//                                          @Param("startTime") LocalDateTime startTime,
//                                          @Param("expectedEndTime") LocalDateTime expectedEndTime);


    @Query("SELECT m FROM Meeting m JOIN m.members mem WHERE mem.employee = :employee")
    List<Meeting> findByMembersEmployee(@Param("employee") Employee employee);

}

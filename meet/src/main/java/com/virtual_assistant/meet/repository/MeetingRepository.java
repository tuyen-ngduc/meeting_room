package com.virtual_assistant.meet.repository;

import com.virtual_assistant.meet.domain.Meeting;
import com.virtual_assistant.meet.dto.response.MemberDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    @Query("SELECT new com.virtual_assistant.meet.dto.response.MemberDTO(m.employee.id, m.employee.name, m.employee.department.name) " +
            "FROM Meeting mt JOIN mt.members m " +
            "WHERE mt.id = :idMeeting")
    List<MemberDTO> findMembersByMeetingId(@Param("idMeeting") Long idMeeting);



}

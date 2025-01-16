package com.virtual_assistant.meet.repository;

import com.virtual_assistant.meet.domain.Meeting;
import com.virtual_assistant.meet.domain.Member;
import com.virtual_assistant.meet.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByEmployee_IdEmployee(String idEmployee);

    @Query("SELECT m FROM Member m WHERE m.employee.id = :employeeId")
    Member findByEmployeeId(@Param("employeeId") String employeeId);


    @Query("SELECT m FROM Member m JOIN m.meetings meeting " +
            "WHERE m.employee.idEmployee = :employeeId " +
            "AND meeting.startTime = :startTime")
    List<Member> findMembersByEmployeeAndMeetingTime(@Param("employeeId") String employeeId,
                                                     @Param("startTime") LocalDateTime startTime);

    @Query("SELECT COUNT(m) > 0 " +
            "FROM Member m JOIN m.meetings meeting " +
            "WHERE meeting.id = :meetingId AND m.employee.idEmployee = :employeeId")
    boolean existsByMeetingIdAndEmployeeId(@Param("meetingId") Long meetingId, @Param("employeeId") String employeeId);

    @Query("SELECT m.role.name FROM Member m JOIN m.meetings meet WHERE m.employee.idEmployee = :employeeId AND meet.id = :meetingId")
    String findRoleNameByEmployeeAndMeeting(@Param("employeeId") String employeeId, @Param("meetingId") Long meetingId);




}

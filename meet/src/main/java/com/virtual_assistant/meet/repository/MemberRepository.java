package com.virtual_assistant.meet.repository;

import com.virtual_assistant.meet.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    @Query("SELECT m FROM Member m WHERE m.employee.id = :employeeId")
    Member findByEmployeeId(@Param("employeeId") String employeeId);


    @Query("SELECT m FROM Member m JOIN m.meetings meeting " +
            "WHERE m.employee.id = :employeeId " +
            "AND meeting.startTime = :startTime")
    List<Member> findMembersByEmployeeAndMeetingTime(@Param("employeeId") String employeeId,
                                                     @Param("startTime") LocalDateTime startTime);


}

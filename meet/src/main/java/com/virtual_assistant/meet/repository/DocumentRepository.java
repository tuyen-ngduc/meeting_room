package com.virtual_assistant.meet.repository;

import com.virtual_assistant.meet.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByMeetingId(long meetingId);
}

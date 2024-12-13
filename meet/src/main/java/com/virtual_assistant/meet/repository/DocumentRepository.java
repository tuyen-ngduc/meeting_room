package com.virtual_assistant.meet.repository;

import com.virtual_assistant.meet.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {

}

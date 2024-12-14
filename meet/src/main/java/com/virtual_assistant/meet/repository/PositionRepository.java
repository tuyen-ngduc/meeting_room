package com.virtual_assistant.meet.repository;

import com.virtual_assistant.meet.domain.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {
}

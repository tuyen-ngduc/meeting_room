package com.virtual_assistant.meet.repository;

import com.virtual_assistant.meet.domain.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {

    Optional<Position> findByName(String name);
}

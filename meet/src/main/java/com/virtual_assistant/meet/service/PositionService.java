package com.virtual_assistant.meet.service;

import com.virtual_assistant.meet.domain.Position;
import com.virtual_assistant.meet.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PositionService {

    @Autowired
    private PositionRepository positionRepository;

    // Thêm chức vụ mới
    public Position createPosition(Position position) {
        return positionRepository.save(position);
    }

    // Cập nhật chức vụ
    public Position updatePosition(long id, Position position) {
        if (!positionRepository.existsById(id)) {
            throw new RuntimeException("Position not found with id " + id);
        }
        position.setId(id);  // Set the id to the existing one for update
        return positionRepository.save(position);
    }

    // Xóa chức vụ
    public void deletePosition(long id) {
        if (!positionRepository.existsById(id)) {
            throw new RuntimeException("Position not found with id " + id);
        }
        positionRepository.deleteById(id);
    }

    // Lấy tất cả các chức vụ
    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    // Lấy chức vụ theo id
    public Position getPositionById(long id) {
        return positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found with id " + id));
    }
}

package com.virtual_assistant.meet.controller;

import com.virtual_assistant.meet.domain.Position;
import com.virtual_assistant.meet.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/positions")
public class PositionController {

    @Autowired
    private PositionService positionService;

    // Lấy tất cả các chức vụ
    @GetMapping
    public List<Position> getAllPositions() {
        return positionService.getAllPositions();
    }

    // Lấy chức vụ theo id
    @GetMapping("/{id}")
    public ResponseEntity<Position> getPositionById(@PathVariable long id) {
        Position position = positionService.getPositionById(id);
        return ResponseEntity.ok(position);
    }

    // Thêm chức vụ mới
    @PostMapping
    public ResponseEntity<Position> createPosition(@RequestBody Position position) {
        Position createdPosition = positionService.createPosition(position);
        return ResponseEntity.ok(createdPosition);
    }

    // Cập nhật chức vụ
    @PutMapping("/{id}")
    public ResponseEntity<Position> updatePosition(@PathVariable long id, @RequestBody Position position) {
        Position updatedPosition = positionService.updatePosition(id, position);
        return ResponseEntity.ok(updatedPosition);
    }

    // Xóa chức vụ
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePosition(@PathVariable long id) {
        positionService.deletePosition(id);
        return ResponseEntity.ok("Position deleted successfully");
    }
}

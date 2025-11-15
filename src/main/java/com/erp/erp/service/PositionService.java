package com.erp.erp.service;

import com.erp.erp.model.Position;
import com.erp.erp.repository.PositionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PositionService {

    private final PositionRepository positionRepository;

    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @SuppressWarnings("null")
    public Position createPosition(Position position) {
        return positionRepository.save(position);
    }

    @SuppressWarnings("null")
    public Position updatePosition(Long id, Position position) {
        return positionRepository.findById(id)
                .map(existing -> {
                    existing.setTitle(position.getTitle());
                    existing.setDepartment(position.getDepartment());
                    return positionRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Position not found"));
    }

    @SuppressWarnings("null")
    public void deletePosition(Long id) {
        positionRepository.deleteById(id);
    }

    @SuppressWarnings("null")
    public Optional<Position> getPositionById(Long id) {
        return positionRepository.findById(id);
    }

    public List<Position> getAllPositions() {
        return positionRepository.findAll();
    }
}

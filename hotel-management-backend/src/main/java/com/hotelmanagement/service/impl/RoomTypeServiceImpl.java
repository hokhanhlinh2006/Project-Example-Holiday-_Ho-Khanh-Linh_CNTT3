package com.hotelmanagement.service.impl;

import com.hotelmanagement.dto.request.RoomTypeRequest;
import com.hotelmanagement.dto.response.RoomTypeResponse;
import com.hotelmanagement.entity.RoomType;
import com.hotelmanagement.exception.ResourceNotFoundException;
import com.hotelmanagement.repository.RoomTypeRepository;
import com.hotelmanagement.service.RoomTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;

    public RoomTypeResponse mapToResponse(RoomType rt) {
        RoomTypeResponse res = new RoomTypeResponse();
        res.setRoomTypeId(rt.getRoomTypeId());
        res.setName(rt.getName());
        res.setDescription(rt.getDescription());
        res.setMaxCapacity(rt.getMaxCapacity());
        res.setBasePrice(rt.getBasePrice());
        res.setSizeM2(rt.getSizeM2());
        res.setAmenities(rt.getAmenities());
        return res;
    }

    @Override
    @Transactional
    public RoomTypeResponse createRoomType(RoomTypeRequest request) {
        RoomType rt = RoomType.builder()
                .name(request.getName())
                .description(request.getDescription())
                .maxCapacity(request.getMaxCapacity())
                .basePrice(request.getBasePrice())
                .sizeM2(request.getSizeM2())
                .amenities(request.getAmenities())
                .build();
        roomTypeRepository.save(rt);
        return mapToResponse(rt);
    }

    @Override
    @Transactional
    public RoomTypeResponse updateRoomType(Integer id, RoomTypeRequest request) {
        RoomType rt = roomTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room type not found with ID: " + id));
        rt.setName(request.getName());
        rt.setDescription(request.getDescription());
        rt.setMaxCapacity(request.getMaxCapacity());
        rt.setBasePrice(request.getBasePrice());
        rt.setSizeM2(request.getSizeM2());
        rt.setAmenities(request.getAmenities());
        roomTypeRepository.save(rt);
        return mapToResponse(rt);
    }

    @Override
    @Transactional(readOnly = true)
    public RoomTypeResponse getRoomTypeById(Integer id) {
        RoomType rt = roomTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room type not found with ID: " + id));
        return mapToResponse(rt);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomTypeResponse> getAllRoomTypes() {
        return roomTypeRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteRoomType(Integer id) {
        RoomType rt = roomTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room type not found with ID: " + id));
        roomTypeRepository.delete(rt);
    }
}

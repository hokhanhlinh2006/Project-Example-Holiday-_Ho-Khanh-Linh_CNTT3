package com.hotelmanagement.service.impl;

import com.hotelmanagement.dto.request.RoomRequest;
import com.hotelmanagement.dto.response.RoomResponse;
import com.hotelmanagement.dto.response.RoomTypeResponse;
import com.hotelmanagement.entity.Room;
import com.hotelmanagement.entity.RoomType;
import com.hotelmanagement.exception.BadRequestException;
import com.hotelmanagement.exception.ResourceNotFoundException;
import com.hotelmanagement.repository.RoomRepository;
import com.hotelmanagement.repository.RoomTypeRepository;
import com.hotelmanagement.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;

    private RoomResponse mapToResponse(Room room) {
        RoomResponse res = new RoomResponse();
        res.setRoomId(room.getRoomId());
        res.setRoomNumber(room.getRoomNumber());
        res.setFloor(room.getFloor());
        res.setStatus(room.getStatus());

        RoomType rt = room.getRoomType();
        RoomTypeResponse rtRes = new RoomTypeResponse();
        rtRes.setRoomTypeId(rt.getRoomTypeId());
        rtRes.setName(rt.getName());
        rtRes.setDescription(rt.getDescription());
        rtRes.setMaxCapacity(rt.getMaxCapacity());
        rtRes.setBasePrice(rt.getBasePrice());
        rtRes.setSizeM2(rt.getSizeM2());
        rtRes.setAmenities(rt.getAmenities());
        
        res.setRoomType(rtRes);
        return res;
    }

    @Override
    @Transactional
    public RoomResponse createRoom(RoomRequest request) {
        if (roomRepository.existsByRoomNumber(request.getRoomNumber())) {
            throw new BadRequestException("Room number already exists");
        }
        RoomType rt = roomTypeRepository.findById(request.getRoomTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Room type not found with ID: " + request.getRoomTypeId()));

        Room room = Room.builder()
                .roomNumber(request.getRoomNumber())
                .floor(request.getFloor())
                .status(request.getStatus())
                .roomType(rt)
                .build();
        roomRepository.save(room);
        return mapToResponse(room);
    }

    @Override
    @Transactional
    public RoomResponse updateRoom(Integer id, RoomRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + id));
        RoomType rt = roomTypeRepository.findById(request.getRoomTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Room type not found with ID: " + request.getRoomTypeId()));

        room.setRoomNumber(request.getRoomNumber());
        room.setFloor(request.getFloor());
        room.setStatus(request.getStatus());
        room.setRoomType(rt);
        roomRepository.save(room);
        return mapToResponse(room);
    }

    @Override
    @Transactional(readOnly = true)
    public RoomResponse getRoomById(Integer id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + id));
        return mapToResponse(room);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteRoom(Integer id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + id));
        roomRepository.delete(room);
    }
}

package com.hotelmanagement.service;

import com.hotelmanagement.dto.request.RoomRequest;
import com.hotelmanagement.dto.response.RoomResponse;
import java.util.List;

public interface RoomService {
    RoomResponse createRoom(RoomRequest request);
    RoomResponse updateRoom(Integer id, RoomRequest request);
    RoomResponse getRoomById(Integer id);
    List<RoomResponse> getAllRooms();
    void deleteRoom(Integer id);
}

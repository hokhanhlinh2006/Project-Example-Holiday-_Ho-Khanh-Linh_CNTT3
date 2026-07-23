package com.hotelmanagement.service;

import com.hotelmanagement.dto.request.RoomTypeRequest;
import com.hotelmanagement.dto.response.RoomTypeResponse;
import java.util.List;

public interface RoomTypeService {
    RoomTypeResponse createRoomType(RoomTypeRequest request);
    RoomTypeResponse updateRoomType(Integer id, RoomTypeRequest request);
    RoomTypeResponse getRoomTypeById(Integer id);
    List<RoomTypeResponse> getAllRoomTypes();
    void deleteRoomType(Integer id);
}

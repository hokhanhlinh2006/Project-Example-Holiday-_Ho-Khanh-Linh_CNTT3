package com.hotelmanagement.dto.response;

import lombok.Data;

@Data
public class RoomResponse {
    private Integer roomId;
    private String roomNumber;
    private Integer floor;
    private String status;
    private RoomTypeResponse roomType;
}

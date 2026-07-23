package com.hotelmanagement.controller;

import com.hotelmanagement.dto.request.RoomTypeRequest;
import com.hotelmanagement.dto.response.ApiResponse;
import com.hotelmanagement.dto.response.RoomTypeResponse;
import com.hotelmanagement.service.RoomTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/room-types")
@RequiredArgsConstructor
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RoomTypeResponse>> createRoomType(@Valid @RequestBody RoomTypeRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Room type created", roomTypeService.createRoomType(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RoomTypeResponse>> updateRoomType(@PathVariable Integer id, @Valid @RequestBody RoomTypeRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Room type updated", roomTypeService.updateRoomType(id, request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoomTypeResponse>> getRoomTypeById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.success("Fetched room type", roomTypeService.getRoomTypeById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoomTypeResponse>>> getAllRoomTypes() {
        return ResponseEntity.ok(ApiResponse.success("Fetched all room types", roomTypeService.getAllRoomTypes()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteRoomType(@PathVariable Integer id) {
        roomTypeService.deleteRoomType(id);
        return ResponseEntity.ok(ApiResponse.success("Room type deleted", null));
    }
}

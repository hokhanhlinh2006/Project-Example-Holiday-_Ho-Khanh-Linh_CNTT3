package com.hotelmanagement.repository;

import com.hotelmanagement.entity.BookingService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingServiceRepository extends JpaRepository<BookingService, Integer> {
    
    
    
    
}

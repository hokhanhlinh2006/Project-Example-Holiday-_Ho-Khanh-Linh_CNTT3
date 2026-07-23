package com.hotelmanagement.repository;

import com.hotelmanagement.entity.User;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @EntityGraph(attributePaths = { "role" })
    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);
}

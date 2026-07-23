package com.hotelmanagement.config;

import com.hotelmanagement.entity.*;
import com.hotelmanagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomRepository roomRepository;
    private final ServiceRepository serviceRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Chỉ seed 1 lần: nếu đã có role thì bỏ qua
        if (roleRepository.count() > 0) {
            return;
        }

        // 1) Roles
        Role adminRole = roleRepository.save(Role.builder()
                .roleName("ADMIN")
                .description("Quản trị hệ thống")
                .build());
        Role receptionistRole = roleRepository.save(Role.builder()
                .roleName("RECEPTIONIST")
                .description("Lễ tân")
                .build());
        Role customerRole = roleRepository.save(Role.builder()
                .roleName("CUSTOMER")
                .description("Khách hàng")
                .build());

        // 2) Tài khoản admin (1 tài khoản)
        userRepository.save(User.builder()
                .email("admin@hotel.com")
                .password(passwordEncoder.encode("admin123"))
                .fullName("System Administrator")
                .phoneNumber("0900000000")
                .cccdPassport("000000000001")
                .loyaltyPoints(0)
                .role(adminRole)
                .build());

        // 3) 10 khách hàng (users)
        List<User> customers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            customers.add(User.builder()
                    .email("customer" + i + "@hotel.com")
                    .password(passwordEncoder.encode("customer" + i))
                    .fullName("Khách Hàng " + i)
                    .phoneNumber(String.format("09010000%02d", i))
                    .cccdPassport(String.format("0790000000%02d", i))
                    .loyaltyPoints(i * 10)
                    .role(customerRole)
                    .build());
        }
        userRepository.saveAll(customers);

        // 4) 10 nhân viên (employees)
        String[] positions = {
                "Receptionist", "Housekeeping", "Manager", "Accountant", "Security",
                "Chef", "Waiter", "Bellboy", "IT Support", "Maintenance"
        };
        List<Employee> employees = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            employees.add(Employee.builder()
                    .employeeCode(String.format("EMP%03d", i))
                    .fullName("Nhân Viên " + i)
                    .position(positions[i - 1])
                    .email("employee" + i + "@hotel.com")
                    .password(passwordEncoder.encode("employee" + i))
                    .phoneNumber(String.format("09020000%02d", i))
                    .salaryRate(new BigDecimal(8_000_000 + i * 500_000))
                    .role(receptionistRole)
                    .build());
        }
        employeeRepository.saveAll(employees);

        // 5) 10 loại phòng (room types)
        String[] typeNames = {
                "Standard Single", "Standard Double", "Superior", "Deluxe", "Family",
                "Executive Suite", "Junior Suite", "Presidential Suite", "Studio", "Penthouse"
        };
        List<RoomType> roomTypes = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            roomTypes.add(RoomType.builder()
                    .name(typeNames[i - 1])
                    .description("Loại phòng " + typeNames[i - 1])
                    .maxCapacity(1 + (i % 4))
                    .basePrice(new BigDecimal(500_000L + i * 300_000L))
                    .sizeM2(20 + i * 5)
                    .amenities("Wifi, TV, Điều hòa, Minibar")
                    .build());
        }
        List<RoomType> savedRoomTypes = roomTypeRepository.saveAll(roomTypes);

        // 6) 10 phòng (rooms) — mỗi phòng gán 1 loại phòng
        String[] statuses = {"VACANT_CLEAN", "OCCUPIED", "VACANT_DIRTY", "MAINTENANCE"};
        List<Room> rooms = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            rooms.add(Room.builder()
                    .roomNumber(String.format("%d0%d", (i - 1) / 5 + 1, i))
                    .floor((i - 1) / 5 + 1)
                    .status(statuses[i % statuses.length])
                    .roomType(savedRoomTypes.get(i - 1))
                    .build());
        }
        roomRepository.saveAll(rooms);

        // 7) 10 dịch vụ (services)
        String[] serviceNames = {
                "Giặt ủi", "Ăn sáng buffet", "Đưa đón sân bay", "Spa & Massage", "Gym",
                "Hồ bơi", "Thuê xe", "Tour du lịch", "Room Service", "Trông trẻ"
        };
        String[] units = {"lần", "suất", "chuyến", "lần", "ngày", "ngày", "ngày", "chuyến", "lần", "giờ"};
        List<HotelService> services = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            services.add(HotelService.builder()
                    .name(serviceNames[i - 1])
                    .description("Dịch vụ " + serviceNames[i - 1])
                    .price(new BigDecimal(50_000L + i * 30_000L))
                    .unit(units[i - 1])
                    .build());
        }
        serviceRepository.saveAll(services);
    }
}

# THIẾT KẾ CẤU TRÚC THƯ MỤC PROJECT SPRING BOOT
## DỰ ÁN: HOTEL MANAGEMENT SYSTEM (PROJECT EXAMPLE HOLIDAY)
**Tác giả:** Senior Java Developer  
**Tiêu chuẩn cấu trúc:** Clean Architecture & Spring Boot Best Practices  
**Ngôn ngữ:** Java 17

---

## 1. CÂY THƯ MỤC DỰ ÁN (PROJECT PACKAGES TREE)

Cấu trúc phân mục logic dưới đây được tổ chức theo tiêu chuẩn của các dự án Spring Boot lớn dùng cho doanh nghiệp. Cấu trúc này giúp tránh hiện tượng phân lớp quá sâu hoặc chồng chéo nghiệp vụ, đồng thời đáp ứng kiến trúc gọn gàng dễ tiếp cận (Clean Architecture).

```text
hotel-management-system/
├── pom.xml                                     # File quản lý dependencies của Maven
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── holiday/
    │   │           └── hotelmanagement/
    │   │               ├── HolidayHotelApplication.java  # Bootstrap Class chính
    │   │               │
    │   │               ├── config/             # Cấu hình cấu trúc (Infrastructure Configs)
    │   │               │   ├── SecurityConfig.java
    │   │               │   ├── WebConfig.java
    │   │               │   ├── CacheConfig.java
    │   │               │   └── JpaAuditConfig.java
    │   │               │
    │   │               ├── security/           # Lớp an ninh (Custom Security integration)
    │   │               │   ├── CustomUserDetailsService.java
    │   │               │   ├── UserPrincipal.java
    │   │               │   ├── CustomAccessDeniedHandler.java
    │   │               │   └── CustomAuthenticationEntryPoint.java
    │   │               │
    │   │               ├── jwt/                # Token Provider & Validation Filters
    │   │               │   ├── JwtTokenProvider.java
    │   │               │   ├── JwtAuthenticationFilter.java
    │   │               │   └── JwtProperties.java
    │   │               │
    │   │               ├── controller/         # Tầng giao tiếp Rest API Endpoints
    │   │               │   ├── AuthController.java
    │   │               │   ├── UserController.java
    │   │               │   ├── RoomController.java
    │   │               │   ├── BookingController.java
    │   │               │   └── DashboardController.java
    │   │               │
    │   │               ├── service/            # Tầng giao diện nghiệp vụ (Interfaces)
    │   │               │   ├── UserService.java
    │   │               │   ├── RoomService.java
    │   │               │   ├── BookingService.java
    │   │               │   ├── PaymentService.java
    │   │               │   └── impl/           # Hiện thực logic nghiệp vụ (Implementations)
    │   │               │       ├── UserServiceImpl.java
    │   │               │       ├── RoomServiceImpl.java
    │   │               │       ├── BookingServiceImpl.java
    │   │               │       └── PaymentServiceImpl.java
    │   │               │
    │   │               ├── repository/         # Tầng kết nối cơ sở dữ liệu (Spring Data JPA)
    │   │               │   ├── UserRepository.java
    │   │               │   ├── RoomRepository.java
    │   │               │   ├── BookingRepository.java
    │   │               │   └── PaymentRepository.java
    │   │               │
    │   │               ├── entity/             # Mô hình dữ liệu quan hệ ORM (JPA Entities)
    │   │               │   ├── BaseEntity.java
    │   │               │   ├── User.java
    │   │               │   ├── Room.java
    │   │               │   ├── Booking.java
    │   │               │   └── Payment.java
    │   │               │
    │   │               ├── dto/                # Data Transfer Objects
    │   │               │   ├── UserDto.java
    │   │               │   └── BookingDto.java
    │   │               │
    │   │               ├── request/            # Chứa các DTO nhận đầu vào từ Client
    │   │               │   ├── LoginRequest.java
    │   │               │   ├── RegisterRequest.java
    │   │               │   ├── BookingRequest.java
    │   │               │   └── PaymentRequest.java
    │   │               │
    │   │               ├── response/           # Chứa các DTO đóng gói đầu ra trả về Client
    │   │               │   ├── ApiResponse.java
    │   │               │   ├── TokenResponse.java
    │   │               │   ├── UserResponse.java
    │   │               │   └── RoomResponse.java
    │   │               │
    │   │               ├── mapper/             # MapStruct chuyển đổi Entity <=> DTO
    │   │               │   ├── UserMapper.java
    │   │               │   ├── RoomMapper.java
    │   │               │   └── BookingMapper.java
    │   │               │
    │   │               ├── exception/          # Quản lý biệt lệ hệ thống toàn cục
    │   │               │   ├── GlobalExceptionHandler.java
    │   │               │   ├── AppErrorCode.java
    │   │               │   └── CustomException.java
    │   │               │
    │   │               ├── validation/         # Custom Annotations Validation tùy chỉnh
    │   │               │   ├── PhoneVN.java
    │   │               │   └── PhoneVNValidator.java
    │   │               │
    │   │               ├── constant/           # Lớp lưu biến hằng số tĩnh
    │   │               │   ├── SecurityConstant.java
    │   │               │   └── MsgConstant.java
    │   │               │
    │   │               ├── enum/               # Định nghĩa các tập hằng số
    │   │               │   ├── Role.java
    │   │               │   ├── RoomStatus.java
    │   │               │   └── BookingStatus.java
    │   │               │
    │   │               └── util/               # Chứa các Helper Static Classes
    │   │                   ├── SecurityUtil.java
    │   │                   └── DateUtil.java
    │   │
    │   └── resources/
    │       ├── application.yml                 # File cấu hình cấu hình môi trường chính
    │       ├── application-dev.yml             # Cấu hình môi trường Local/Development
    │       └── application-prod.yml            # Cấu hình môi trường Production
    │
    └── test/                                   # Viết Test Cases
        └── java/
            └── com/
                └── holiday/
                    └── hotelmanagement/
                        ├── auth/               # Test cases của Authentication
                        └── booking/            # Test cases của Lịch trình đặt phòng
```

---

## 2. GIẢI THÍCH CHỨC NĂNG CHI TIẾT TỪNG PACKAGE

### `config`
*   **Chức năng:** Nơi khai báo và cấu hình các Third-party Libraries hoặc cài đặt sâu của Java Spring.
*   **Ví dụ:** `SecurityConfig` cấu hình cơ chế CORS, Spring Security Filter Chain; `CacheConfig` cài đặt thời gian sống (TTL) của cache; `JpaAuditConfig` kích hoạt `@CreatedBy` và `@CreatedDate`.

### `security`
*   **Chức năng:** Tích hợp logic xử lý phân quyền người dùng của Spring Security.
*   **Ví dụ:** `CustomUserDetailsService` load tài khoản từ database thông qua UserRepository để Spring Security so khớp thông tin; `UserPrincipal` định nghĩa cấu trúc dữ liệu người dùng được lưu trữ tạm thời trong luồng thread của hệ thống.

### `jwt`
*   **Chức năng:** Đóng vai trò là phân hệ con tự lập để xử lý riêng các tác vụ liên quan đến Token JWT (JSON Web Token).
*   **Ví dụ:** `JwtTokenProvider` phụ trách sinh chuỗi Token JWT ký bảo mật từ UserPrincipal và giải mã Token ngược lại; `JwtAuthenticationFilter` chặn bắt request để lọc Header `Authorization: Bearer <Token>`.

### `controller`
*   **Chức năng:** Nhận các HTTP request từ Client, ánh xạ đúng URL tới các Handler Methods.
*   **Quy tắc:** Chỉ tiếp nhận dữ liệu định dạng `request`, chuyển thông tin cho Service xử lý, nhận kết quả và bọc vào định dạng `response` để xuất ra cho Client. Không viết code xử lý logic nghiệp vụ hay truy quét SQL tại đây.

### `service` & `service.impl`
*   **Chức năng:** 
    *   `service`: Định nghĩa tập các giao diện (interface) nghiệp vụ của hệ thống khách sạn, giúp tăng tính trừu tượng và tách biệt thiết kế (Abstraction).
    *   `service.impl`: Triển khai chi tiết của interface. Nơi thực hiện tính toán nghiệp vụ khó (kiểm tra tính hợp lệ của ngày đặt phòng, áp dụng mã giảm giá, kiểm tra trạng thái thanh toán). Ràng buộc giao dịch hệ thống thông qua `@Transactional`.

### `repository`
*   **Chức năng:** Lớp giao tiếp trực tiếp để tương tác với MySQL Database. Kế thừa thư viện JPA `JpaRepository`.
*   **Ví dụ:** Định nghĩa các custom query bằng `@Query` hoặc viết các Method Name Query tự sinh của Spring Data JPA (ví dụ: `findByEmailAndDeletedFalse`).

### `entity`
*   **Chức năng:** Khai báo cấu trúc bảng cơ sở dữ liệu MySQL dưới dạng đối tượng Java (Object-Relational Mapping).
*   **Quy tắc:** Sử dụng annotations `@Entity`, `@Table`, `@Id` để ánh xạ cấu trúc. Sử dụng `@ManyToOne`, `@OneToMany` để biểu thị liên kết khoá ngoại. Tích hợp Lombok để tránh code boilerplate.

### `dto`
*   **Chức năng:** Lớp dữ liệu chung (Data Transfer Object) dùng để luân chuyển thông tin.
*   **Khoảng sử dụng:** Có mục đích đóng gói trung lập ở các tầng trung gian hoặc dữ liệu trao đổi nội bộ trước khi ánh xạ qua Entity.

### `request`
*   **Chức năng:** Module chứa các đối tượng hứng dữ liệu từ Body gửi lên của Client.
*   **Quy tắc:** Áp dụng chặt kiểm tra dữ liệu bằng các Validator Constraints (`@NotBlank`, `@NotNull`, `@Size`).
*   **Ví dụ:** `LoginRequest` nhận thông tin email/password; `BookingRequest` nhận cấu hình ngày đặt phòng/mã phân loại hạng phòng.

### `response`
*   **Chức năng:** Đóng gói khuôn mẫu dữ liệu trả về cho Client.
*   **Ví dụ:** `ApiResponse<T>` thiết kế tiêu chuẩn gồm trạng thái `success`, `timestamp` và dữ liệu động `T data`; `TokenResponse` chứa Access Token và Refresh Token cho client.

### `mapper`
*   **Chức năng:** Chuyển đổi qua lại giữa DTO, Request/Response và Entity Database tự động qua thư viện **MapStruct** khi biên dịch.
*   **Mục đích:** Đảm bảo tính bảo mật (che giấu trường nhạy cảm như băm password khi trả dữ liệu) và giảm tài nguyên CPU khi tự viết các hàm Setter/Getter thủ công.

### `exception`
*   **Chức năng:** Quản lý tập trung các lỗi phát sinh trong hệ thống.
*   **Ví dụ:** `GlobalExceptionHandler` chặn bắt mọi exception hiển thị lỗi chuẩn hoá; `AppErrorCode` là Enum chứa danh sách bảng mã lỗi nghiệp vụ của khách sạn cùng mã HTTP Status.

### `validation`
*   **Chức năng:** Định nghĩa các Custom Validation Annotations tuỳ biến riêng biệt không có sẵn trong thư viện.
*   **Ví dụ:** `@PhoneVN` dùng để đối soát chuỗi số điện thoại Việt Nam chuẩn nhà mạng, liên kết với Class triển khai `PhoneVNValidator`.

### `constant`
*   **Chức năng:** Lưu trữ các chuỗi / trị giá tĩnh, phân tách cấu hình cứng ra khỏi mã nguồn để dễ cập nhật.
*   **Ví dụ:** `SecurityConstant` định nghĩa thời gian sống của Token, đường dẫn API public; `MsgConstant` định nghĩa thông điệp thành công.

### `enum`
*   **Chức năng:** Khai báo danh mục dữ liệu có miền giá trị giới hạn cố định.
*   **Ví dụ:** `Role` (ADMIN, RECEPTIONIST, CUSTOMER); `RoomStatus` (OCCUPIED, VACANT_CLEAN, VACANT_DIRTY, MAINTENANCE).

### `util`
*   **Chức năng:** Chức năng chứa các static helper phục vụ tính toán tiện ích cho toàn bộ hệ thống.
*   **Ví dụ:** `SecurityUtil` trích xuất thông tin người dùng hiện hành từ ThreadContext; `DateUtil` so sánh khoảng cách chênh lệch múi ngày giờ checkout của khách.

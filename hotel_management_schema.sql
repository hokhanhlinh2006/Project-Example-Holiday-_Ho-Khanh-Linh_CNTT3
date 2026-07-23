-- ============================================================================
-- PROJECT: HOTEL MANAGEMENT SYSTEM (PROJECT EXAMPLE HOLIDAY)
-- SCHEMA CREATION & SAMPLE DATA INSERTS FOR MYSQL 8
-- ROLE: DATABASE ADMINISTRATOR
-- DESIGN STANDARDS: 3NF (Third Normal Form), Workbench Compatible
-- ============================================================================

-- ----------------------------------------------------------------------------
-- 1. DATABASE CREATION AND INITIALIZATION
-- ----------------------------------------------------------------------------
DROP DATABASE IF EXISTS hotel_management;
CREATE DATABASE hotel_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hotel_management;

-- ----------------------------------------------------------------------------
-- 2. CREATE SCHEMAS (TABLES, KEYS, DEFAULT VALUES, CHECK CONSTRAINTS)
-- ----------------------------------------------------------------------------

-- 2.1. Table: roles (Vai trò hệ thống)
CREATE TABLE roles (
    role_id INT AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL,
    description VARCHAR(255) NULL,
    CONSTRAINT pk_roles PRIMARY KEY (role_id),
    CONSTRAINT uq_role_name UNIQUE (role_name),
    CONSTRAINT chk_role_name CHECK (role_name IN ('ADMIN', 'RECEPTIONIST', 'CUSTOMER'))
) ENGINE=InnoDB;

-- 2.2. Table: users (Khách hàng đăng nhập trực tuyến)
CREATE TABLE users (
    user_id INT AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    cccd_passport VARCHAR(50) NULL,
    loyalty_points INT NOT NULL DEFAULT 0,
    role_id INT NOT NULL,
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_users PRIMARY KEY (user_id),
    CONSTRAINT uq_users_email UNIQUE (email),
    CONSTRAINT uq_users_phone UNIQUE (phone_number),
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles (role_id) ON DELETE RESTRICT,
    CONSTRAINT chk_loyalty_points CHECK (loyalty_points >= 0)
) ENGINE=InnoDB;

-- 2.3. Table: employees (Nhân viên khách sạn)
CREATE TABLE employees (
    employee_id INT AUTO_INCREMENT,
    employee_code VARCHAR(20) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    position VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    salary_rate DECIMAL(15,2) NOT NULL,
    role_id INT NOT NULL,
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_employees PRIMARY KEY (employee_id),
    CONSTRAINT uq_employees_code UNIQUE (employee_code),
    CONSTRAINT uq_employees_email UNIQUE (email),
    CONSTRAINT uq_employees_phone UNIQUE (phone_number),
    CONSTRAINT fk_employees_role FOREIGN KEY (role_id) REFERENCES roles (role_id) ON DELETE RESTRICT,
    CONSTRAINT chk_salary_rate CHECK (salary_rate >= 0)
) ENGINE=InnoDB;

-- 2.4. Table: room_types (Loại phòng / Hạng phòng)
CREATE TABLE room_types (
    room_type_id INT AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT NULL,
    max_capacity INT NOT NULL,
    base_price DECIMAL(15,2) NOT NULL,
    size_m2 INT NOT NULL,
    amenities TEXT NULL, -- Chỗ trữ danh sách JSON các tiện nghi phòng
    CONSTRAINT pk_room_types PRIMARY KEY (room_type_id),
    CONSTRAINT uq_room_types_name UNIQUE (name),
    CONSTRAINT chk_max_capacity CHECK (max_capacity > 0),
    CONSTRAINT chk_base_price CHECK (base_price >= 0),
    CONSTRAINT chk_size_m2 CHECK (size_m2 > 0)
) ENGINE=InnoDB;

-- 2.5. Table: rooms (Phòng vật lý)
CREATE TABLE rooms (
    room_id INT AUTO_INCREMENT,
    room_number VARCHAR(10) NOT NULL,
    floor INT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'VACANT_CLEAN',
    room_type_id INT NOT NULL,
    CONSTRAINT pk_rooms PRIMARY KEY (room_id),
    CONSTRAINT uq_room_number UNIQUE (room_number),
    CONSTRAINT fk_rooms_type FOREIGN KEY (room_type_id) REFERENCES room_types(room_type_id) ON DELETE RESTRICT,
    CONSTRAINT chk_rooms_floor CHECK (floor > 0),
    CONSTRAINT chk_rooms_status CHECK (status IN ('VACANT_CLEAN', 'VACANT_DIRTY', 'OCCUPIED', 'MAINTENANCE'))
) ENGINE=InnoDB;

-- 2.6. Table: bookings (Đặt phòng tổng thể)
CREATE TABLE bookings (
    booking_id INT AUTO_INCREMENT,
    user_id INT NULL, -- NULL nếu khách vãng lai đặt trực tiếp không qua register tài khoản trực tuyến
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    total_amount DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    booking_status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    CONSTRAINT pk_bookings PRIMARY KEY (booking_id),
    CONSTRAINT fk_bookings_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL,
    CONSTRAINT chk_total_amount CHECK (total_amount >= 0),
    CONSTRAINT chk_booking_status CHECK (booking_status IN ('PENDING', 'CONFIRMED', 'CANCELLED', 'CHECKED_IN', 'CHECKED_OUT'))
) ENGINE=InnoDB;

-- 2.7. Table: booking_details (Chi tiết chuyến lưu trú phòng)
CREATE TABLE booking_details (
    booking_detail_id INT AUTO_INCREMENT,
    booking_id INT NOT NULL,
    room_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    check_in_actual DATETIME NULL,
    check_out_actual DATETIME NULL,
    price_applied DECIMAL(15,2) NOT NULL,
    CONSTRAINT pk_booking_details PRIMARY KEY (booking_detail_id),
    CONSTRAINT fk_details_booking FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE CASCADE,
    CONSTRAINT fk_details_room FOREIGN KEY (room_id) REFERENCES rooms(room_id) ON DELETE RESTRICT,
    CONSTRAINT chk_date_range CHECK (end_date > start_date),
    CONSTRAINT chk_price_applied CHECK (price_applied >= 0)
) ENGINE=InnoDB;

-- 2.8. Table: invoices (Hóa đơn quyết toán tài chính)
CREATE TABLE invoices (
    invoice_id INT AUTO_INCREMENT,
    booking_id INT NOT NULL,
    employee_id INT NOT NULL,
    issue_date DATETIME NOT NULL,
    tax_amount DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    discount_amount DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    final_amount DECIMAL(15,2) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'UNPAID',
    CONSTRAINT pk_invoices PRIMARY KEY (invoice_id),
    CONSTRAINT uq_invoice_booking UNIQUE (booking_id), -- Mối quan hệ 1-1 giữa Booking và Invoice
    CONSTRAINT fk_invoices_booking FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE RESTRICT,
    CONSTRAINT fk_invoices_employee FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE RESTRICT,
    CONSTRAINT chk_tax_amount CHECK (tax_amount >= 0),
    CONSTRAINT chk_discount_amount CHECK (discount_amount >= 0),
    CONSTRAINT chk_final_amount CHECK (final_amount >= 0),
    CONSTRAINT chk_invoice_status CHECK (status IN ('UNPAID', 'PAID', 'CANCELLED'))
) ENGINE=InnoDB;

-- 2.9. Table: payments (Giao dịch thanh toán hóa đơn)
CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT,
    invoice_id INT NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    payment_amount DECIMAL(15,2) NOT NULL,
    transaction_id VARCHAR(100) NULL,
    payment_date DATETIME NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    CONSTRAINT pk_payments PRIMARY KEY (payment_id),
    CONSTRAINT fk_payments_invoice FOREIGN KEY (invoice_id) REFERENCES invoices(invoice_id) ON DELETE CASCADE,
    CONSTRAINT chk_payment_amount CHECK (payment_amount > 0),
    CONSTRAINT chk_payment_method CHECK (payment_method IN ('CASH', 'BANK_TRANSFER', 'CREDIT_CARD')),
    CONSTRAINT chk_payment_status CHECK (status IN ('PENDING', 'SUCCESS', 'FAILED'))
) ENGINE=InnoDB;

-- 2.10. Table: reviews (Đánh giá chất lượng phòng & dịch vụ)
CREATE TABLE reviews (
    review_id INT AUTO_INCREMENT,
    booking_id INT NOT NULL,
    user_id INT NOT NULL,
    rating INT NOT NULL,
    comment TEXT NULL,
    created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_reviews PRIMARY KEY (review_id),
    CONSTRAINT fk_reviews_booking FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE CASCADE,
    CONSTRAINT fk_reviews_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT chk_rating CHECK (rating BETWEEN 1 AND 5)
) ENGINE=InnoDB;

-- 2.11. Table: services (Danh mục dịch vụ khách sạn cung cấp)
CREATE TABLE services (
    service_id INT AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NULL,
    price DECIMAL(15,2) NOT NULL,
    unit VARCHAR(20) NOT NULL,
    CONSTRAINT pk_services PRIMARY KEY (service_id),
    CONSTRAINT uq_services_name UNIQUE (name),
    CONSTRAINT chk_service_price CHECK (price >= 0)
) ENGINE=InnoDB;

-- 2.12. Table: booking_services (Chi tiết tiêu dùng dịch vụ tại phòng nghỉ)
CREATE TABLE booking_services (
    booking_service_id INT AUTO_INCREMENT,
    booking_detail_id INT NOT NULL,
    service_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    price_applied DECIMAL(15,2) NOT NULL,
    service_date DATETIME NOT NULL,
    CONSTRAINT pk_booking_services PRIMARY KEY (booking_service_id),
    CONSTRAINT fk_bservice_detail FOREIGN KEY (booking_detail_id) REFERENCES booking_details(booking_detail_id) ON DELETE CASCADE,
    CONSTRAINT fk_bservice_service FOREIGN KEY (service_id) REFERENCES services(service_id) ON DELETE RESTRICT,
    CONSTRAINT chk_quantity CHECK (quantity > 0),
    CONSTRAINT chk_price_applied_serv CHECK (price_applied >= 0)
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 3. INDEXES FOR PERFORMANCE OPTIMIZATION (BẢNG CHỈ MỤC TRUY VẤN NHANH)
-- ----------------------------------------------------------------------------

-- Indexes cho người dùng tìm kiếm theo liên lạc chính
CREATE INDEX idx_users_email_search ON users (email);
CREATE INDEX idx_users_phone_search ON users (phone_number);

-- Index tra cứu buồng phòng theo số hiệu nhanh
CREATE INDEX idx_rooms_number_floor ON rooms (room_number, floor);

-- Index tối ưu hệ thống tìm phòng trống theo khoảng thời gian thực tế
CREATE INDEX idx_booking_details_date_range ON booking_details (start_date, end_date);

-- Index kiểm soát các Booking theo trạng thái nhanh
CREATE INDEX idx_bookings_status ON bookings (booking_status);

-- Index tracking nhanh hoá đơn tài chính
CREATE INDEX idx_invoices_status ON invoices (status);

-- Indexes gán nợ giao dịch
CREATE INDEX idx_payments_invoice_ref ON payments (invoice_id);

-- ----------------------------------------------------------------------------
-- 4. INSERT REALISTIC VIETNAMESE SAMPLE DATA (CHÈN DỮ LIỆU KIỂM THỬ)
-- ----------------------------------------------------------------------------

-- 4.1. Insert Roles
INSERT INTO roles (role_name, description) VALUES
('ADMIN', 'Quản trị viên toàn quyền hệ thống'),
('RECEPTIONIST', 'Nhân viên lễ tân thu ngân, điều hành phòng'),
('CUSTOMER', 'Khách hàng đăng đặt phòng trực tuyến');

-- 4.2. Insert Users (Password băm sẵn dùng BCrypt cho mật khẩu thô 'Password@123')
INSERT INTO users (email, password, full_name, phone_number, cccd_passport, loyalty_points, role_id) VALUES
('nguyenvana@gmail.com', '$2a$10$W14z14Ww35L7x8x3wXW9DeL1k5BvF5L9De0m08X8w8D/E.s/O9LwO', 'Nguyễn Văn An', '0912345678', '001095034567', 150, 3),
('tranthingocb@gmail.com', '$2a$10$W14z14Ww35L7x8x3wXW9DeL1k5BvF5L9De0m08X8w8D/E.s/O9LwO', 'Trần Thị Ngọc Bích', '0908765432', '002096001234', 80, 3),
('lehoangnam@gmail.com', '$2a$10$W14z14Ww35L7x8x3wXW9DeL1k5BvF5L9De0m08X8w8D/E.s/O9LwO', 'Lê Hoàng Nam', '0987654321', '034097005678', 0, 3),
('phamminhduc@gmail.com', '$2a$10$W14z14Ww35L7x8x3wXW9DeL1k5BvF5L9De0m08X8w8D/E.s/O9LwO', 'Phạm Minh Đức', '0356123456', '079093009999', 540, 3);

-- 4.3. Insert Employees
INSERT INTO employees (employee_code, full_name, position, email, password, phone_number, salary_rate, role_id) VALUES
('EMP001', 'Hồ Khánh Linh', 'Lễ tân trưởng (Receptionist Leader)', 'khanhlinh.ho@holidayhotel.com', '$2a$10$W14z14Ww35L7x8x3wXW9DeL1k5BvF5L9De0m08X8w8D/E.s/O9LwO', '0944123456', 15000000.00, 2),
('EMP002', 'Vũ Minh Tuấn', 'Lễ tân ca đêm', 'minhtuan.vu@holidayhotel.com', '$2a$10$W14z14Ww35L7x8x3wXW9DeL1k5BvF5L9De0m08X8w8D/E.s/O9LwO', '0977234567', 11000000.00, 2),
('EMP003', 'Nguyễn Thị Hải', 'Quản trị hệ thống (System Administrator)', 'hai.nguyen@holidayhotel.com', '$2a$10$W14z14Ww35L7x8x3wXW9DeL1k5BvF5L9De0m08X8w8D/E.s/O9LwO', '0988345678', 25000000.00, 1);

-- 4.4. Insert Room Types
INSERT INTO room_types (name, description, max_capacity, base_price, size_m2, amenities) VALUES
('Standard Double', 'Phòng tiêu chuẩn có 1 giường đôi thoải mái cho 2 người lớn', 2, 600000.00, 25, '{"tivi": true, "dieu_hoa": true, "nuoc_suoi": 2}'),
('Deluxe Twin', 'Phòng hướng nội thất cao cấp có 2 giường nhỏ riêng biệt', 2, 900000.00, 32, '{"tivi": true, "dieu_hoa": true, "mini_bar": true, "ban_cong": true}'),
('Suite Family', 'Căn hộ phòng thượng hạng có phòng khách lớn và giường King cho gia đình', 4, 1800000.00, 55, '{"tivi": true, "dieu_hoa": true, "ay_phat_tra": true, "mini_bar": true, "bon_tam": true}');

-- 4.5. Insert Rooms
INSERT INTO rooms (room_number, floor, status, room_type_id) VALUES
('101', 1, 'VACANT_CLEAN', 1),
('102', 1, 'VACANT_CLEAN', 1),
('201', 2, 'OCCUPIED', 2),
('202', 2, 'VACANT_DIRTY', 2),
('301', 3, 'OCCUPIED', 3),
('302', 3, 'MAINTENANCE', 3);

-- 4.6. Insert Bookings
INSERT INTO bookings (user_id, total_amount, booking_status) VALUES
(1, 1200000.00, 'CHECKED_OUT'), -- Đã ở xong và check-out thanh toán
(2, 900000.00, 'CHECKED_IN'),   -- Khách đang ở phòng (201)
(3, 1800000.00, 'PENDING');    -- Lượt đặt trước đang đợi thanh toán tạm giữ chỗ

-- 4.7. Insert Booking Details
INSERT INTO booking_details (booking_id, room_id, start_date, end_date, check_in_actual, check_out_actual, price_applied) VALUES
(1, 101, '2026-07-01', '2026-07-03', '2026-07-01 14:02:00', '2026-07-03 11:45:00', 600000.00),
(2, 201, '2026-07-08', '2026-07-09', '2026-07-08 14:15:00', NULL, 900000.00),
(3, 301, '2026-07-15', '2026-07-16', NULL, NULL, 1800000.00);

-- 4.8. Insert Invoices
INSERT INTO invoices (booking_id, employee_id, issue_date, tax_amount, discount_amount, final_amount, status) VALUES
(1, 1, '2026-07-03 11:50:00', 120000.00, 50000.00, 1270000.00, 'PAID');

-- 4.9. Insert Payments
INSERT INTO payments (invoice_id, payment_method, payment_amount, transaction_id, payment_date, status) VALUES
(1, 'BANK_TRANSFER', 1270000.00, 'FT20260703998812', '2026-07-03 11:52:00', 'SUCCESS');

-- 4.10. Insert Reviews
INSERT INTO reviews (booking_id, user_id, rating, comment) VALUES
(1, 1, 5, 'Phòng ở sạch sẽ thoáng mát, nhân sự lễ tân hỗ trợ nhiệt tình, vị trí khách sạn tuyệt vời.');

-- 4.11. Insert Services
INSERT INTO services (name, description, price, unit) VALUES
('Coca Cola MiniBar', 'Sử dụng lon coca trong tủ mini-bar của phòng', 25000.00, 'Lon'),
('Giặt là siêu tốc', 'Giặt ủi quần áo của khách lấy ngay trong 4 tiếng', 50000.00, 'Kg'),
('Búp phê bữa sáng', 'Bữa ăn sáng Buffet tại nhà hàng tầng trệt của khách sạn', 120000.00, 'Vé');

-- 4.12. Insert Booking Services (Phục vụ dịch vụ phát sinh thêm cho hoá đơn phòng đang lưu trú)
INSERT INTO booking_services (booking_detail_id, service_id, quantity, price_applied, service_date) VALUES
(1, 1, 2, 25000.00, '2026-07-02 18:30:00'), -- Khách phòng 101 dùng 2 lon Coca
(1, 2, 3, 50000.00, '2026-07-02 09:00:00'); -- Khách giặt ủi 3kg quần áo

-- ----------------------------------------------------------------------------
-- 5. FINAL TEST QUERY TO VALIDATE RELATIONSHIPS (TRUY VẤN THỬ KIỂM THỬ)
-- ----------------------------------------------------------------------------
SELECT 
    b.booking_id,
    u.full_name AS customer_name,
    rm.room_number,
    rt.name AS room_type,
    bd.start_date,
    bd.end_date,
    b.booking_status
FROM bookings b
JOIN users u ON b.user_id = u.user_id
JOIN booking_details bd ON b.booking_id = bd.booking_id
JOIN rooms rm ON bd.room_id = rm.room_id
JOIN room_types rt ON rm.room_type_id = rt.room_type_id;

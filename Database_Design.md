# TÀI LIỆU THIẾT KẾ CƠ SỞ DỮ LIỆU
## DỰ ÁN: HỆ THỐNG QUẢN LÝ KHÁCH SẠN (HOTEL MANAGEMENT SYSTEM)

### PHẦN VIII: THIẾT KẾ CƠ SỞ DỮ LIỆU QUAN HỆ (MYSQL 3NF STANDARD)

Hệ thống lưu trữ cơ sở dữ liệu được thiết kế theo chuẩn cơ sở dữ liệu quan hệ MySQL, đảm bảo tính chuẩn hóa đến dạng chuẩn 3 (Third Normal Form - 3NF). Thiết kế giúp loại bỏ tối đa các thuộc tính dư thừa, ngăn chặn xung đột dữ liệu và tăng tốc độ xử lý truy vấn giao dịch.

---

#### 1. Chi tiết cấu trúc các bảng dữ liệu (Table Schemas)

##### 1.1. Bảng: roles (Vai trò hệ thống)
Quản trị danh mục vai trò người dùng trong hệ thống để thực hiện phân quyền.
- **Tên bảng**: `roles`

| Tên cột | Kiểu dữ liệu | PK | FK | NULL | Mô tả |
|---|---|---|---|---|---|
| role_id | INT AUTO_INCREMENT | Yes | No | No | ID tự tăng của vai trò |
| role_name | VARCHAR(50) | No | No | No | Tên vai trò (ADMIN, RECEPTIONIST, CUSTOMER) (Unique) |
| description | VARCHAR(255) | No | No | Yes | Mô tả chức trách phân quyền vai trò |

##### 1.2. Bảng: users (Khách hàng)
Lưu trữ hồ sơ tài khoản của khách hàng đăng ký đặt phòng trực tuyến.
- **Tên bảng**: `users`

| Tên cột | Kiểu dữ liệu | PK | FK | NULL | Mô tả |
|---|---|---|---|---|---|
| user_id | INT AUTO_INCREMENT | Yes | No | No | ID khách hàng tự tăng |
| email | VARCHAR(100) | No | No | No | Địa chỉ email đăng ký tài khoản (Unique) |
| password | VARCHAR(255) | No | No | No | Mật khẩu truy cập dạng hashing |
| full_name | VARCHAR(100) | No | No | No | Họ tên đầy đủ của khách hàng |
| phone_number | VARCHAR(20) | No | No | No | Số điện thoại liên lạc (Unique) |
| cccd_passport | VARCHAR(50) | No | No | Yes | Số CCCD/Hộ chiếu phục vụ check-in |
| loyalty_points | INT | No | No | No | Điểm số tích lũy thành viên (Default: 0) |
| role_id | INT | No | Yes | No | Khóa ngoại liên kết bảng `roles` |
| created_at | TIMESTAMP | No | No | No | Thời gian khởi tạo tài khoản |
| updated_at | TIMESTAMP | No | No | No | Thời gian cập nhật hồ sơ gần nhất |

##### 1.3. Bảng: employees (Nhân viên khách sạn)
Quản lý hồ sơ nhân sự vận hành trực tiếp hệ thống.
- **Tên bảng**: `employees`

| Tên cột | Kiểu dữ liệu | PK | FK | NULL | Mô tả |
|---|---|---|---|---|---|
| employee_id | INT AUTO_INCREMENT | Yes | No | No | ID nhân viên tự tăng |
| employee_code | VARCHAR(20) | No | No | No | Mã số thẻ nhân viên (Unique) |
| full_name | VARCHAR(100) | No | No | No | Họ tên chi tiết của nhân viên |
| position | VARCHAR(50) | No | No | No | Bộ phận công tác (Lễ tân, Buồng phòng) |
| email | VARCHAR(100) | No | No | No | Email công việc được cấp (Unique) |
| password | VARCHAR(255) | No | No | No | Mật khẩu đăng nhập PMS |
| phone_number | VARCHAR(20) | No | No | No | Số điện thoại nhân viên (Unique) |
| salary_rate | DECIMAL(15,2) | No | No | No | Lương định mức phục vụ chấm công ca |
| role_id | INT | No | Yes | No | Khóa ngoại liên kết phân quyền bảng `roles` |
| created_at | TIMESTAMP | No | No | No | Ngày bắt đầu nhận ca việc |
| updated_at | TIMESTAMP | No | No | No | Ngày cập nhật thay đổi nhân viên |

##### 1.4. Bảng: room_types (Loại phòng)
Bảng định nghĩa phân cấp phòng hành chính thương mại của khách sạn.
- **Tên bảng**: `room_types`

| Tên cột | Kiểu dữ liệu | PK | FK | NULL | Mô tả |
|---|---|---|---|---|---|
| room_type_id | INT AUTO_INCREMENT | Yes | No | No | ID danh mục loại phòng |
| name | VARCHAR(100) | No | No | No | Tên loại phòng (VIP, Deluxe, Standard) (Unique) |
| description | TEXT | No | No | Yes | Chi tiết mô tả cấu trúc buồng phòng |
| max_capacity | INT | No | No | No | Số lượng người ở tối đa được khai báo |
| base_price | DECIMAL(15,2) | No | No | No | Đơn giá chuẩn thuê ngày |
| size_m2 | INT | No | No | No | Diện tích phòng theo mét vuông |
| amenities | TEXT | No | No | Yes | Dữ liệu dạng JSON chứa danh mục tiện nghi |

##### 1.5. Bảng: rooms (Phòng vật lý)
Quản trị danh sách các buồng phòng vật lý cụ thể tại khách sạn.
- **Tên bảng**: `rooms`

| Tên cột | Kiểu dữ liệu | PK | FK | NULL | Mô tả |
|---|---|---|---|---|---|
| room_id | INT AUTO_INCREMENT | Yes | No | No | ID phòng vật lý tự tăng |
| room_number | VARCHAR(10) | No | No | No | Số phòng thực tế (Ví dụ: 101, 305) (Unique) |
| floor | INT | No | No | No | Số tầng chứa phòng |
| status | VARCHAR(50) | No | No | No | Trạng thái phòng (VACANT_CLEAN, VACANT_DIRTY...) |
| room_type_id | INT | No | Yes | No | Khóa ngoại liên kết bảng `room_types` |

##### 1.6. Bảng: bookings (Đặt phòng tổng thể)
Khởi tạo thông tin đơn hàng đặt giữ phòng trước của khách hàng.
- **Tên bảng**: `bookings`

| Tên cột | Kiểu dữ liệu | PK | FK | NULL | Mô tả |
|---|---|---|---|---|---|
| booking_id | INT AUTO_INCREMENT | Yes | No | No | ID đơn đặt phòng tự tăng |
| user_id | INT | No | Yes | Yes | Khóa ngoại liên kết bảng `users` (NULL nếu đặt tại quầy) |
| created_at | TIMESTAMP | No | No | No | Thời điểm thực hiện lập đơn đặt phòng |
| updated_at | TIMESTAMP | No | No | No | Thời điểm cập nhật đơn phòng |
| total_amount | DECIMAL(15,2) | No | No | No | Tổng tạm tính giá thuê của lượt đặt |
| booking_status | VARCHAR(50) | No | No | No | Trạng thái đặt phòng (PENDING, PAID, CANCELLED...) |

##### 1.7. Bảng: booking_details (Chi tiết chuyến lưu trú phòng)
Bảng liên kết trung gian tháo gỡ quan hệ N-N giữa phòng vật lý và đơn đặt phòng theo khung ngày cụ thể. Ditches time intersections.
- **Tên bảng**: `booking_details`

| Tên cột | Kiểu dữ liệu | PK | FK | NULL | Mô tả |
|---|---|---|---|---|---|
| booking_detail_id | INT AUTO_INCREMENT | Yes | No | No | ID chi tiết đặt phòng tự tăng |
| booking_id | INT | No | Yes | No | Khóa ngoại liên kết bảng `bookings` |
| room_id | INT | No | Yes | No | Khóa ngoại liên kết bảng `rooms` |
| start_date | DATE | No | No | No | Ngày nhận phòng dự kiến |
| end_date | DATE | No | No | No | Ngày trả phòng dự kiến |
| check_in_actual | DATETIME | No | No | Yes | Giờ lễ tân quét nhận phòng thực tế |
| check_out_actual | DATETIME | No | No | Yes | Giờ lễ tân quét trả phòng thực tế |
| price_applied | DECIMAL(15,2) | No | No | No | Đơn giá phòng áp dụng lúc khóa booking |

##### 1.8. Bảng: invoices (Hóa đơn quyết toán)
Ghi nhận chứng từ hóa đơn tài chính khi khách thanh toán trả phòng.
- **Tên bảng**: `invoices`

| Tên cột | Kiểu dữ liệu | PK | FK | NULL | Mô tả |
|---|---|---|---|---|---|
| invoice_id | INT AUTO_INCREMENT | Yes | No | No | ID hóa đơn tự tăng |
| booking_id | INT | No | Yes | No | Khóa ngoại kết nối duy nhất bảng `bookings` |
| employee_id | INT | No | Yes | No | Khóa ngoại liên kết nhân viên thu ngân `employees` |
| issue_date | DATETIME | No | No | No | Thời điểm phát hành sổ sách hóa đơn |
| tax_amount | DECIMAL(15,2) | No | No | No | Giá trị thuế giá trị gia tăng áp thuế |
| discount_amount | DECIMAL(15,2) | No | No | No | Tiền chiết khấu trừ thẳng hóa đơn |
| final_amount | DECIMAL(15,2) | No | No | No | Tổng tiền thực tế thu từ khách |
| status | VARCHAR(50) | No | No | No | Trạng thái hóa đơn (UNPAID, PAID, CANCELLED) |

##### 1.9. Bảng: payments (Giao dịch thanh toán)
Lịch sử phiên giao dịch chuyển tiền xử lý hóa đơn.
- **Tên bảng**: `payments`

| Tên cột | Kiểu dữ liệu | PK | FK | NULL | Mô tả |
|---|---|---|---|---|---|
| payment_id | INT AUTO_INCREMENT | Yes | No | No | ID phiên giao dịch tự tăng |
| invoice_id | INT | No | Yes | No | Khóa ngoại liên kết bảng hóa đơn `invoices` |
| payment_method | VARCHAR(50) | No | No | No | Phương thức thanh toán (CASH, BANK_TRANSFER, CREDIT_CARD) |
| payment_amount | DECIMAL(15,2) | No | No | No | Số tiền chuyển thực tế tại giao dịch |
| transaction_id | VARCHAR(100) | No | No | Yes | Mã tham chiếu thu hộ của ngân hàng/ví điện tử |
| payment_date | DATETIME | No | No | No | Ngày ký nhận xác nhận giao dịch |
| status | VARCHAR(50) | No | No | No | Trạng thái giao dịch (PENDING, SUCCESS, FAILED) |

##### 1.10. Bảng: reviews (Đánh giá chất lượng)
Nhận xét của khách hàng đối với dịch vụ trải nghiệm lưu trú.
- **Tên bảng**: `reviews`

| Tên cột | Kiểu dữ liệu | PK | FK | NULL | Mô tả |
|---|---|---|---|---|---|
| review_id | INT AUTO_INCREMENT | Yes | No | No | ID đánh giá tự tăng |
| booking_id | INT | No | Yes | No | Khóa ngoại liên kết đơn hàng `bookings` |
| user_id | INT | No | Yes | No | Khóa ngoại liên kết tài khoản `users` viết đánh giá |
| rating | INT | No | No | No | Số sao chất lượng đánh giá (1 đến 5) |
| comment | TEXT | No | No | Yes | Văn bản chi tiết ý kiến khách hàng |
| created_at | TIMESTAMP | No | No | No | Mốc thời gian thực hiện bình luận |

##### 1.11. Bảng: services (Dịch vụ khách sạn)
Định nghĩa danh mục dịch vụ phụ trợ được khách sạn khai thác trực tiếp.
- **Tên bảng**: `services`

| Tên cột | Kiểu dữ liệu | PK | FK | NULL | Mô tả |
|---|---|---|---|---|---|
| service_id | INT AUTO_INCREMENT | Yes | No | No | ID dịch vụ tự tăng |
| name | VARCHAR(100) | No | No | No | Tên gọi dịch vụ (Giặt là, Bữa sáng...) (Unique) |
| description | VARCHAR(255) | No | No | Yes | Mô tả hình thức phục vụ |
| price | DECIMAL(15,2) | No | No | No | Đơn giá quy định thu dịch vụ |
| unit | VARCHAR(20) | No | No | No | Đơn vị tính hóa đơn của dịch vụ |

##### 1.12. Bảng: booking_services (Chi tiết tiêu thụ dịch vụ)
Bảng phụ nợ trung gian lưu trữ vết sử dụng dịch vụ tại phòng nghỉ của khách cư trú.
- **Tên bảng**: `booking_services`

| Tên cột | Kiểu dữ liệu | PK | FK | NULL | Mô tả |
|---|---|---|---|---|---|
| booking_service_id | INT AUTO_INCREMENT | Yes | No | No | ID tiêu thụ dịch vụ tự tăng |
| booking_detail_id | INT | No | Yes | No | Khóa ngoại liên kết `booking_details` chỉ định phòng dùng |
| service_id | INT | No | Yes | No | Khóa ngoại liên kết bảng hàng hóa dịch vụ `services` |
| quantity | INT | No | No | No | Số lượng đơn vị sử dụng (Default: 1) |
| price_applied | DECIMAL(15,2) | No | No | No | Đơn giá thực thu thời điểm sử dụng |
| service_date | DATETIME | No | No | No | Thời điểm hành vi dùng dịch vụ khởi phát |

---

#### 2. Mô tả Quan hệ nghiệp vụ giữa các bảng (Relational Map)

Các mối liên hệ phụ thuộc thực thể cơ sở dữ liệu được định nghĩa tuần tự thông qua các liên kết khóa ngoại bao gồm:
- 2.1. Phân quyền và Người dùng: Bảng `roles` kết nối đến bảng `users` và `employees` qua quan hệ 1-N. Mỗi bản ghi tài khoản chỉ thuộc một nhóm vai trò duy nhất kiểm soát an ninh hệ thống.
- 2.2. Phân hạng buồng phòng: Bảng `room_types` kết nối đến bảng `rooms` qua quan hệ 1-N. Cho phép một loại phân hạng phòng nghỉ (như Deluxe) chứa nhiều số phòng riêng lẻ thực tế.
- 2.3. Khách hàng và Lịch sử đơn đặt phòng: Bảng `users` kết nối đến bảng `bookings` qua quan hệ 1-N. Khách hàng trực tuyến được thực hiện tạo nhiều lượt đặt giữ chỗ phòng khác nhau. Khách vãng lai mua phòng trực tiếp được lưu giữ hồ sơ riêng với khóa `user_id` thiết lập ở giá trị NULL.
- 2.4. Khớp toán lịch phòng đặt: Bảng `booking_details` tháo gỡ quan hệ N-N giữa phòng vật lý `rooms` và lượt đặt phòng `bookings` bằng quan hệ 1-N hai chiều. Mối liên hệ này xác định lịch và trạng thái cư trú của một phòng cụ thể tránh trùng lặp.
- 2.5. Hóa đơn và Thanh toán: Bảng `bookings` kết nối đến bảng `invoices` qua quan hệ 1-1 hỗ trợ quyết toán chung cho đơn đặt phòng. Bảng `invoices` tiếp tục liên kết khóa ngoại 1-N đến bảng `payments` ghi nhận đầy đủ lịch sử gộp đợt chuyển tiền của khách hàng. Nhân sự tiếp tân tại quầy check-out được liên kết 1-N từ bảng `employees` đến `invoices` để ghi công nợ thu ngân lễ tân.
- 2.6. Đánh giá chất lượng: Bảng `bookings` và `users` đều liên kết 1-N đến `reviews` đảm bảo chặt chẽ nguyên lý chỉ người dùng thực tế đã sở hữu lượt đặt hoàn thành check-out mới gửi phản hồi chất lượng.
- 2.7. Sử dụng dịch vụ phòng nghỉ: Bảng `booking_details` kết nối 1-N đến bảng `booking_services` để chỉ định dịch vụ phụ được phát sinh cụ thể bởi phòng nghỉ nào. Bản ghi `booking_services` tham chiếu khóa ngoại đến bảng danh mục dịch vụ `services`.

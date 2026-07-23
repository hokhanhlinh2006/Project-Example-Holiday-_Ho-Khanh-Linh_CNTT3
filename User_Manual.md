# SÁCH HƯỚNG DẪN SỬ DỤNG HỆ THỐNG (USER MANUAL)
## HỆ THỐNG QUẢN LÝ KHÁCH SẠN (HOTEL MANAGEMENT SYSTEM)

Chào mừng bạn đến với tài liệu hướng dẫn sử dụng Hệ thống Quản lý Khách sạn. Tài liệu này được thiết kế để giúp Khách hàng, Nhân viên lễ tân và Quản trị viên nắm bắt nhanh chóng và vận hành khai thác hiệu quả các tính năng của phần mềm.

---

## 1. Giới thiệu tổng quan hệ thống (Introduction)
Hệ thống Quản lý Khách sạn (Hotel Management System) là nền tảng quản lý lưu trú trực tuyến toàn diện, hỗ trợ:
*   **Dành cho khách du lịch:** Tra cứu hạng phòng trống, đặt phòng nhanh, tự phục vụ dịch vụ ăn uống tận phòng, thanh toán trực tuyến thuận tiện và gửi đánh giá phản hồi chất lượng lưu trú.
*   **Dành cho Ban quản lý & Lễ tân:** Thực hiện thủ tục nhận phòng (Check-in), trả phòng (Check-out) tức thời, quản lý danh mục phòng trống, cập nhật tiện nghi và theo dõi chi tiết luồng tiền hóa đơn.
*   **Dành cho Quản trị viên (Admin):** Tra cứu biểu đồ tăng trưởng doanh số, theo dõi công suất lấp đầy phòng theo thời gian thực và quản lý phân quyền nhân viên tối ưu.

---

## 2. Tính năng Đăng ký tài khoản (Register)
*   **Mục đích:** Giúp du khách lần đầu ghé thăm trang web có thể tự lập tài khoản lưu trú cá nhân để tiến hành đặt chỗ nghỉ.
*   **Các bước thực hiện:**
    1. Nhấp chuột vào nút **"Đăng ký"** (Register) ở góc phải phía trên thanh điều hướng góc màn hình chính.
    2. Điền đầy đủ thông tin vào biểu mẫu hiển thị bao gồm:
        *   **Họ và tên:** Nhập tên đầy đủ (ví dụ: *Nguyễn Văn A*).
        *   **Địa chỉ Email:** Định dạng hợp lệ để nhận thông báo đặt buồng (ví dụ: *nva@gmail.com*).
        *   **Số điện thoại:** Đầy đủ 10 chữ số di động Việt Nam.
        *   **CCCD/Passport:** Dành làm thủ tục khai báo tạm trú trích xuất.
        *   **Mật khẩu:** Nhập tối thiểu 6 ký tự để bảo mật.
    3. Nhấp chọn nút **"Đăng ký tài khoản"**.
*   **Kết quả:** Hệ thống hiển thị hộp thoại thông báo *"Đăng ký thành công!"* và tự động điều hướng người dùng sang trang Đăng nhập.

---

## 3. Tính năng Đăng nhập hệ thống (Login)
*   **Mục đích:** Xác thực danh tính của Khách hàng, Lễ tân và Quản lý để cấp quyền truy cập các tính năng sâu tương ứng trong hệ thống.
*   **Các bước thực hiện:**
    1. Nhấp chuột vào nút **"Đăng nhập"** (Login) trên thanh menu điều hướng.
    2. Điền các thông tin bảo mật:
        *   **Tên Email đăng nhập**
        *   **Mật khẩu bảo vệ**
    3. Nhấp chọn nút **"Đăng nhập hệ thống"**.
*   **Kết quả:** Đăng nhập thành công, hệ thống lưu khóa bảo mật Access Token và hiển thị Avatar/Tên người dùng cá nhân góc phải, đồng thời kích hoạt các nút chức năng ẩn dựa trên quyền hạn tài khoản.

---

## 4. Tính năng Tìm kiếm & Đặt phòng (Bookings)
*   **Mục đích:** Cho phép du khách lựa chọn buồng phòng nghỉ ngơi ưa thích và đăng ký thời gian lưu trú dự kiến.
*   **Các bước thực hiện:**
    1. Trên thanh điều hướng, nhấp chọn mục **"Rooms"** (Phòng) để xem toàn bộ catalogue phòng hiện hành.
    2. Sử dụng bộ lọc danh mục phía trên để chọn Hạng phòng mong muốn (Standard, Superior, Deluxe, Suite...).
    3. Tìm buồng phòng ưng ý đang ở trạng thái trống sạch, nhấp vào nút **"Book Room"** (Đặt phòng).
    4. Tại form Đặt phòng:
        *   Nhập ngày nhận phòng mong muốn (Check-in Date).
        *   Nhập ngày trả phòng mong muốn (Check-out Date).
    5. Kiểm tra thông tin giá phòng ước tính hiển thị ở bảng thống kê bên dưới và nhấn **"Xác nhận đặt chỗ"**.
*   **Kết quả:** Hệ thống lập tức tạo giao dịch ở trạng thái chờ duyệt (PENDING), đồng thời chuyển hướng du khách tới trang Thanh toán phí.

---

## 5. Tính năng Thực hiện Thanh toán (Payment)
*   **Mục đích:** Hoàn thành nghĩa vụ tài chính phí đặt buồng của hóa đơn để nhân sự lễ tân chuẩn bị dọn dẹp sẵn phòng đón khách.
*   **Các bước thực hiện:**
    1. Truy cập vào hóa đơn của lượt đặt phòng tương ứng.
    2. Kiểm tra chi tiết phí phòng cơ bản, mức thuế suất VAT 10% tính tự động và giảm trừ Voucher khuyến mãi.
    3. Chọn Phương thức thanh toán mong muốn:
        *   **Chuyển khoản Ngân hàng (Bank Transfer)**: Quét mã QR code hoặc nhập thông tin STK hiển thị.
        *   **Thẻ tín dụng (Credit Card)**: Nhập thông tin thanh toán quốc tế VISA/Mastercard.
        *   **Tiền mặt (Cash / Pay at Counter)**: Dành cho khách thanh toán trực tiếp tại quầy khi làm thủ tục check-in.
    4. Nhấp chọn nút **"Xác nhận thanh toán"**.
*   **Kết quả:** Giao dịch ghi nhận thành công, trạng thái hóa đơn đổi sang **PAID**, trạng thái đặt phòng chuyển sang sẵn sàng tiếp nhận khách.

---

## 6. Tính năng Quản lý buồng phòng (Room Management)
*   **Mục đích:** Giúp nhân viên lễ tân và tổ dọn dẹp buồng phòng kiểm soát chặt chẽ trạng thái vệ sinh của 50 phòng thực tế trong khách sạn.
*   **Các bước thực hiện:**
    1. Tài khoản Lễ tân/Admin truy cập vào **"Admin Panel"** -> chọn tab **"Phòng"** (Rooms).
    2. Xem trực diện bảng tình trạng các phòng theo mã màu:
        *   *Màu xanh (VACANT_CLEAN)*: Phòng trống sạch, sẵn sàng giao khách nhận phòng.
        *   *Màu đỏ (OCCUPIED)*: Phòng đang bận, có khách đang cư trú.
        *   *Màu vàng (VACANT_DIRTY)*: Phòng bẩn, mới checkout, cần liên hệ tổ buồng dọn dẹp.
    3. Để thay đổi trạng thái buồng sau khi dọn sạch: nhấp chuột vào nút **"Cập nhật phòng"**, chỉnh trạng thái về **VACANT_CLEAN** và nhấn lưu.
*   **Kết quả:** Danh sách buồng phòng cập nhật tức thì trạng thái thực tế lên hệ thống để đón lượt đặt phòng kế cận.

---

## 7. Bảng Thống kê & Giám sát (Dashboard)
*   **Mục đích:** Giúp Quản lý cập nhật trực quan sức khỏe kinh doanh của khách sạn theo thời gian thực.
*   **Các bước thực hiện:**
    1. Đăng nhập bằng tài khoản quản trị Admin hoặc Lễ tân.
    2. Nhấp chọn mục **"Dashboard"** trên thanh menu sảnh điều hướng chính.
    3. Quan sát các phân khu báo cáo biểu diễn:
        *   **Chỉ số công suất lấp đầy phòng (Occupancy Rate):** Tỷ lệ phòng bận trên tổng số phòng hiện có của khách sạn.
        *   **Tổng doanh thu tích lũy:** Tổng đếm tiền của toàn bộ hóa đơn đã thu ngân.
        *   *Biểu đồ phân bổ nguồn doanh số:* Cơ cấu phần trăm thu nhập từ tiền phòng chính và doanh số các dịch vụ phụ (buffet, giặt là, spa...).
*   **Kết quả:** Dữ liệu tự động truy xuất động từ DB kết xuất đồ thị giúp nhà quản trị đưa ra quyết định phân loại giá phòng thích ứng.

---

## 8. Tính năng Quản lý người dùng (User Management)
*   **Mục đích:** ADMIN thực hiện kiểm tra hồ sơ, phân quyền bảo mật của nhân viên và hành vi khách hàng vi phạm chính sách lưu trú.
*   **Các bước thực hiện:**
    1. Truy cập trang quản trị hệ thống và chọn mục **"Người dùng"** (Users).
    2. Nhập email hoặc họ tên vào thanh công cụ tìm kiếm hành khách cần xử lý.
    3. Chọn tác vụ tương ứng:
        *   Nhấn **"Khóa tài khoản"** với tài khoản vi phạm chính sách quấy nhiễu đặt chỗ ảo.
        *   Nhấn **"Nâng quyền tài khoản"** để đổi gán vai trò người dùng thường sang Nhân sự lễ tân hỗ trợ công việc.
*   **Kết quả:** Tài khoản bị khóa lập tức bị từ chối cấp Token JWT và không thể thao tác đăng nhập hoạt động trên hệ thống.

---

## 9. Xuất kết xuất báo cáo tài chính (Reports)
*   **Mục đích:** Kết xuất mẫu hóa đơn lưu trú trực quan dạng PDF để đưa hành khách kiểm tra danh mục chi tiết chi tiêu trước khi checkout rời khỏi khách sạn.
*   **Các bước thực hiện:**
    1. Lễ tân truy cập tab **"Hóa đơn"** (Invoices) trên Dashboard điều phối quản trị.
    2. Đánh nhập mã số hóa đơn hoặc mã đặt chỗ của khách để gọi chi tiết tiền.
    3. Kiểm tra danh mục các dịch vụ phụ đã sử dụng hiển thị chi tiết (ví dụ: Spa 450.000đ, 2 ly nước cam 90.000đ...).
    4. Nhấp chuột chọn nút **"In hóa đơn"** (Print PDF) ở cuối biểu mẫu.
*   **Kết quả:** Trình duyệt tự động mở và tải xuống tệp tin hóa đơn PDF định dạng đẹp, chuyên nghiệp làm chứng từ bàn giao tài chính cho du khách.

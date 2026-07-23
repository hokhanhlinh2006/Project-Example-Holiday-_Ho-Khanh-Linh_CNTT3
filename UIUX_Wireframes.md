# ĐẶC TẢ THIẾT KẾ PHÁC THẢO GIAO DIỆN (WIREFRAMES)
## DỰ ÁN: HỆ THỐNG QUẢN LÝ KHÁCH SẠN (HOTEL MANAGEMENT SYSTEM)

### PHẦN XIII: ĐẶC TẢ CẤU TRÚC PHÂN BỐ GIAO DIỆN (WIREFRAME SPECIFICATIONS)

Tài liệu này thuyết minh chi tiết cấu trúc sắp xếp thành phần giao diện (Grid Layout, Component Placement) cho 15 màn hình chính thuộc hai phân hệ Khách hàng (Customer Portal) và Quản trị & Vận hành (Staff & Admin Portal). Thiết kế đảm bảo sự nhất quán phân bổ và đồng bộ tối ưu hóa trải nghiệm người dùng (UI/UX).

---

### PHÂNỆ 1: CỔNG THÔNG TIN KHÁCH HÀNG (CUSTOMER PORTAL)

#### 1. Màn hình: Trang chủ (Home Screen)
- **Header**:
  - Trái: Logo thương hiệu khách sạn.
  - Giữa: Danh mục đường dẫn (Loại phòng, Về chúng tôi, Liên hệ, Điểm thưởng).
  - Phải: Nút "Đăng nhập" và "Đăng ký" (Nếu đã đăng nhập, đổi thành Avatar và Tên hiển thị người dùng).
- **Sidebar**: Không áp dụng (N/A).
- **Content**:
  - Banner chính (Hero Section): Hình ảnh phòng khách sạn chất lượng cao kèm thông điệp chào mừng.
  - Thanh tìm kiếm đặt phòng nhanh (Sticky Search Bar): Nằm đè lên chân ảnh Banner.
  - Khối sản phẩm: Danh mục 3 loại phòng VIP, Deluxe và Standard dạng thẻ (Card Grid).
  - Khối lý do lựa chọn khách sạn (Amenities overview) và Khối đánh giá khách hàng tiêu biểu (Reviews slider).
- **Button**:
  - Nút "Tìm kiểm phòng trống" (Màu nổi bật chính, đặt cuối thanh tìm kiếm).
  - Nút "Xem chi tiết phòng" (Dạng đường viền mỏng đặt dưới chân mỗi Card loại phòng).
- **Form**: Form tìm kiếm phòng nhanh gồm (Trường chọn ngày Check-in, Ngày Check-out, Ô chọn số lượng khách).
- **Table**: Không áp dụng (N/A).
- **Footer**:
  - Cột 1: Logo và Mô tả ngắn về khách sạn.
  - Cột 2: Hotline, Email và Địa chỉ khách sạn.
  - Cột 3: Liên kết nhanh đến các trang Chính sách bảo mật, Chính sách hoàn cọc.
  - Chân trang: Dòng thông tin bản quyền (Copyright).

---

#### 2. Màn hình: Đăng nhập (Login Screen)
- **Header**: Thiết kế rút gọn, chỉ hiển thị Logo khách sạn căn giữa để đưa sự chú ý vào form nhập liệu.
- **Sidebar**: Không áp dụng (N/A).
- **Content**:
  - Khung thẻ đăng nhập (Card Layout) căn dọc và ngang chính giữa màn hình.
  - Liên kết khôi phục mật khẩu và đường link đăng ký nếu chưa có tài khoản.
- **Button**:
  - Nút "Đăng nhập" (Kích thước lớn, màu chủ đạo hệ thống làm nổi bật).
  - Nút "Đăng ký ngay" (Dạng chữ liên kết xanh lam).
- **Form**: Biểu mẫu chứa:
  - Input Email (Định dạng văn bản).
  - Input Mật khẩu (Trường bảo mật che ký tự kèm biểu tượng hiển thị lớp).
  - Checkbox "Ghi nhớ mật khẩu".
- **Table**: Không áp dụng (N/A).
- **Footer**: Bản quyền rút gọn dạng đơn dòng nằm đáy màn hình.

---

#### 3. Màn hình: Đăng ký tài khoản (Register Screen)
- **Header**: Logo khách sạn đặt căn giữa trang.
- **Sidebar**: Không áp dụng (N/A).
- **Content**: Card đăng ký kích thước lớn hơn form đăng nhập, căn giữa trang, chứa các trường điền hồ sơ dữ liệu cá nhân chi tiết.
- **Button**:
  - Nút xác nhận "Tạo tài khoản" (Kích thước lớn, màu chủ đạo).
  - Nút "Trở lại Đăng nhập" (Dạng link liên kết ở cuối).
- **Form**: Biểu mẫu đăng ký bao gồm các trường:
  - Input Họ tên, Email, Số điện thoại.
  - Input Số căn cước công dân hoặc Hộ chiếu.
  - Input Mật khẩu và Xác nhận lại mật khẩu.
  - Checkbox "Đồng ý với các điều khoản bảo mật dịch vụ".
- **Table**: Không áp dụng (N/A).
- **Footer**: Bản quyền rút gọn đơn dòng dưới đáy trang.

---

#### 4. Màn hình: Chi tiết sản phẩm loại phòng (Room List Screen)
- **Header**: Kế thừa đầy đủ cấu trúc của trang chủ (Logo, Menu đi kèm Avatar người dùng).
- **Sidebar**: Bộ lọc lọc phòng nâng cao (Filter panel) nằm ở cột bên trái:
  - Thanh trượt khoảng giá thuê ngày (Range Slider).
  - Bộ chọn loại phòng (VIP, Deluxe, Standard).
  - Các tiện ích mong muốn (Wifi, Bể bơi, Ăn sáng).
- **Content**:
  - Tiêu đề danh sách buồng phòng.
  - Lưới hiển thị các thẻ phòng khả dụng (Room Card Grid). Mỗi thẻ gồm: Ảnh đại diện phòng, Tên phòng, Sức chứa tối đa, Đơn giá thuê và mô tả ngắn.
- **Button**:
  - Nút "Áp dụng bộ lọc" (Cuối Sidebar).
  - Nút "Đặt phòng ngay" (Nổi bật trên từng Card phòng).
  - Nút "Chi tiết phòng" (Dạng viền text trên Card).
- **Form**: Bộ điều khiển lọc phòng ở cột Sidebar.
- **Table**: Không áp dụng (N/A).
- **Footer**: Đầy đủ chân trang gồm thông tin liên hệ và liên kết hệ thống.

---

#### 5. Màn hình: Chi tiết buồng phòng (Room Detail Screen)
- **Header**: Đồng bộ đầy đủ cấu trúc chung.
- **Sidebar**: Khung Widget đặt phòng nhanh (Sticky Reservation Widget) nằm dọc cột bên phải:
  - Hiển thị tóm tắt đơn giá phòng/ngày.
  - Hai ô nhập ngày đi/đến.
  - Thống kê tạm tính tổng tiền phòng.
- **Content**:
  - Tên hạng phòng tiêu đề lớn.
  - Album trưng bày hình ảnh buồng phòng 2 hàng lớn.
  - Mục mô tả văn bản nội dung chi tiết diện tích phòng và trang bị tiện nghi.
  - Khối danh sách các bình luận chấm điểm (Reviews) của khách lưu trú trước.
- **Button**:
  - Nút "Xác nhận gửi yêu cầu giữ phòng" (Màu đỏ/cam nổi bật đặt trong Sticky Widget).
  - Nút "Gửi bình luận đánh giá" (Đặt sau biểu mẫu viết đánh giá).
- **Form**: Biểu mẫu nhập bình luận (Chọn các mức sao 1-5, Nhập phản hồi dạng TextArea).
- **Table**: Bảng biểu kê chi tiết thông số phòng (Diện tích m2, Loại giường ngủ, Góc nhìn hướng phòng, Số người mặc định).
- **Footer**: Đầy đủ chân trang.

---

#### 6. Màn hình: Lập tiến trình đơn phòng (Booking Screen)
- **Header**: Giống các trang khác, tích hợp thanh định vị bước đặt chỗ (Stepper: 1. Thông tin khách -> 2. Thanh toán -> 3. Hoàn tất).
- **Sidebar**: Không áp dụng (N/A).
- **Content**: Bố cục chia dọc thành 2 phần chính:
  - Phần trung tâm (Trái): Biểu mẫu điền hồ sơ chi tiết hành trình lưu trú cùng số CCCD/Passport của các khách cư trú cùng phòng.
  - Phần tóm tắt (Phải): Khung hiển thị hóa đơn tạm tính gồm: Ảnh nhỏ phòng, Ngày nhận/trả phòng, Số đêm thuê, Giá phòng gốc và Thuế phí phát sinh.
- **Button**:
  - Nút "Quay lại" (Dạng chữ mờ).
  - Nút "Xác nhận & Đi tới thanh toán" (Màu cam sáng kích thước lớn).
- **Form**: Biểu mẫu điền thông tin khách gồm: Nhập tên tài khoản đại diện, Số điện thoại hỗ trợ liên lạc, Trường khai báo CCCD từng khách đi kèm và Trường Ghi chú yêu cầu thêm cho khách sạn.
- **Table**: Không áp dụng (N/A).
- **Footer**: Rút gọn bản quyền.

---

#### 7. Màn hình: Kết nối thanh toán (Payment Screen)
- **Header**: Tiêu đề trang thanh toán đi kèm Stepper ở bước 2: Thanh toán.
- **Sidebar**: Không áp dụng (N/A).
- **Content**:
  - Hộp thông báo thời gian bảo lưu phòng kèm bộ đếm ngược 24 giờ.
  - Thẻ thông tin tài khoản chuyển khoản cọc (Số tài khoản ngân hàng, Tên chủ thụ hưởng, Nội dung chuyển khoản tự động hóa).
  - Ảnh mã QR liên kết thanh toán nhanh (VietQR) chứa số tiền cọc chuẩn hóa.
- **Button**:
  - Nút "Đã hoàn thành chuyển khoản" (Nổi bật đậm màu).
  - Nút "Hủy đơn đặt phòng" (Dạng chữ xám cảnh báo phạt hủy cọc).
- **Form**: Nhóm lựa chọn cổng kết nối thanh toán (Chuyển khoản QR ngân hàng hoặc Khai báo thẻ quốc tế).
- **Table**: Bảng phân bổ cơ cấu dòng tiền thanh toán (Tiền cọc giữ chỗ 30%, Tiền phòng còn lại cần trả khi check-in, Thuế VAT).
- **Footer**: Rút gọn bản quyền.

---

#### 8. Màn hình: Lịch sử giao dịch cá nhân (Booking History Screen)
- **Header**: Đồng bộ đầy đủ cấu trúc chung.
- **Sidebar**: Menu dọc quản trị cá nhân (Hồ sơ của tôi, Đổi mật khẩu tài khoản, Lịch sử đặt phòng, Quản lý điểm thưởng).
- **Content**:
  - Tiêu đề danh mục lịch sử đặt chỗ.
  - Nhóm tab phân loại trạng thái đơn (Tất cả, Đang chờ, Đã xác nhận, Đã ở xong, Đã hủy).
  - Bảng thống kê toàn bộ lịch sử đơn đặt chỗ.
- **Button**:
  - Nút "Hủy đặt phòng" (Hiển thị chỉ đối với đơn CONFIRMED trước 48 giờ).
  - Nút "Xem hóa đơn PDF" (Dành cho đơn CheckedOut/Completed).
  - Nút "Viết đánh giá" (Đối với đơn mới check-out chưa nhận xét).
- **Form**: Không áp dụng (N/A).
- **Table**: Bảng danh sách đơn đặt phòng gồm: (Mã đặt chỗ, Loại phòng, Ngày Check-in, Ngày Check-out, Tổng tiền thực trả, Trạng thái đơn, Cột hành động).
- **Footer**: Đầy đủ chân trang.

---

#### 9. Màn hình: Hồ sơ cá nhân (Profile Screen)
- **Header**: Đồng bộ đầy đủ cấu trúc chung.
- **Sidebar**: Menu cấu trúc quản lý cá nhân giống màn lịch sử đặt chỗ.
- **Content**:
  - Tiêu đề màn hình cập nhật hồ sơ cá nhân.
  - Phân vùng cấu hình Avatar tài khoản người dùng bên trái.
  - Khối biểu mẫu cập nhật thông tin cá nhân bên phải.
- **Button**:
  - Nút "Cập nhật thay đổi" (Nút lớn màu xanh lá/chủ đạo).
  - Nút "Hủy bỏ sửa đổi" (Màu xám nhạt).
- **Form**: Biểu mẫu cập nhật gồm: Họ tên, Email (Lock sửa), Số điện thoại, Ngày sinh, Số định danh cá nhân CCCD.
- **Table**: Không áp dụng (N/A).
- **Footer**: Đầy đủ chân trang.

---

### PHÂN HỆ 2: VẬN HÀNH VÀ QUẢN TRỊ NỘI BỘ (PMS STAFF & ADMIN PORTAL)

#### 10. Màn hình: Báo cáo chỉ số vận hành (Dashboard Screen)
- **Header**:
  - Trái: Menu Hamburger thu gọn Sidebar.
  - Giữa: Thanh hiển thị tên phần mềm PMS và tên Chi nhánh khách sạn.
  - Phải: Nhập ô tìm kiếm chung, Avatar nhân viên kết hợp nút Đăng xuất.
- **Sidebar**: Thanh menu điều hướng quản trị dọc:
  - Menu: Dashboard, Sơ đồ lưới buồng phòng, Quản lý Booking, Quản lý Khách hàng, Quản lý Nhân sự, Cấu hình phòng, Báo cáo tài chính.
- **Content**:
  - Lưới hiển thị 4 Card thống kê nhanh (Doanh thu ngày, Tỷ lệ lấp đầy %, Check-in dự kiến, Lượt dọn dẹp cần làm).
  - Khối Biểu đồ (Biểu đồ đường tiến trình doanh thu tuần, biểu đồ cột cột so sánh hiệu suất phòng).
  - Bảng tổng hợp cácBooking mới cập nhật gần đây phần chân trang.
- **Button**:
  - Nút "Xuất dữ liệu Excel" báo cáo ngày làm việc.
- **Form**: Không áp dụng (N/A).
- **Table**: Bảng hiển thị 5 lượt đặt chỗ mới phát sinh (Mã đặt phòng, Tên khách, Loại phòng chỉ định, Giá trị đơn, Thời điểm đặt).
- **Footer**: Hiển thị đơn dòng phiên bản máy chủ phần mềm PMS (Ví dụ: v1.0.3 - Stable).

---

#### 11. Màn hình: Danh sách khách hàng (User Management Screen)
- **Header**: Đồng bộ hệ thống Dashboard nội bộ.
- **Sidebar**: Menu dọc quản trị hệ thống.
- **Content**:
  - Tiêu đề chức năng "Quản trị Cơ sở dữ liệu Khách hàng".
  - Trường tìm kiếm và bộ lọc trạng thái tài khoản.
  - Bảng hiển thị thông tin hồ sơ khách lưu trú.
- **Button**:
  - Nút "Xuất danh bạ khách hàng" (Tải file CSV).
  - Nút "Khóa hoạt động tài khoản khách" (Nút cảnh báo đỏ).
  - Nút "Sửa thông tin cơ bản" (Nút nhỏ trên dòng bảng).
- **Form**: Form tìm kiếm khách hàng bằng các ô lọc tiêu chuẩn (Nhập tên, số điện thoại hoặc email).
- **Table**: Bảng dữ liệu khách hàng (Mã khách hàng, Tên khách hàng, Email liên hệ, Điện thoại, CCCD/Passport, Điểm loyalty tích lũy, Trạng thái thẻ, Cột hành động).
- **Footer**: Bản quyền hệ thống rút gọn.

---

#### 12. Màn hình: Quản lý thiết lập buồng phòng vật lý (Room Management Screen)
- **Header**: Đồng bộ hệ thống Dashboard nội bộ.
- **Sidebar**: Menu dọc quản trị hệ thống.
- **Content**:
  - Tiêu đề trang thiết lập "Quản lý buồng phòng vật lý".
  - Thanh chọn bộ lọc lọc buồng phòng (Lọc theo tầng lầu, lọc trạng thái Clean, Dirty, Maintenance).
  - Bảng dữ liệu buồng phòng vật lý.
- **Button**:
  - Nút "Thêm phòng nghỉ mới" (Màu xanh chủ đạo nổi bật góc phải).
  - Nút "Thay đổi trạng thái buồng phòng" (Chuyển đổi Clean/Dirty nhanh).
  - Nút "Sửa cấu hình" và nút "Xóa phòng vật lý khỏi cơ sở dữ liệu".
- **Form**: Biểu mẫu tạo mới/sửa đổi phòng nghỉ vật lý (Nhập số phòng, chọn số tầng phân cấp, chọn Khóa loại phòng liên kết, chọn trạng thái ban đầu).
- **Table**: Bảng quản trị buồng phòng vật lý gồm các trường (STT, Số phòng nghỉ, Tầng lầu, Phân hạng loại phòng, Trạng thái buồng phòng, Cột Hành động).
- **Footer**: Bản quyền hệ thống rút gọn.

---

#### 13. Màn hình: Quản lý đơn đặt phòng tổng thể (Booking Management Screen)
- **Header**: Đồng bộ hệ thống Dashboard nội bộ.
- **Sidebar**: Menu dọc quản trị hệ thống.
- **Content**:
  - Tiêu đề nghiệp vụ quản trị "Danh sách đơn đặt trước".
  - Phân vùng bộ lọc nâng cao (Truy lục đơn đặt theo ngày đến, theo email khách đính kèm).
  - Bảng danh mục các Booking.
- **Button**:
  - Nút "Check-in lễ tân" (Hiện nổi bật bật khi khách đến đúng lịch).
  - Nút "Hủy đơn gán cọc" (Hỗ trợ nghiệp vụ).
  - Nút "Xem chi tiết biên nhận" (Khởi tạo mở cửa sổ modal popup).
- **Form**: Bộ điều khiển tìm kiếm booking theo các từ khóa.
- **Table**: Bảng dữ liệu Booking toàn hệ thống bao gồm (STT, Mã đặt phòng, Tên khách đặt, Ngày đến dự kiến, Ngày đi dự kiến, Tiền cọc thanh toán, Trạng thái Booking, Chọn thao tác xử lý).
- **Footer**: Bản quyền hệ thống rút gọn.

---

#### 14. Màn hình: Quản trị tài khoản nhân sự (Employee Management Screen)
- **Header**: Đồng bộ hệ thống Dashboard nội bộ.
- **Sidebar**: Menu dọc quản trị hệ thống.
- **Content**:
  - Tiêu đề nghiệp vụ "Quản lý tài khoản nhân viên & ca trực".
  - Thanh tìm kiếm mã định danh nhân sự khách sạn.
  - Bảng danh mục nhân viên khách sạn.
- **Button**:
  - Nút "Thêm mới tài khoản nhân viên" (Góc phải màn hình).
  - Nút "Chỉnh sửa phân quyền Role" (Mở modal cấu hình vai trò).
  - Nút "Đóng băng tài khoản nhân viên" (Xử lý nhân viên nghỉ việc).
- **Form**: Biêu mẫu đăng ký thông tin nhân viên (Họ tên, Mã nhân viên định dạng Regex riêng, Email cơ quan, Mật khẩu khởi tạo, Số điện thoại, Phân cấp mức lương cơ bản, Nhóm phân quyền Role chọn từ Combobox).
- **Table**: Bảng nhân viên (STT, Mã số nhân sự, Tên nhân viên, Chức vị công tác, Email công việc, Số điện thoại liên hệ, Phân quyền vai trò hệ thống, Trạng thái tài khoản, Cột hành động).
- **Footer**: Bản quyền hệ thống rút gọn.

---

#### 15. Màn hình: Báo cáo tài chính doanh thu (Revenue Report Screen)
- **Header**: Đồng bộ hệ thống Dashboard nội bộ.
- **Sidebar**: Menu dọc quản trị hệ thống.
- **Content**:
  - Tiêu đề báo cáo "Thống kê doanh thu quyết toán & dòng tiền".
  - Phân vùng cấu hình biểu thời gian (Date Range Picker: Từ ngày -> Đến ngày).
  - 3 Thẻ số liệu đo lường cụ thể (Doanh thu tiền phòng nghỉ, Doanh thu từ dịch vụ ăn uống & minibar phát sinh, Tổng trả cọc hủy đơn phạt cọc).
  - Biểu đồ phối tổng hợp luồng doanh thu theo tháng dạng biểu đồ cột chồng.
  - Bảng kê danh sách hóa đơn chi tiết tài chính đã chốt sổ.
- **Button**:
  - Nút "Thực thi kết xuất báo cáo" (Để hiển thị dữ liệu biểu đồ).
  - Nút "Tải báo cáo PDF hoàn chỉnh" hoặc "Xuất tệp CSV / Excel".
- **Form**: Form cấu hình thời gian và xuất dữ liệu báo cáo.
- **Table**: Bảng dữ liệu hóa đơn quyết toán (Mã hóa đơn, Ngày xuất bản ghi, Doanh thu phòng, Doanh thu dịch vụ nợ kèm phòng, Chiết khấu giảm trừ giá phòng, Tiền VAT, Tổng số tiền thực thu, Nhân viên xử lý thu).
- **Footer**: Bản quyền hệ thống rút gọn.

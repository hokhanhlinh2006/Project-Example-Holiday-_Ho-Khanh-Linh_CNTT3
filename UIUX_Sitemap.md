# SƠ ĐỒ CẤU TRÚC TRANG TIẾP CẬN HỆ THỐNG
## DỰ ÁN: HỆ THỐNG QUẢN LÝ KHÁCH SẠN (HOTEL MANAGEMENT SYSTEM)

### PHẦN XII: SƠ ĐỒ SITE MAP HỆ THỐNG (SYSTEM SITE MAP)

Hồ sơ site map phân tách cấu trúc màn hình (UI Layout) thành 2 phân hệ người dùng chính: Cổng thông tin Khách hàng trực tuyến (Customer Portal) và Trang Quản trị Điều động Vận hành nội bộ (Staff & Admin PMS Portal). Sơ đồ được xây dựng theo định dạng hình cây phân cấp logic nghiệp vụ.

---

#### 1. Phân hệ Khách hàng (Customer Portal - Web/Mobile Responsive)

Phân hệ dành cho khách hàng tìm kiếm phòng, tạo lập đơn đặt chỗ và thực thanh khoản nợ trực tuyến:

```
Trang chủ (Home - Cổng thông tin công cộng)
 ├── Đăng nhập tài khoản (Login Page)
 ├── Đăng ký thành viên (Register Page)
 ├── Khôi phục tài khoản (Forgot Password)
 ├── Danh mục tra cứu phòng trực tuyến (Search & Filter Room Types)
 │    └── Chi tiết loại phòng (Room Type Details View)
 │         └── Thiết lập đơn đặt chỗ (Reservation Checkout Form)
 ├── Kết cấu cổng thanh toán API (Payment Gateway Redirect)
 │    └── Kết quả giao dịch thanh toán (Transaction Status Banner UI)
 ├── Trang cá nhân thành viên (Customer Profile Dashboard)
 │    ├── Hồ sơ cá nhân (Personal Information Details)
 │    ├── Đổi mật khẩu tài khoản (Change Password Page)
 │    ├── Lịch sử giao dịch lưu trú (My Bookings History)
 │    │    └── Chi tiết hóa đơn thanh toán (Invoice Detail View)
 │    │         └── Viết phản hồi đánh giá (Submit Review Form)
 │    └── Ví điểm thưởng tích lũy (Loyalty Points Wallet)
 └── Giới thiệu & Liên hệ khách sạn (About & Contact Us)
```

---

#### 2. Phân hệ Vận hành & Quản trị (Staff & Admin PMS Portal)

Phân hệ dành riêng cho bộ phận Tiếp tân trực ca, Kế toán hóa đơn, Buồng phòng và Quản trị viên điều hành tĩnh:

```
Cổng đăng nhập quản trị nội bộ (Staff Login Portal)
 ├── Bảng dữ liệu tổng quan (Admin Dashboard)
 │    ├── Biểu đồ trực quan doanh thu (Revenue Metrics Charts)
 │    ├── Phân tích tỷ lệ lấp đầy phòng (Occupancy Rates Analysis)
 │    └── Chỉ số KPI ca trực tiếp tân (Receptionist Operations Overview)
 ├── Sơ đồ lưới buồng phòng (Room Grid Console - Thời gian thực)
 │    ├── Biên bản sơ bộ phòng vật lý (Physical Room Status Inspector)
 │    │    ├── Lập nhanh Check-in (Quick Check-in Form)
 │    │    └── Lập nhanh Check-out (Quick Check-out)
 │    └── Thiết lập điều chuyển buồng phòng (Transfer Room Form)
 ├── Quản lý đặt phòng (Booking Management Directory)
 │    ├── Danh sách đơn đặt chỗ toàn phần (Total Booking List Ledger)
 │    │    └── Thông tin chi tiết đơn lưu giữ (Booking Invoice Details View)
 │    └── Lập đơn đặt phòng tại quầy (Create Walk-in Booking Form)
 ├── Quản lý dịch vụ tiêu thụ (Room Services Logging console)
 │    ├── Ghi nhận tiêu thụ đồ uống mini-bar (Log Mini-bar Consumption Form)
 │    └── Thu mục hàng hóa dịch vụ (Services Master Catalog)
 ├── Danh bạ khách hàng (Customer Database Panel)
 │    └── Hồ sơ & Nhật ký giao dịch khách lưu trú (Customer Profile & Booking Logs)
 ├── Quản lý nhân sự làm việc (Employee Directory Grid)
 │    ├── Khai báo hồ sơ nhân sự mới (Register Employee Form)
 │    └── Phân quyền & Quản lý ca trực (Shift Setup & Role Access RBAC Panel)
 ├── Khai báo cấu trúc buồng phòng (Hotel Rooms Inventory Config)
 │    ├── Danh mục các phòng vật lý (Physical Rooms Manager Table)
 │    └── Cấu hình đặc tả loại phòng (Room Types Configuration Panel)
 ├── Quản trị dòng tiền thanh toán (Invoice & Payment Billing Center)
 │    ├── Danh sách chứng từ hóa đơn (Tax Invoices Ledger)
 │    └── Nhật ký tài khoản giao dịch (Bank & Cash Transactions Log)
 └── Cấu hình thông số hệ thống (Master System Control Panel)
      ├── Tích hợp tham số khóa bảo mật API (Payment Gateway API Credentials)
      ├── Khai phá vết kiểm toán hệ thống (System Security Audit Trail Logs)
      └── Công cụ sao lưu database (Database Backup & Restore Wizard Console)
```

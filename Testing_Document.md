# FILE: Testing_Plan.md

# KẾ HOẠCH KIỂM THỬ PHẦN MỀM (SOFTWARE TESTING PLAN)
## DỰ ÁN: HOTEL MANAGEMENT SYSTEM (PROJECT EXAMPLE HOLIDAY)
**Tác giả:** QA Lead  
**Tiêu chuẩn áp dụng:** ISTQB (International Software Testing Qualifications Board) Standard  
**Môi trường thử nghiệm:** Staging & Sandbox Environment

---

Tài liệu này đặc tả Kế hoạch kiểm thử toàn diện cho Hệ thống Quản lý Khách sạn nhằm kiểm soát chặt chẽ chất lượng sản phẩm trước khi chuyển giao bàn giao, đảm bảo phần mềm hoạt động ổn định, chính xác nghiệp vụ và có tính bảo mật thông tin cao.

---

## 1. UNIT TEST (KIỂM THỬ ĐƠN VỊ)

### 1.1. Mục tiêu (Objective)
Kiểm tra tính đúng đắn và độc lập của các đơn vị mã nguồn nhỏ nhất (hàm, phương thức, class) trong mã nguồn ứng dụng Java Spring Boot và ReactJS, cách ly hoàn toàn với các nhân tố bên ngoài (Database, Network).

### 1.2. Phạm vi (Scope)
*   **Backend:** Các lớp xử lý logic Service (ví dụ: tính toán thuế VAT hóa đơn, tính phụ thu đổi giờ checkout), các lớp xử lý DTO Validation mapping, Helpers, Util classes.
*   **Frontend:** Giải thuật xử lý chuỗi, định dạng ngày tháng hiển thị, các hàm xử lý tính toán giá phòng tạm tính ở Client.

### 1.3. Công cụ sử dụng (Tools)
*   **Backend:** JUnit 5, Mockito (dùng mock dữ liệu Database), AssertJ.
*   **Frontend:** Jest, React Testing Library.

### 1.4. Quy trình thực hiện (Process)
1.  **Phát triển test script:** Lập trình viên tự viết TestCase trong quá trình code (tiếp cận TDD hoặc viết Test song song khi code).
2.  **Mocking:** Sử dụng Mockito để giả lập dữ liệu trả về từ Repository DB nhằm tập trung kiểm tra logic Service.
3.  **Chạy kiểm thử:** Chạy lệnh quét Unit Test trên IDE hoặc thông qua CI/CD Pipeline khi có hành vi tạo Pull Request.
4.  **Đo lường độ bao phủ:** Sử dụng công cụ Jacoco để kiểm soát tỷ lệ bao phủ dòng code (Code Coverage).

### 1.5. Kết quả mong đợi (Expected Result)
*   100% các hàm nghiệp vụ chính được kiểm thử chạy qua, không xuất hiện các lỗi NullPointerException hoặc tràn số.
*   Tỷ lệ bao phủ mã nguồn (Code Coverage) tối thiểu đạt **80%** tổng số dòng code.

---

## 2. INTEGRATION TEST (KIỂM THỬ TÍCH HỢP)

### 2.1. Mục tiêu (Objective)
Xác minh sự giao tiếp tương tác giữa các module bên trong hệ thống với nhau và với các thành phần dịch vụ bên ngoài (Database, Caching Server, Mail Server) hoạt động trơn tru, không xảy ra xung đột dữ liệu.

### 2.2. Phạm vi (Scope)
*   Sự phối hợp giữa Spring Data JPA Entity và Database MySQL thực tế.
*   Sự phối hợp giữa Security Filter Chain Jwt Filter và phân quyền Controller.
*   Các API Endpoints (tương tác từ HTTP Request cho đến Response đầu ra thực tế của Database - API Integration Test).
*   Giao tiếp đồng bộ dữ liệu giữa Application Server và In-memory Redis Cache.

### 2.3. Công cụ sử dụng (Tools)
*   Spring Boot Test (`@SpringBootTest`), Testcontainers (giả lập Docker MySQL/Redis trong lúc test), MockMvc (giả lập gọi HTTP API), RestAssured.

### 2.4. Quy trình thực hiện (Process)
1.  **Thiết lập DB Testing:** Cấu hình một cơ sở dữ liệu ảo (H2 Database hoặc Testcontainers chạy MySQL thực tế) riêng để đảm bảo dữ liệu test không ảnh hưởng DB thật.
2.  **Khởi tạo MockMvc/RestAssured:** Giả lập các cuộc gọi REST API thực tế gửi dữ liệu DTO JSON đến các Endpoint.
3.  **Verify kết quả DB:** So sánh dữ liệu trả về trong HTTP Body và dữ liệu thật được ghi/xoá dưới Database để xác minh Transaction (`@Transactional` rollback sau mỗi test case).

### 2.5. Kết quả mong đợi (Expected Result)
*   Tất cả các API Endpoints trả về đúng HTTP Status Code đã thiết kế (Ví dụ: 201 Created khi tạo đặt phòng, 400 Bad Request khi sai định dạng email).
*   Dữ liệu được lưu trữ xuống Database chính xác theo đúng cấu trúc ràng buộc quan hệ đo lường thực tế.

---

## 3. SYSTEM TEST (KIỂM THỬ HỆ THỐNG)

### 3.1. Mục tiêu (Objective)
Đánh giá hoạt động của phần mềm khách sạn dưới góc nhìn một hệ thống hoàn chỉnh đã được đóng gói tích hợp đầy đủ cả Backend, Frontend và Database, bảo đảm đáp ứng trọn vẹn Đặc tả yêu cầu phần mềm (SRS).

### 3.2. Phạm vi (Scope)
*   Kiểm thử chức năng End-to-End (E2E) các luồng quy trình nghiệp vụ cốt lõi:
    1. *Khách hàng*: Tìm phòng -> Đặt phòng -> Nhận thư xác nhận -> Viết đánh giá sau ở.
    2. *Lễ tân*: Tiếp nhận đặt phòng -> Đổi phòng cho khách -> Thực hiện Check-in -> Thêm dịch vụ mini-bar -> Check-out -> Xuất hoá đơn tính tiền -> Đóng ca.
    3. *Admin*: Thêm tài khoản nhân sự -> Sửa đổi giá buồng phòng theo mùa -> Kiểm tra biểu đồ KPI doanh số.

### 3.3. Công cụ sử dụng (Tools)
*   Selenium WebDriver, Playwright hoặc Cypress (cho E2E Test tự động hóa).
*   Postman (kiểm thử thủ công các kịch bản API).

### 3.4. Quy trình thực hiện (Process)
1.  **Thiết kế Test Cases:** Xây dựng kịch bản kiểm thử bao gồm cả trường hợp tích cực (Positive Cases) và tiêu cực (Negative Cases) bám sát tài liệu SRS.
2.  **Thiết lập Staging Environment:** Triển khai bản dựng Frontend và Backend thực tế lên máy chủ kiểm thử.
3.  **Thực thi kiểm thử:** QA chạy kiểm thử thủ công (Manual Test UI) hoặc chạy script E2E qua Playwright.
4.  **Báo cáo lỗi (Bug Report):** Ghi nhận lỗi lên hệ thống quản lý (như Jira/Trello), phân loại mức độ nghiêm trọng (Severity) và độ ưu tiên sửa (Priority).

### 3.5. Kết quả mong đợi (Expected Result)
*   Mọi tính năng hoạt động đúng theo tài liệu đặc tả SRS, không phát sinh lỗi chặn luồng (Blocker/Critical) trên giao diện.
*   Giao diện hiển thị nhất quán trên các trình duyệt phổ biến (Chrome, Edge, Safari).

---

## 4. USER ACCEPTANCE TEST (KIỂM THỬ CHẤP NHẬN NGƯỜI DÙNG - UAT)

### 4.1. Mục tiêu (Objective)
Đảm bảo phần mềm khách sạn đáp ứng đúng mong đợi, nhu cầu thực tế của người dùng cuối (chủ khách sạn, quản lý, lễ tân, buồng phòng và hàng khách thực tế) trước khi đưa vào vận hành dòng tiền thật.

### 4.2. Phạm vi (Scope)
Kiểm tra tính thân thiện của giao diện và mức độ trực quan khi sử dụng (Usability), độ chính xác của tài chính hóa đơn cùng tính thực tiễn của quy trình vận hành khách sạn.

### 4.3. Công cụ sử dụng (Tools)
*   UAT Test Cases Sheets, Google Forms (thu thập phiếu khảo sát phản hồi), công cụ ghi màn hình hành vi (Hotjar).

### 4.4. Quy trình thực hiện (Process)
1.  **Lựa chọn đối tượng:** Triệu tập một nhóm khách hàng thực tế và nhân viên khách sạn (lễ tân, kế toán) để trực tiếp sử dụng hệ thống.
2.  **Cung cấp Kịch bản UAT:** Cấp tài khoản thử nghiệm và đưa ra các nhiệm vụ thực tế (Ví dụ: "Hãy đặt phòng suite cho gia đình 4 người từ ngày 15 đến 18 và thanh toán bằng chuyển khoản").
3.  **Giám sát và Hỗ trợ:** Đội QA quan sát hành vi, ghi nhận những điểm người dùng gặp bối rối hoặc thao tác sai.
4.  **Nghiệm thu:** Thu thập phiếu đánh giá và chữ ký xác nhận đạt chuẩn (UAT Sign-off).

### 4.5. Kết quả mong đợi (Expected Result)
*   Người dùng vận hành quy trình trơn tru, không gặp lỗi nghiệp vụ nghiêm trọng.
*   Chỉ số hài lòng Usability đạt tối thiểu **4/5 sao** hoặc **85%** phản hồi tích cực.

---

## 5. REGRESSION TEST (KIỂM THỬ HỒI QUY)

### 5.1. Mục tiêu (Objective)
Đảm bảo các sửa lỗi mới (Bug Fixes), tính năng mới bổ sung (New Features) hoặc các cập nhật hệ thống không làm hỏng hoặc gây lỗi cho các vùng chức năng đang hoạt động ổn định trước đó.

### 5.2. Phạm vi (Scope)
Quét lại toàn bộ các luồng chức năng quan trọng (Đăng nhập, Đặt phòng, Xuất hoá đơn) sau mỗi lần cập nhật bản dựng phần mềm mới (New Build/Release).

### 5.3. Công cụ sử dụng (Tools)
*   Playwright, Selenium (chạy tự động suite Test Regression tự sinh bảo đảm rút ngắn thời gian test).

### 5.4. Quy trình thực hiện (Process)
1.  **Xác định vùng ảnh hưởng:** QA Lead phân tích thay đổi mã nguồn để xác định các tính năng phụ thuộc liên đới.
2.  **Chọn lọc test case:** Lựa chọn bộ Test Suite kiểm thử hồi quy bao gồm các chức năng cốt lõi.
3.  **Chạy suite kiểm thử:** Thực thi tự động hệ thống test để so khớp kết quả hiển thị trước và sau khi đổi Code.

### 5.5. Kết quả mong đợi (Expected Result)
*   Mã phản hồi và dòng dữ liệu ở các API và màn hình cũ không bị thay đổi hành vi hoạt động ngoài ý muốn.
*   **0%** lỗi hồi quy xuất hiện trên các tính năng cốt lõi.

---

## 6. PERFORMANCE TEST (KIỂM THỬ HIỆU NĂNG)

### 6.1. Mục tiêu (Objective)
Đánh giá hiệu suất vận hành của hệ thống, đo lường thời gian phản hồi API, khả năng chịu tải và độ ổn định dưới áp lực số lượng lớn khách hàng truy cập đặt phòng cùng lúc (ví dụ dịp lễ Tết).

### 6.2. Phạm vi (Scope)
*   **Load Testing:** Kiểm tra hệ thống chịu tải thông thường (500 người dùng đồng thời).
*   **Stress Testing:** Kiểm tra giới hạn bền cực đại của hệ thống (2000 - 5000 người dùng đồng thời bấm đặt phòng liên tục) để xác định điểm gãy nghẽn cổ chai mạng/CPU.

### 6.3. Công cụ sử dụng (Tools)
*   Apache JMeter, k6 (viết script test tải bằng JavaScript).

### 6.4. Quy trình thực hiện (Process)
1.  **Thiết kế Kịch bản Tải:** Mô phỏng luồng hành vi (Đọc phòng -> Lọc dịch vụ -> Đặt phòng) với vòng lặp tăng dần số lượng người dùng ảo (Virtual Users - VUs).
2.  **Thiết lập Môi trường Test:** Chạy test hiệu năng trên môi trường Staging có phần cứng tiệm cận môi trường chạy thật.
3.  **Kích hoạt Tải và Giám sát:** Đẩy tải tăng dần trong thời gian quy định (10-30 phút). Theo dõi tài nguyên hệ thống (CPU máy chủ, dung lượng RAM, dung lượng kết nối Connection Pool MySQL).
4.  **Phân tích kết quả:** Đo lường thời gian phản hồi trung bình (Response Time), tỷ lệ lỗi nhận về (Error Rate %).

### 6.5. Kết quả mong đợi (Expected Result)
*   Thời gian phản hồi API trung bình đạt dưới **1.5 giây** đối với tải thông thường.
*   Tỷ lệ lỗi (Error Rate) dưới **1%** dưới ngưỡng chịu tải tối đa định mức.
*   Hệ thống không bị treo hoặc rò rỉ bộ nhớ (Memory Leak).

---

## 7. SECURITY TEST (KIỂM THỬ BẢO MẬT)

### 7.1. Mục tiêu (Objective)
Phát hiện các lỗ hổng bảo mật, nguy cơ tấn công mạng tiềm ẩn cấu trúc của hệ thống, bảo vệ tuyệt đối cơ sở dữ liệu khách sạn, thông tin cá nhân của khách và các giao dịch chuyển tiền.

### 7.2. Phạm vi (Scope)
*   Kiểm tra các cơ chế mã hoá an toàn dữ liệu nhạy cảm (mật khẩu dùng BCrypt, thông tin thẻ tín dụng của khách).
*   Đánh giá lỗ hổng bảo mật tiêu chuẩn **OWASP Top 10** (SQL Injection, Cross-Site Scripting - XSS, Broken Authentication, Broken Object Level Authorization).
*   Kiểm tra an ninh phân quyền JWT (Bypass token, sửa đổi payload giả danh ADMIN).

### 7.3. Công cụ sử dụng (Tools)
*   OWASP ZAP, SonarQube (quét lỗi bảo mật tĩnh trong mã nguồn SAST), Postman (kiểm thử cướp quyền API).

### 7.4. Quy trình thực hiện (Process)
1.  **Static Code Analysis:** Sử dụng SonarQube quét mã nguồn nhằm dò tìm các đoạn code lỗi cấu trúc có thể nguy hại an ninh bảo mật.
2.  **API Penetration Testing:** Tạo các request sửa đổi JWT Token Payload đổi role, gửi request không kèm Authorization xem API có khoá chặn an toàn không.
3.  **Vulnerability Scanning:** Sử dụng OWASP ZAP quét tự động rà soát các cổng an ninh của Web Server tìm lỗ hổng XSS, Injection phổ thông.

### 7.5. Kết quả mong đợi (Expected Result)
*   Không chứa lỗ hổng bảo mật mức độ Nghiêm trọng (High/Critical) theo thang đánh giá chuẩn an ninh.
*   Mọi API đặc quyền ADMIN/RECEPTIONIST được chặn đứng tuyệt đối nếu request sử dụng token giả lập hoặc hạ cấp phân quyền.
*   Dữ liệu mật khẩu lưu trữ trong Database được băm mã hoá hoàn toàn, không thể đọc thô.


---


# FILE: QA_Test_Cases.md

# TÀI LIỆU KỊCH BẢN KIỂM THỬ PHẦN MỀM (TEST CASES)
## DỰ ÁN: HỆ THỐNG QUẢN LÝ KHÁCH SẠN (HOTEL MANAGEMENT SYSTEM)

### PHẦN XVII: DANH SÁCH 100 KỊCH BẢN KIỂM THỬ (QA CHECKLIST)

Tài liệu này bao gồm 100 kịch bản kiểm thử (Test Cases) chi tiết được phân bổ trên 7 phân hệ nghiệp vụ chính của hệ thống. Tài liệu được cấu trúc dưới dạng bảng kiểm thử kỹ thuật phục vụ giai đoạn đảm bảo chất lượng (QA/QC).

---

### PHÂN HỆ 1: AUTHENTICATION (XÁC THỰC DANH TÍNH - 15 TEST CASES)

| ID | Feature | Test Scenario | Test Steps | Expected Result | Actual Result | Status | Priority |
|---|---|---|---|---|---|---|---|
| TC-AUTH-001 | Đăng ký | Đăng ký tài khoản mới với thông tin hợp lệ | 1. Truy cập trang Đăng ký.<br>2. Nhập Email, Password, Số điện thoại, CCCD hợp lệ.<br>3. Bấm "Tạo tài khoản". | Hệ thống lưu tài khoản thành công, gửi email kích hoạt, hiển thị thông báo thành công. | Chưa thực hiện | Ready | Cao |
| TC-AUTH-002 | Đăng ký | Đăng ký với địa chỉ Email đã tồn tại | 1. Truy cập trang Đăng ký.<br>2. Nhập Email trùng với tài khoản đã có.<br>3. Bấm "Tạo tài khoản". | Hệ thống báo lỗi trùng lặp dữ liệu email, ngăn chặn lưu bản ghi mới. | Chưa thực hiện | Ready | Cao |
| TC-AUTH-003 | Đăng ký | Đăng ký với định dạng Email sai | 1. Nhập email không có `@` hoặc thiếu phần mở rộng domain.<br>2. Bấm "Tạo tài khoản". | Hệ thống hiển thị thông điệp cảnh báo định dạng email không hợp lệ. | Chưa thực hiện | Ready | Trung bình |
| TC-AUTH-004 | Đăng ký | Đăng ký với Mật khẩu quá ngắn | 1. Nhập mật khẩu dưới 8 ký tự.<br>2. Bấm Đăng ký. | Hệ thống báo lỗi mật khẩu bắt buộc từ 8 ký tự trở lên. | Chưa thực hiện | Ready | Trung bình |
| TC-AUTH-005 | Đăng ký | Đăng ký với Mật mật khẩu thiếu chữ hoa hoặc số | 1. Nhập mật khẩu toàn chữ thường.<br>2. Bấm Đăng ký. | Hệ thống báo lỗi mật khẩu phải chứa ít nhất chữ hoa, chữ thường và chữ số. | Chưa thực hiện | Ready | Trung bình |
| TC-AUTH-006 | Đăng ký | Đăng ký với Số điện thoại Việt Nam sai định dạng | 1. Nhập SĐT chứa chữ cái hoặc ít hơn 10 số.<br>2. Bấm Đăng ký. | Hệ thống chặn đăng ký và báo lỗi số điện thoại không đúng chuẩn quốc gia. | Chưa thực hiện | Ready | Cao |
| TC-AUTH-007 | Đăng ký | Đăng ký với Số CCCD/Passport sai định dạng | 1. Nhập CCCD dài 10 chữ số (chuẩn là 12 số).<br>2. Bấm Đăng ký. | Hệ thống hiển thị thông báo lỗi số định danh cá nhân không hợp lệ. | Chưa thực hiện | Ready | Thấp |
| TC-AUTH-008 | Đăng nhập | Đăng nhập tài khoản với thông tin hợp lệ | 1. Nhập email và mật khẩu chính xác.<br>2. Bấm "Đăng nhập". | Đăng nhập thành công, điều hướng về trang Profile, lưu JWT Token. | Chưa thực hiện | Ready | Cao |
| TC-AUTH-009 | Đăng nhập | Đăng nhập với mật khẩu không chính xác | 1. Nhập đúng email, sai mật khẩu.<br>2. Bấm Đăng nhập. | Hệ thống báo lỗi thông tin đăng nhập không hợp lệ, không cấp token. | Chưa thực hiện | Ready | Cao |
| TC-AUTH-010 | Đăng nhập | Đăng nhập với tài khoản chưa kích hoạt email | 1. Nhập tài khoản ở trạng thái PENDING_ACTIVATION.<br>2. Bấm đăng nhập. | Báo lỗi tài khoản chưa kích hoạt, yêu cầu truy cập email để xác minh. | Chưa thực hiện | Ready | Trung bình |
| TC-AUTH-011 | Đăng nhập | Khóa tài khoản sau 5 lần nhập sai mật khẩu | 1. Nhập sai mật khẩu liên tiếp 5 lần cùng một email.<br>2. Nhấn đăng nhập lần thứ 6. | Tài khoản bị khóa tạm thời trong 15 phút, xuất hiện cảnh báo bị khóa. | Chưa thực hiện | Ready | Cao |
| TC-AUTH-012 | Đăng nhập | Đăng nhập khi để trống các trường | 1. Để trống email và mật khẩu.<br>2. Bấm Đăng nhập. | Hệ thống báo lỗi các trường thông tin bắt buộc không được rỗng. | Chưa thực hiện | Ready | Trung bình |
| TC-AUTH-013 | Xác thực | Truy cập trang Admin không có JWT Token | 1. Gửi request GET tới `/api/v1/dashboard/metrics` không kèm header Authorization. | HTTP Status 401 Unauthorized, chặn không cho xem dữ liệu. | Chưa thực hiện | Ready | Cao |
| TC-AUTH-014 | Phân quyền | Khách hàng cố gắng truy cập API của Admin | 1. Đăng nhập quyền CUSTOMER.<br>2. Gửi request GET tới `/api/v1/employees` kèm token vừa nhận. | HTTP Status 403 Forbidden, báo lỗi không có quyền truy cập. | Chưa thực hiện | Ready | Cao |
| TC-AUTH-015 | Đăng xuất | Đăng xuất khỏi hệ thống | 1. Đang đăng nhập.<br>2. Bấm chọn nút "Đăng xuất". | Hủy JWT token trên client, xóa session, điều hướng về trang chủ. | Chưa thực hiện | Ready | Cao |

---

### PHÂN HỆ 2: BOOKING (QUẢN LÝ ĐẶT PHÒNG - 25 TEST CASES)

| ID | Feature | Test Scenario | Test Steps | Expected Result | Actual Result | Status | Priority |
|---|---|---|---|---|---|---|---|
| TC-BOOK-016 | Đặt phòng | Tìm phòng với Ngày Check-in lớn hơn Ngày Check-out | 1. Nhập check-in là 15/08, check-out là 10/08.<br>2. Bấm Tìm phòng. | Hệ thống báo lỗi ngày trả lịch trình không được trước ngày nhận phòng. | Chưa thực hiện | Ready | Cao |
| TC-BOOK-017 | Đặt phòng | Tìm phòng với ngày Check-in ở quá khứ | 1. Nhập ngày check-in là ngày đã qua.<br>2. Bấm Tìm phòng. | Hệ thống chặn và báo lỗi ngày nhận phòng phải ở hiện tại hoặc tương lai. | Chưa thực hiện | Ready | Cao |
| TC-BOOK-018 | Đặt phòng | Tìm phòng khả dụng thành công | 1. Nhập ngày check-in và check-out hợp lệ.<br>2. Bấm Tìm phòng. | Hiển thị chính xác danh sách buồng phòng trống trong khoảng ngày đó. | Chưa thực hiện | Ready | Cao |
| TC-BOOK-019 | Đặt phòng | Đặt phòng khi chưa điền thông tin số khách | 1. Chọn phòng.<br>2. Nhập số khách là 0.<br>3. Bấm Đặt phòng. | Báo lỗi số người lưu trú tối thiểu phải từ 1 người trở lên. | Chưa thực hiện | Ready | Trung bình |
| TC-BOOK-020 | Đặt phòng | Đặt phòng vượt quá sức chứa tối đa thiết lập | 1. Chọn loại phòng Standard có max capacity là 2.<br>2. Nhập số khách là 4.<br>3. Bấm Đặt phòng. | Báo lỗi số lượng khách vượt quá quy định sức chứa tối đa của hạng phòng. | Chưa thực hiện | Ready | Cao |
| TC-BOOK-021 | Đặt phòng | Tạo Booking trạng thái PENDING thành công | 1. Điền toàn bộ thông tin hợp lệ.<br>2. Nhấn nút Xác nhận đặt phòng. | Tạo bản ghi Booking thành công ở trạng thái PENDING, trả về hạn thanh toán cọc. | Chưa thực hiện | Ready | Cao |
| TC-BOOK-022 | Đặt phòng | Khóa tạm thời phòng vật lý trong khi thanh toán | 1. Khách A bấm đặt phòng 102 đang ở bước thanh toán cọc.<br>2. Khách B vào tìm kiếm cùng thời gian đặt. | Phòng 102 phải bị ẩn hoặc chuyển sang chế độ lock đợi thanh toán cho khách B. | Chưa thực hiện | Ready | Cao |
| TC-BOOK-023 | Đặt giữ phòng | Tự động hủy đơn đặt phòng quá hạn thanh toán cọc | 1. Tạo đơn booking PENDING lúc 08h00 ngày 1.<br>2. Chờ qua 08h00 ngày 2 (24 giờ sau) không nạp tiền. | Hệ thống tự động chuyển trạng thái đơn sang CANCELLED, mở lại phòng trống. | Chưa thực hiện | Ready | Cao |
| TC-BOOK-024 | Hủy đặt phòng | Khách hàng tự hủy đơn trước giờ Check-in 48 giờ | 1. Khách truy cập đơn đặt chỗ trạng thái CONFIRMED.<br>2. Bấm Hủy đơn trước Check-in đúng 3 ngày (72h). | Hủy thành công, ghi nhận trả cọc (nếu có), trạng thái đơn thành CANCELLED. | Chưa thực hiện | Ready | Cao |
| TC-BOOK-025 | Hủy đặt phòng | Hủy đơn đặt phòng sát giờ (nhỏ hơn 48 giờ) | 1. Bấm hủy đơn cách giờ check-in mặc định 24h. | Trạng thái đơn đổi sang CANCELLED, tiền cọc hoàn lại cập nhật bằng 0. | Chưa thực hiện | Ready | Cao |
| TC-BOOK-026 | Đặt phòng | Đặt phòng trùng lịch (Double Booking) | 1. Lễ tân thao tác gán phòng 105 cho Booking X ngày 1-3/08.<br>2. Gán tiếp phòng 105 cho Booking Y ngày 2-4/08. | Hệ thống báo lỗi xung đột thời gian thuê phòng vật lý, chặn lưu đơn Y. | Chưa thực hiện | Ready | Cao |
| TC-BOOK-027 | Check-in | Làm thủ tục Check-in cho đơn hợp lệ | 1. Tìm đơn CONFIRMED đúng ngày đến ở bảng điều khiển.<br>2. Bấm chốt Nhận phòng. | Trạng thái đơn đổi thành CheckedIn, phòng từ VACANT_CLEAN sang OCCUPIED. | Chưa thực hiện | Ready | Cao |
| TC-BOOK-028 | Check-in | Check-in cho phòng chưa dọn (VACANT_DIRTY) | 1. Chọn phòng 103 có trạng thái bẩn.<br>2. Bấm Check-in khách vào phòng này. | Hệ thống đưa cảnh báo lỗi phòng chưa sạch, không được phép check-in vào ở. | Chưa thực hiện | Ready | Cao |
| TC-BOOK-029 | Check-in | Check-in sai ngày đăng ký trên hệ thống | 1. Vé đặt phòng ngày 20/08.<br>2. Lễ tân bấm nhận phòng ngày 18/08. | Hệ thống từ chối đăng ký check-in do sai lịch, yêu cầu sửa đổi thông tin booking. | Chưa thực hiện | Ready | Trung bình |
| TC-BOOK-030 | Check-in | Check-in thiếu hồ sơ CCCD của khách lưu trú | 1. Thực hiện check-in nhưng không điền thông tin CCCD/Passport của các khách ở. | Hệ thống chặn và báo lỗi bắt buộc cung cấp giấy tờ tùy thân của khách ở. | Chưa thực hiện | Ready | Trung bình |
| TC-BOOK-031 | Dịch vụ | Thêm dịch vụ tiêu thụ vào phòng nghỉ | 1. Chọn phòng đang OCCUPIED.<br>2. Chọn thêm 2 lon Coca-cola từ minibar.<br>3. Bấm xác nhận. | Dịch vụ được thêm vào đơn hàng, tự động nhân số lượng với đơn giá thực tế. | Chưa thực hiện | Ready | Cao |
| TC-BOOK-032 | Dịch vụ | Thêm dịch vụ với số lượng âm | 1. Nhập số lượng dịch vụ cần thêm là -1.<br>2. Bấm Lưu. | Báo lỗi số lượng dịch vụ tiêu thụ phải từ 1 trở lên. | Chưa thực hiện | Ready | Trung bình |
| TC-BOOK-033 | Check-out | Tính toán hóa đơn Check-out chính xác | 1. Bấm check-out cho đơn phòng nghỉ có sử dụng dịch vụ.<br>2. Xem bảng kê hóa đơn nháp. | Hóa đơn tính chuẩn: Tiền phòng = đơn giá * số đêm; Tổng tiền = tiền phòng + tiền dịch vụ. | Chưa thực hiện | Ready | Cao |
| TC-BOOK-034 | Check-out | Quyết toán hóa đơn khi chưa thanh toán đủ tiền | 1. Bấm chốt check-out trong khi hóa đơn vẫn chưa được đóng tiền (UNPAID). | Hệ thống chặn không cho check-out, bắt buộc hoàn tất thanh toán hóa đơn. | Chưa thực hiện | Ready | Cao |
| TC-BOOK-035 | Check-out | Check-out thành công | 1. Hóa đơn đã PAID.<br>2. Lễ tân nhấn Xác nhận hoàn tất Check-out. | Booking thành COMPLETED, trạng thái phòng vật lý tự động chuyển sang VACANT_DIRTY. | Chưa thực hiện | Ready | Cao |
| TC-BOOK-036 | Đặt phòng | Đặt đơn tại quầy (Walk-in) gán phòng tự động | 1. Khách đặt trực tiếp tại lễ tân.<br>2. Lễ tân gán trực tiếp phòng 104 đang Vacant_Clean. | Khởi tạo đơn ở trạng thái CheckedIn luôn, không cần đặt cọc. | Chưa thực hiện | Ready | Cao |
| TC-BOOK-037 | Đặt phòng | Đặt phòng và áp mã Voucher giảm giá | 1. Chọn phòng.<br>2. Điền mã giảm giá ưu đãi 10% hợp lệ.<br>3. Bấm đặt. | Tổng tiền giảm trừ đúng 10% trên đơn giá phòng gốc trước thuế. | Chưa thực hiện | Ready | Trung bình |
| TC-BOOK-038 | Đặt phòng | Đặt phòng và áp mã Voucher đã hết hạn | 1. Điền mã giảm giá đã quá thời hạn sử dụng. | Hệ thống từ chối áp dụng và báo lỗi mã ưu đãi không còn khả lực. | Chưa thực hiện | Ready | Trung bình |
| TC-BOOK-039 | Lịch sử | Truy vấn lịch sử đặt phòng của Khách hàng | 1. Đăng nhập tài khoản khách.<br>2. Chọn Lịch sử đặt phòng. | Trả về đúng danh sách các đơn đặt phòng thuộc về sở hữu của tài khoản đó. | Chưa thực hiện | Ready | Cao |
| TC-BOOK-040 | Hủy đặt phòng | Hủy đơn đặt phòng đã Check-in | 1. Đơn đang CheckedIn.<br>2. Cố gắng gửi lệnh yêu cầu hủy đơn. | Hệ thống chặn hành động do khách đã nhận phòng cư trú thực tế. | Chưa thực hiện | Ready | Trung bình |

---

### PHÂN HỆ 3: ROOM (QUẢN LÝ BUỒNG PHÒNG - 15 TEST CASES)

| ID | Feature | Test Scenario | Test Steps | Expected Result | Actual Result | Status | Priority |
|---|---|---|---|---|---|---|---|
| TC-ROOM-041 | Phòng nghỉ | Thêm mới phòng vật lý với thông tin hợp lệ | 1. Nhập Số phòng là 502, Tầng 5, chọn Loại Deluxe.<br>2. Bấm Lưu phòng. | Tạo phòng thành công, trạng thái mặc định của phòng mới là VACANT_CLEAN. | Chưa thực hiện | Ready | Cao |
| TC-ROOM-042 | Phòng nghỉ | Tạo phòng nghỉ trùng số phòng đã có | 1. Nhập số phòng 101 (đã tồn tại).<br>2. Bấm Lưu. | Báo lỗi trùng lặp số buồng phòng trong cùng khách sạn. | Chưa thực hiện | Ready | Cao |
| TC-ROOM-043 | Phòng nghỉ | Tạo phòng với Tầng âm (Không hợp lệ) | 1. Điền tầng là -2.<br>2. Bấm Lưu phòng. | Báo lỗi tầng lầu phải là số nguyên dương từ 1 đến 50. | Chưa thực hiện | Ready | Trung bình |
| TC-ROOM-044 | Phòng nghỉ | Thêm phòng nghỉ nhưng không gán loại phòng | 1. Để trống dropdown loại phòng.<br>2. Bấm Lưu. | Báo lỗi loại phòng tham chiếu liên kết không được để trống. | Chưa thực hiện | Ready | Trung bình |
| TC-ROOM-045 | Thiết lập | Sửa trạng thái phòng nghỉ thủ công | 1. Chọn phòng 101 đang VACANT_DIRTY.<br>2. Đổi trạng thái sang VACANT_CLEAN.<br>3. Bấm cập nhật. | Trạng thái phòng được ghi nhận sạch sẽ và sẵn sàng đón khách mới. | Chưa thực hiện | Ready | Cao |
| TC-ROOM-046 | Thiết lập | Chuyển phòng ở trạng thái Bảo trì (MAINTENANCE) | 1. Đổi trạng thái phòng sang MAINTENANCE.<br>2. Bấm cập nhật. | Phòng không hiển thị trên danh mục tìm kiếm thuê phòng trực tuyến của khách. | Chưa thực hiện | Ready | Cao |
| TC-ROOM-047 | Thiết lập | Xóa phòng đang có khách ở ở thực tế | 1. Phòng đang OCCUPIED.<br>2. Bấm chọn lệnh xóa khỏi hệ thống. | Hệ thống cảnh báo từ chối xóa do có giao dịch cư trú đang diễn ra. | Chưa thực hiện | Ready | Cao |
| TC-ROOM-048 | Loại phòng | Thêm mới loại phòng với đơn giá bằng 0 | 1. Nhập loại phòng Vip mới giá 0 VNĐ.<br>2. Bấm lưu. | Báo lỗi đơn giá phòng cơ bản phải lớn hơn 0 VNĐ. | Chưa thực hiện | Ready | Trung bình |
| TC-ROOM-049 | Loại phòng | Tạo hạng phòng với diện tích quá nhỏ | 1. Nhập diện tích phòng là 5m2.<br>2. Bấm Lưu. | Báo lỗi diện tích buồng phòng tối thiểu phải từ 10m2 trở lên. | Chưa thực hiện | Ready | Thấp |
| TC-ROOM-050 | Tìm kiếm | Tìm kiếm phòng trên Sơ đồ lưới buồng phòng | 1. Nhập từ khóa tìm kiếm là phòng 204. | Lưới sơ đồ tự lọc và làm nổi bật đúng ô phòng 204. | Chưa thực hiện | Ready | Cao |
| TC-ROOM-051 | Loại phòng | Xem chi tiết loại phòng phía khách hàng | 1. Khách click chọn xem phòng Suite. | Hiển thị giá phòng, diện tích, sức chứa tối đa và danh mục mô tả amenities. | Chưa thực hiện | Ready | Cao |
| TC-ROOM-052 | Thiết lập | Thay đổi loại phòng của một phòng đang trống | 1. Đang là Standard, sửa loại thành Deluxe.<br>2. Bấm Lưu. | Loại phòng được cập nhật, truy vấn giá thuê mới tương ứng được áp dụng. | Chưa thực hiện | Ready | Trung bình |
| TC-ROOM-053 | Tìm kiếm | Lọc phòng theo trạng thái dọn dẹp buồng | 1. Chọn bộ lọc "Phòng Bẩn" (Dirty) trên màn hình tiếp tân. | Kết quả hiển thị tất cả các phòng đang có trạng thái VACANT_DIRTY. | Chưa thực hiện | Ready | Cao |
| TC-ROOM-054 | Xóa phòng | Xóa loại phòng đang liên kết với buồng phòng | 1. Chọn Xóa hạng Vip.<br>2. Hạng Vip đang có 3 phòng vật lý liên đới. | Đưa ra thông báo chặn xóa và yêu cầu giải phóng/đổi loại phòng liên đới trước. | Chưa thực hiện | Ready | Cao |
| TC-ROOM-055 | Loại phòng | Thiết lập điều chỉnh cơ cấu giá loại phòng theo mùa | 1. Admin định tăng 20% giá Standard từ 01/12-31/12.<br>2. Lưu cấu hoạt. | Hệ thống tự động tính lũy kế điều chỉnh giá Standard khi tìm kiếm trong tháng 12. | Chưa thực hiện | Ready | Trung bình |

---

### PHÂN HỆ 4: PAYMENT (QUẢN LÝ THANH TOÁN - 15 TEST CASES)

| ID | Feature | Test Scenario | Test Steps | Expected Result | Actual Result | Status | Priority |
|---|---|---|---|---|---|---|---|
| TC-PAY-056 | Thanh toán | Thanh toán cọc giữ phòng trực tuyến thành công | 1. Thực hiện Booking.<br>2. Chọn thanh toán VNPay.<br>3. Trả tiền thành công ở app bank. | Hóa đơn đổi thành PAID (một phần cọc), Booking đổi thành CONFIRMED. | Chưa thực hiện | Ready | Cao |
| TC-PAY-057 | Thanh toán | Cổng thanh toán báo lỗi (Thùng ví rỗng tiền) | 1. Chọn thanh toán trực tuyến.<br>2. Tài khoản liên kết không đủ tiền giao dịch. | Hệ thống báo lỗi thanh toán thất bại, đơn phòng giữ trạng thái PENDING. | Chưa thực hiện | Ready | Cao |
| TC-PAY-058 | Thanh toán | Hết thời gian giao dịch tại cổng thanh toán | 1. Mở trang thanh toán VNPay.<br>2. Chờ hết 15 phút không nhập OTP. | Giao dịch tự động hủy bỏ, hoàn tác giao dịch trên hệ thống PMS. | Chưa thực hiện | Ready | Cao |
| TC-PAY-059 | Thanh toán | Thanh toán tiền mặt tại quầy lễ tân | 1. Chốt hóa đơn trả phòng.<br>2. Khách đưa tiền mặt.<br>3. Tiếp tân bấm "Xác nhận thanh toán tiền mặt". | Trạng thái hóa đơn chuyển sang PAID, cấp mã giao dịch nội bộ tiền mặt. | Chưa thực hiện | Ready | Cao |
| TC-PAY-060 | Thanh toán | Hoàn tiền khi hủy đơn đặt phòng | 1. Khách hủy đơn đặt phòng cách Check-in 5 ngày.<br>2. Hệ thống duyệt hoàn tiền cọc. | Tạo bản ghi giao dịch âm (Refund Transaction) trị giá 100% cọc đã trả. | Chưa thực hiện | Ready | Cao |
| TC-PAY-061 | Thanh toán | Kiểm tra số tiền cọc tối thiểu | 1. Đơn phòng có tổng tiền 1.000.000 VNĐ.<br>2. Cố gắng ghi nhận thanh toán cọc dưới 300.000 VNĐ (30%). | Hệ thống không chấp nhận xác nhận đơn phòng do chưa đóng đủ cọc tối thiểu. | Chưa thực hiện | Ready | Cao |
| TC-PAY-062 | Thanh toán | Đóng hóa đơn đã thanh toán trước đó | 1. Hóa đơn X đang ở trạng thái PAID.<br>2. Gửi thêm lượt request thanh toán tiếp cho hóa đơn X. | Hệ thống chặn và trả lỗi hóa đơn đã được quyết toán hoàn tất. | Chưa thực hiện | Ready | Cao |
| TC-PAY-063 | Kế toán | Xác thực tính toán tiền thuế VAT trong hóa đơn | 1. Xuất hóa đơn tổng 5.000.000 VNĐ.<br>2. Tính thuế VAT 10%. | Thuế VAT phải hiển thị đúng là 500.000 VNĐ, tổng tiền thanh toán hiển thị 5.500.000 VNĐ. | Chưa thực hiện | Ready | Trung bình |
| TC-PAY-064 | Giao dịch | Giao dịch trùng mã đối chiếu ngân hàng (Transaction ID) | 1. Nhập thủ công mã giao dịch ATM trùng khớp mã đã ghi trong DB.<br>2. Bấm Xác nhận. | Hệ thống báo lỗi trùng mã giao dịch kiểm toán, từ chối lưu. | Chưa thực hiện | Ready | Cao |
| TC-PAY-065 | Thanh toán | Thanh toán hóa đơn bằng tích điểm Loyalty | 1. Chọn thanh toán.<br>2. Chọn cách trừ 100 loyalty points (tương đương 100k VNĐ). | Giảm trừ trực tiếp 100.000 VNĐ trên tổng tiền của hóa đơn. | Chưa thực hiện | Ready | Trung bình |
| TC-PAY-066 | Thanh toán | Thanh toán và số dư điểm loyalty không đủ | 1. Tài khoản có 20 điểm.<br>2. Chọn trừ 50 điểm thanh toán. | Hệ thống báo lỗi không đủ số dư điểm và giữ nguyên tổng tiền ban đầu. | Chưa thực hiện | Ready | Trung bình |
| TC-PAY-067 | Kế toán | Ghi nhận chiết khấu trực tiếp trên Hóa đơn | 1. Thiết lập chiết khấu 200.000 VNĐ vào hóa đơn check-out. | Tổng thu cuối cùng phải hiển thị chính xác: finalAmount = total - discount + tax. | Chưa thực hiện | Ready | Cao |
| TC-PAY-068 | Hóa đơn | Xem hóa đơn điện tử phía Khách hàng | 1. Khách truy cập đơn đặt chỗ cũ.<br>2. Bấm Xem hóa đơn. | Hiển thị bảng kê chi tiết các tiền phòng, các món đồ minibar đã sử dụng rõ ràng. | Chưa thực hiện | Ready | Cao |
| TC-PAY-069 | Hóa đơn | Xuất file PDF Hóa đơn điện tử | 1. Nhân viên nhấn Xuất PDF Hóa đơn. | Tải về thành công file hóa đơn chuẩn định dạng, hiển thị đầy đủ thông tin. | Chưa thực hiện | Ready | Trung bình |
| TC-PAY-070 | Thanh toán | Thanh toán hóa đơn có giá trị bằng 0 | 1. Tạo thanh toán cho hóa đơn chênh lệch có giá trị 0 VNĐ. | Hệ thống báo lỗi số tiền giao dịch không được nhỏ hơn hoặc bằng 0. | Chưa thực hiện | Ready | Trung bình |

---

### PHÂN HỆ 5: REVIEW (BÌNH LUẬN & ĐÁNH GIÁ - 10 TEST CASES)

| ID | Feature | Test Scenario | Test Steps | Expected Result | Actual Result | Status | Priority |
|---|---|---|---|---|---|---|---|
| TC-REV-071 | Đánh giá | Khách hàng đánh giá bình luận hợp lệ | 1. Truy cập đơn đặt chỗ đã trả phòng.<br>2. Chọn 5 sao, viết nhận xét tốt.<br>3. Bấm Gửi. | Ghi nhận đánh giá thành công ở trạng thái chờ duyệt (PENDING_MODERATION). | Chưa thực hiện | Ready | Cao |
| TC-REV-072 | Đánh giá | Đánh giá cho Booking chưa Check-out | 1. Booking đang ở trạng thái CONFIRMED (chưa ở).<br>2. Tìm link gửi đánh giá. | Hệ thống chặn không cho đánh giá, báo lỗi chỉ được review sau check-out. | Chưa thực hiện | Ready | Cao |
| TC-REV-073 | Đánh giá | Đánh giá điểm ngoài thang quy định | 1. Gửi dữ liệu rating bằng 6 hoặc 0 qua API. | Báo lỗi trường điểm rating của phòng phải nằm trong khoảng từ 1 đến 5 sao. | Chưa thực hiện | Ready | Trung bình |
| TC-REV-074 | Kiểm duyệt | Admin phê duyệt bình luận hợp chuẩn | 1. Admin truy cập màn Kiểm duyệt đánh giá.<br>2. Nhấn Phê duyệt cho review. | Đánh giá hiển thị công khai trên giao diện chi tiết loại phòng tương ứng. | Chưa thực hiện | Ready | Cao |
| TC-REV-075 | Kiểm duyệt | Admin từ chối bình luận bôi nhọ tục tĩu | 1. Đánh giá chứa nội dung thô tục.<br>2. Admin bấm Chọn Ẩn/Từ chối duyệt. | Trạng thái chuyển sang REJECTED, không hiển thị trên website. | Chưa thực hiện | Ready | Cao |
| TC-REV-076 | Đánh giá | Tự động tính lũy kế cập nhật điểm trung bình loại phòng | 1. Loại phòng Vip đang có điểm trung bình là 4.0 (từ 2 review).<br>2. Duyệt thêm 1 review 5.0. | Điểm trung bình loại phòng tự động cập nhật lại thành 4.33. | Chưa thực hiện | Ready | Trung bình |
| TC-REV-077 | Đánh giá | Viết bình luận đánh giá quá giới hạn ký tự | 1. Nhập nhận xét dài 2000 chữ (quy định tối đa 1000).<br>2. Bấm Gửi. | Hệ thống báo lỗi độ dài phản hồi vượt quá quy định 1000 ký tự. | Chưa thực hiện | Ready | Thấp |
| TC-REV-078 | Đánh giá | Gửi đánh giá trùng lặp trên một đơn đặt phòng | 1. Đơn phòng đã được đánh giá thành công trước đó.<br>2. Gửi thêm yêu cầu đánh giá mới cho đơn này. | Hệ thống chặn gửi và báo lỗi mỗi đơn đặt chỗ chỉ được đánh giá một lần duy nhất. | Chưa thực hiện | Ready | Cao |
| TC-REV-079 | Đánh giá | Xóa đánh giá bình luận vi phạm bởi Admin | 1. Admin bấm nút xóa hẳn bình luận.<br>2. Xác nhận. | Bản ghi bị xóa khỏi cơ sở dữ liệu, điểm đánh giá trung bình loại phòng recalculate. | Chưa thực hiện | Ready | Trung bình |
| TC-REV-080 | Đánh giá | Người dùng vãng lai cố tình gửi API đánh giá | 1. Gửi request POST tới `/api/v1/reviews` không kèm JWT Token. | Báo lỗi 401 Unauthorized, chặn bảo mật thành công. | Chưa thực hiện | Ready | Cao |

---

### PHÂN HỆ 6: EMPLOYEE (QUẢN TRỊ NHÂN VIÊN - 15 TEST CASES)

| ID | Feature | Test Scenario | Test Steps | Expected Result | Actual Result | Status | Priority |
|---|---|---|---|---|---|---|---|
| TC-EMP-081 | Nhân sự | Tạo mới nhân viên với thông tin hợp lệ | 1. Admin nhập thông tin nhân viên mới, mã EMP-1092.<br>2. Bấm Lưu dữ liệu. | Tài khoản nhân sự được lập ở trạng thái ACTIVE, tạo mới thành công. | Chưa thực hiện | Ready | Cao |
| TC-EMP-082 | Nhân sự | Tạo mã nhân viên không hợp định dạng (EMP-xxxx) | 1. Nhập mã nhân sự là "MALL-012".<br>2. Bấm Tạo. | Hệ thống báo lỗi mã nhân viên không hợp lệ (Sai Regex bắt buộc EMP-xxxx). | Chưa thực hiện | Ready | Trung bình |
| TC-EMP-083 | Nhân sự | Lập nhân sự trùng mã định danh | 1. Nhập mã EMP-1092 đã cấp cho nhân sự khác.<br>2. Bấm Tạo. | Báo lỗi mã nhân viên đã tồn tại trên hệ thống, không cho thiết lập. | Chưa thực hiện | Ready | Cao |
| TC-EMP-084 | Nhân sự | Lập nhân sự có email trùng lặp | 1. Nhập Email trùng email hệ thống.<br>2. Bấm Lưu. | Hệ thống báo lỗi địa chỉ email làm việc đã được đăng ký sử dụng. | Chưa thực hiện | Ready | Cao |
| TC-EMP-085 | Quy trình | Thiết lập ca làm việc cho nhân viên | 1. Chọn nhân sự.<br>2. Chọn ca sáng (06h00 - 14h00), gán ngày trực chủ nhật.<br>3. Lưu. | Phân lịch trực ca thành công, lưu dữ liệu vào bảng phân lịch nhân viên. | Chưa thực hiện | Ready | Trung bình |
| TC-EMP-086 | Quy trình | Khóa tài khoản nhân viên (Block Account) | 1. Chọn nhân sự.<br>2. Nhấn nút Khóa tài khoản.<br>3. Xác nhận. | Trạng thái nhân viên thành INACTIVE, nhân viên không thể đăng nhập PMS nữa. | Chưa thực hiện | Ready | Cao |
| TC-EMP-087 | Phân quyền | Cấp quyền Role công vụ cho nhân viên | 1. Chọn nhân viên.<br>2. Cập nhật Role từ Lễ tân (Receptionist) sang Quản lý khách (Admin). | Cập nhật phân quyền thành công, nhân viên được cấp dải menu quản trị của Admin. | Chưa thực hiện | Ready | Cao |
| TC-EMP-088 | Tiền lương | Nhập mức lương ca làm ở dạng số âm | 1. Điền lương rate là -15000.<br>2. Bấm Lưu. | Báo lỗi mức lương định mức của nhân viên phải lớn hơn 0 VNĐ. | Chưa thực hiện | Ready | Trung bình |
| TC-EMP-089 | Kiểm toán | Thay đổi thông tin lương nhân sự được log lại | 1. Thực hiện tăng lương cho nhân viên X thêm 5.000 VNĐ.<br>2. Vào Audit trail xem. | Ghi nhận chi tiết lịch sử thay đổi: Người sửa, Ngày sửa, Lương cũ, Lương mới. | Chưa thực hiện | Ready | Cao |
| TC-EMP-090 | Nhân sự | Cập nhật số điện thoại sai định dạng | 1. Nhập SĐT nhân viên chứa các ký tự đặc biệt chữ cái. | Báo lỗi định dạng số điện thoại không hợp chuẩn, chặn không cho cập nhật. | Chưa thực hiện | Ready | Trung bình |
| TC-EMP-091 | Đăng nhập | Đăng nhập bằng tài khoản nhân viên đang khóa (INACTIVE) | 1. Nhập email nhân vụ bị khóa.<br>2. Bấm đăng nhập. | Báo lỗi tài khoản nhân sự đang bị đóng băng để rà soát, không cấp JWT. | Chưa thực hiện | Ready | Cao |
| TC-EMP-092 | Quy trình | Chốt ca trực bàn giao tiền mặt (Close Shift) | 1. Lễ tân kết thúc ca trực.<br>2. Nhập số tiền thu thực tế chênh lệch.<br>3. Bấm chốt ca. | Cho phép chốt sổ, cập nhật số dư tiền mặt ngăn kéo và chốt ca lễ tân thành công. | Chưa thực hiện | Ready | Cao |
| TC-EMP-093 | Tìm kiếm | Tìm kiếm nhân viên trên danh sách theo Tên | 1. Gõ từ khóa tìm kiếm "Tran Thi B". | Lưới hiển thị chính xác thông tin nhân sự mang tên B. | Chưa thực hiện | Ready | Trung bình |
| TC-EMP-094 | Nhân sự | Thêm nhân viên với số điện thoại trống | 1. Để trống trường số điện thoại nhân sự.<br>2. Bấm Lưu. | Hệ thống chặn đăng ký và báo lỗi số điện thoại không được để trống. | Chưa thực hiện | Ready | Trung bình |
| TC-EMP-095 | Phân quyền | Nhân sự lễ tân cố gắng xóa tài khoản Admin | 1. Nhân viên đăng nhập tài khoản Lễ tân.<br>2. Gửi request DELETE tới API quản trị tài khoản Admin. | Hệ thống trả về lỗi 403 Forbidden, chặn hành vi leo quyền. | Chưa thực hiện | Ready | Cao |

---

### PHÂN HỆ 7: DASHBOARD (BÁO CÁO CHỈ SỐ - 10 TEST CASES)

| ID | Feature | Test Scenario | Test Steps | Expected Result | Actual Result | Status | Priority |
|---|---|---|---|---|---|---|---|
| TC-DASH-096 | Chỉ số | Xác định doanh thu ngày hôm nay khớp thực tế | 1. Chạy 2 giao dịch check-out tổng giá trị 4.000.000 VNĐ trong ngày.<br>2. Xem Dashboard. | Thẻ doanh thu ngày hôm nay tăng chính xác thêm 4.000.000 VNĐ. | Chưa thực hiện | Ready | Cao |
| TC-DASH-097 | Chỉ số | Kiểm tra Tỷ lệ lấp đầy phòng (Occupancy Rate) | 1. Khách sạn có 10 phòng, có 3 phòng đang có khách ở.<br>2. Truy cập Dashboard. | Tỷ lệ phần trăm buồng phòng lấp đầy hiển thị chính xác là 30%. | Chưa thực hiện | Ready | Cao |
| TC-DASH-098 | Chỉ số | Thống kê số lượng phòng bẩn (Dirty) chuẩn buồng | 1. Hệ thống có 5 phòng có trạng thái VACANT_DIRTY.<br>2. Xem thẻ thống kê dọn phòng. | Thẻ thống kê số lượng phòng bẩn cần dọn dẹp hiển thị đúng con số 5. | Chưa thực hiện | Ready | Trung bình |
| TC-DASH-099 | Báo cáo | Lọc doanh thu báo cáo theo khoảng thời gian không có dữ liệu | 1. Thiết lập chọn mốc thời gian lọc từ ngày 01/01/2000 đến 01/02/2000.<br>2. Bấm lọc. | Doanh thu báo cáo hiển thị về mức 0 VNĐ, không xảy ra crash hệ thống. | Chưa thực hiện | Ready | Trung bình |
| TC-DASH-100 | Lịch trình | Thống kê số lượng khách nhận phòng hôm nay | 1. Hôm nay có 4 Booking đến lịch trình Check-in.<br>2. Mở dashboard tiếp tân. | Thẻ nhắc nhở Check-in trong ngày hiển thị chính xác con số 4 đơn phòng. | Chưa thực hiện | Ready | Cao |
| TC-DASH-101 | Báo cáo | So sánh báo cáo tháng này với tháng trước | 1. Chọn chức năng so sánh chu kỳ.<br>2. Lọc tháng này và tháng trước. | Xuất hiện chỉ số tăng trưởng phần trăm dương/âm khớp tính toán chênh lệch. | Chưa thực hiện | Ready | Trung bình |
| TC-DASH-102 | Quyền | Nhân viên buồng phòng xem báo cáo tài chính | 1. Nhân viên buồng (Housekeeper) truy cập Dashboard tài chính. | Hệ thống từ chối hiển thị và điều hướng sang trang sơ đồ dọn dẹp phòng. | Chưa thực hiện | Ready | Cao |
| TC-DASH-103 | Biểu đồ | Kiểm tra tính liên tục dữ liệu biểu đồ doanh thu | 1. Truy vấn biểu đồ cột doanh thu 12 tháng gần nhất. | Các cột tháng hiển thị đầy đủ, không bị rỗng ở tháng không có hóa đơn (hiển thị 0). | Chưa thực hiện | Ready | Trung bình |
| TC-DASH-104 | Báo cáo | Xuất báo cáo tài chính định dạng Excel (.xlsx) | 1. Bấm nút Tải xuống Excel báo cáo doanh thu năm. | File Excel được tải xuống thành công, hiển thị chính xác tổng các cột số liệu. | Chưa thực hiện | Ready | Trung bình |
| TC-DASH-105 | Báo cáo | Lọc khoảng thời gian sai định dạng trên Dashboard | 1. Nhập ngày "32/13/2026" vào ô lọc thời gian.<br>2. Bấm tìm lọc. | Hệ thống chặn nhập liệu, thông báo định dạng ngày tháng không hợp chuẩn. | Chưa thực hiện | Ready | Trung bình |


---


# FILE: QA_Test_Data.md

# CO SO DU LIEU DOI SOAT KIEM THU (TEST DATA)
## DU AN: HEY THONG QUAN LY KHACH SAN (HOTEL MANAGEMENT SYSTEM)

Tai lieu cung cap du lieu mo phong thuc hanh khop thuc te Viet Nam phuc vu viec doi soat va cau truc co so du lieu mau cho he thong.

---

### 1. Danh sach 10 loai phong (Room Types)

| ID | Hang loai phong | Don gia | Max Khach | Dien tich | Tien nghi mau |
|---|---|---|---|---|---|
| 1 | Standard Single (Phong Don Tieu chuan) | 500,000 VNĐ | 1 | 18 m2 | `["Wifi", "Quat may", "Tivi"]` |
| 2 | Standard Double (Phong Doi Tieu chuan) | 750,000 VNĐ | 2 | 22 m2 | `["Wifi", "Dieu hoa", "Tivi"]` |
| 3 | Superior Twin (Phong Hai Giuong Superior) | 950,000 VNĐ | 2 | 25 m2 | `["Wifi", "Dieu hoa", "Tivi", "Minibar"]` |
| 4 | Superior King (Phong Giuong Lon Superior) | 1,050,000 VNĐ | 2 | 28 m2 | `["Wifi", "Dieu hoa", "Tivi", "Minibar", "Ban cong"]` |
| 5 | Deluxe Triple (Phong Ba Giuong Deluxe) | 1,400,000 VNĐ | 3 | 32 m2 | `["Wifi", "Dieu hoa", "Tivi Smart", "Minibar", "Bon tam"]` |
| 6 | Deluxe Family (Phong Gia dinh Deluxe) | 1,800,000 VNĐ | 4 | 40 m2 | `["Wifi", "Dieu hoa", "Tivi Smart", "Minibar", "Bon tam", "Sofa"]` |
| 7 | Executive Suite (Phong Suite Cao cap) | 2,500,000 VNĐ | 2 | 48 m2 | `["Wifi", "Dieu hoa", "Tivi Smart", "Minibar", "Bon tam", "Ban cong", "Tra chieu"]` |
| 8 | Junior Suite (Phong Suite Nho) | 2,200,000 VNĐ | 2 | 42 m2 | `["Wifi", "Dieu hoa", "Tivi", "Minibar", "Bon tam", "Ban cong"]` |
| 9 | Presidential Suite (Phong Tong thong) | 5,000,000 VNĐ | 4 | 120 m2 | `["Wifi VIP", "Dieu hoa trung tam", "Tivi 4K", "Quay Bar rieng", "Be suc", "Quan gia rieng"]` |
| 10 | Penthouse Luxury (Can ho Ap mai Hang sang) | 6,500,000 VNĐ | 6 | 150 m2 | `["Wifi VIP", "Dieu hoa trung tam", "Be boi rieng", "San vuon", "Bep gia dinh", "Khu BBQ"]` |

---

### 2. Danh sach 30 phong vat ly (Rooms)

| ID | So phong | Vi tri tang | Trang thai phong | ID Loai phong |
|---|---|---|---|---|
| 1 | Phong 101 | Tang 1 | VACANT_CLEAN | Type 1 |
| 2 | Phong 102 | Tang 1 | VACANT_CLEAN | Type 2 |
| 3 | Phong 103 | Tang 1 | VACANT_CLEAN | Type 3 |
| 4 | Phong 104 | Tang 1 | VACANT_CLEAN | Type 4 |
| 5 | Phong 105 | Tang 1 | VACANT_CLEAN | Type 5 |
| 6 | Phong 201 | Tang 2 | VACANT_CLEAN | Type 6 |
| 7 | Phong 202 | Tang 2 | VACANT_CLEAN | Type 7 |
| 8 | Phong 203 | Tang 2 | VACANT_CLEAN | Type 8 |
| 9 | Phong 204 | Tang 2 | VACANT_CLEAN | Type 9 |
| 10 | Phong 205 | Tang 2 | VACANT_CLEAN | Type 10 |
| 11 | Phong 301 | Tang 3 | VACANT_CLEAN | Type 1 |
| 12 | Phong 302 | Tang 3 | VACANT_CLEAN | Type 2 |
| 13 | Phong 303 | Tang 3 | VACANT_CLEAN | Type 3 |
| 14 | Phong 304 | Tang 3 | VACANT_CLEAN | Type 4 |
| 15 | Phong 305 | Tang 3 | VACANT_CLEAN | Type 5 |
| 16 | Phong 401 | Tang 4 | OCCUPIED | Type 6 |
| 17 | Phong 402 | Tang 4 | OCCUPIED | Type 7 |
| 18 | Phong 403 | Tang 4 | OCCUPIED | Type 8 |
| 19 | Phong 404 | Tang 4 | OCCUPIED | Type 9 |
| 20 | Phong 405 | Tang 4 | OCCUPIED | Type 10 |
| 21 | Phong 501 | Tang 5 | OCCUPIED | Type 1 |
| 22 | Phong 502 | Tang 5 | OCCUPIED | Type 2 |
| 23 | Phong 503 | Tang 5 | OCCUPIED | Type 3 |
| 24 | Phong 504 | Tang 5 | OCCUPIED | Type 4 |
| 25 | Phong 505 | Tang 5 | OCCUPIED | Type 5 |
| 26 | Phong 601 | Tang 6 | VACANT_DIRTY | Type 6 |
| 27 | Phong 602 | Tang 6 | VACANT_DIRTY | Type 7 |
| 28 | Phong 603 | Tang 6 | VACANT_DIRTY | Type 8 |
| 29 | Phong 604 | Tang 6 | MAINTENANCE | Type 9 |
| 30 | Phong 605 | Tang 6 | MAINTENANCE | Type 10 |

---

### 3. Danh sach 20 tai khoan dang nhap (Users)

| ID | Email dang ky | Ho va Ten | Dien thoai | Vai tro |
|---|---|---|---|---|
| 1 | tranngocphong1@gmail.com | Tran Ngoc Phong | 0777452080 | CUSTOMER |
| 2 | lehoangyen2@gmail.com | Le Hoang Yen | 0936873706 | CUSTOMER |
| 3 | duongducdung3@gmail.com | Duong Duc Dung | 0883995853 | CUSTOMER |
| 4 | hoangkhanhhai4@gmail.com | Hoang Khanh Hai | 0775254539 | CUSTOMER |
| 5 | vongocdung5@gmail.com | Vo Ngoc Dung | 0778604621 | CUSTOMER |
| 6 | dothitrang6@gmail.com | Do Thi Trang | 0357117075 | CUSTOMER |
| 7 | dohoangson7@gmail.com | Do Hoang Son | 0335548527 | CUSTOMER |
| 8 | nguyenhoangcuong8@gmail.com | Nguyen Hoang Cuong | 0332098009 | CUSTOMER |
| 9 | hothuquynh9@gmail.com | Ho Thu Quynh | 0331107629 | CUSTOMER |
| 10 | phamphuonglan10@gmail.com | Pham Phuong Lan | 0976757211 | CUSTOMER |
| 11 | vuthiquynh11@gmail.com | Vu Thi Quynh | 0936752668 | CUSTOMER |
| 12 | nguyenngoccuong12@gmail.com | Nguyen Ngoc Cuong | 0862988024 | CUSTOMER |
| 13 | vohoangkhoa13@gmail.com | Vo Hoang Khoa | 0882865021 | CUSTOMER |
| 14 | ngovannam14@gmail.com | Ngo Van Nam | 0988598020 | CUSTOMER |
| 15 | phamgiagiang15@gmail.com | Pham Gia Giang | 0327914469 | CUSTOMER |
| 16 | duongkhanhtuan16@gmail.com | Duong Khanh Tuan | 0909229877 | CUSTOMER |
| 17 | dothudung17@gmail.com | Do Thu Dung | 0912893821 | CUSTOMER |
| 18 | dophuongvy18@gmail.com | Do Phuong Vy | 0352614915 | CUSTOMER |
| 19 | hokhanhlinh19@gmail.com | Ho Khanh Linh | 0916130171 | CUSTOMER |
| 20 | duongthiphong20@gmail.com | Duong Thi Phong | 0778988129 | CUSTOMER |

---

### 4. Danh sach 20 ho so khach hang (Customers)

| ID | User ID | Ten Khach hang | So lien lac | So CCCD/Passport | Diem tich luy |
|---|---|---|---|---|---|
| 1 | 1 | Tran Ngoc Phong | 0777452080 | `001097168274` | 280 |
| 2 | 2 | Le Hoang Yen | 0936873706 | `001091058240` | 175 |
| 3 | 3 | Duong Duc Dung | 0883995853 | `001092475773` | 277 |
| 4 | 4 | Hoang Khanh Hai | 0775254539 | `B8279651` | 187 |
| 5 | 5 | Vo Ngoc Dung | 0778604621 | `001096603035` | 17 |
| 6 | 6 | Do Thi Trang | 0357117075 | `001098862469` | 79 |
| 7 | 7 | Do Hoang Son | 0335548527 | `001095173957` | 139 |
| 8 | 8 | Nguyen Hoang Cuong | 0332098009 | `B5080725` | 68 |
| 9 | 9 | Ho Thu Quynh | 0331107629 | `001093327225` | 238 |
| 10 | 10 | Pham Phuong Lan | 0976757211 | `001097448010` | 135 |
| 11 | 11 | Vu Thi Quynh | 0936752668 | `001092638758` | 54 |
| 12 | 12 | Nguyen Ngoc Cuong | 0862988024 | `B4503489` | 208 |
| 13 | 13 | Vo Hoang Khoa | 0882865021 | `001097251496` | 145 |
| 14 | 14 | Ngo Van Nam | 0988598020 | `001094384755` | 203 |
| 15 | 15 | Pham Gia Giang | 0327914469 | `001095116689` | 22 |
| 16 | 16 | Duong Khanh Tuan | 0909229877 | `B9106521` | 127 |
| 17 | 17 | Do Thu Dung | 0912893821 | `001097220761` | 79 |
| 18 | 18 | Do Phuong Vy | 0352614915 | `001091268695` | 226 |
| 19 | 19 | Ho Khanh Linh | 0916130171 | `001095333138` | 262 |
| 20 | 20 | Duong Thi Phong | 0778988129 | `B4326196` | 171 |

---

### 5. Danh sach 10 nhan su van hanh (Employees)

| ID | Ma nhan su | Ten nhan vien | Vai tro | Email cong tac | Dien thoai | Luong ca truc |
|---|---|---|---|---|---|---|
| 1 | `EMP-1001` | Dang Hoang Son | RECEPTIONIST | staff_dang1@hotel.com | 0775462655 | 47,000.0 VNĐ |
| 2 | `EMP-1002` | Ly Phuong Hoa | RECEPTIONIST | staff_ly2@hotel.com | 0332616155 | 35,000.0 VNĐ |
| 3 | `EMP-1003` | Duong Gia Yen | RECEPTIONIST | staff_duong3@hotel.com | 0888054869 | 22,000.0 VNĐ |
| 4 | `EMP-1004` | Tran Tuan Son | ACCOUNTANT | staff_tran4@hotel.com | 0986010737 | 25,000.0 VNĐ |
| 5 | `EMP-1005` | Ly Minh Nam | ACCOUNTANT | staff_ly5@hotel.com | 0325873137 | 28,000.0 VNĐ |
| 6 | `EMP-1006` | Ngo Duc Giang | HOUSEKEEPER | staff_ngo6@hotel.com | 0775398486 | 49,000.0 VNĐ |
| 7 | `EMP-1007` | Tran Hoang Quynh | HOUSEKEEPER | staff_tran7@hotel.com | 0331637841 | 50,000.0 VNĐ |
| 8 | `EMP-1008` | Vu Khanh Linh | ADMIN | staff_vu8@hotel.com | 0353779569 | 27,000.0 VNĐ |
| 9 | `EMP-1009` | Do Tuan Tuan | ADMIN | staff_do9@hotel.com | 0799534170 | 28,000.0 VNĐ |
| 10 | `EMP-1010` | Ngo Gia Phong | HOUSEKEEPER | staff_ngo10@hotel.com | 0333229143 | 45,000.0 VNĐ |

---

### 6. Danh sach 50 dat phong mau (Bookings)

| ID | User ID | Ngay Check-in | Ngay Check-out | Tong gia tri | Trang thai |
|---|---|---|---|---|---|
| 1 | User 1 | 2026-08-31 | 2026-09-01 | 5,000,000 VNĐ | CANCELLED |
| 2 | User 2 | 2026-08-12 | 2026-08-17 | 32,500,000 VNĐ | COMPLETED |
| 3 | User 3 | 2026-08-22 | 2026-08-24 | 2,100,000 VNĐ | CONFIRMED |
| 4 | User 4 | 2026-08-09 | 2026-08-13 | 5,600,000 VNĐ | COMPLETED |
| 5 | User 5 | 2026-08-14 | 2026-08-16 | 3,600,000 VNĐ | COMPLETED |
| 6 | User 6 | 2026-08-28 | 2026-09-01 | 7,200,000 VNĐ | CANCELLED |
| 7 | User 7 | 2026-08-28 | 2026-09-01 | 3,800,000 VNĐ | CANCELLED |
| 8 | User 8 | 2026-08-24 | 2026-08-26 | 3,600,000 VNĐ | CONFIRMED |
| 9 | User 9 | 2026-08-03 | 2026-08-05 | 2,100,000 VNĐ | COMPLETED |
| 10 | User 10 | 2026-08-04 | 2026-08-07 | 4,200,000 VNĐ | COMPLETED |
| 11 | User 11 | 2026-08-09 | 2026-08-14 | 9,000,000 VNĐ | COMPLETED |
| 12 | User 12 | 2026-08-21 | 2026-08-23 | 13,000,000 VNĐ | CONFIRMED |
| 13 | User 13 | 2026-08-14 | 2026-08-16 | 2,800,000 VNĐ | COMPLETED |
| 14 | User 14 | 2026-08-17 | 2026-08-18 | 950,000 VNĐ | CONFIRMED |
| 15 | User 15 | 2026-08-13 | 2026-08-18 | 5,250,000 VNĐ | COMPLETED |
| 16 | User 16 | 2026-08-13 | 2026-08-15 | 13,000,000 VNĐ | COMPLETED |
| 17 | User 17 | 2026-08-15 | 2026-08-18 | 2,250,000 VNĐ | CONFIRMED |
| 18 | User 18 | 2026-08-08 | 2026-08-10 | 5,000,000 VNĐ | COMPLETED |
| 19 | User 19 | 2026-08-16 | 2026-08-17 | 2,200,000 VNĐ | CONFIRMED |
| 20 | User 20 | 2026-08-13 | 2026-08-18 | 3,750,000 VNĐ | COMPLETED |
| 21 | User 1 | 2026-08-22 | 2026-08-26 | 10,000,000 VNĐ | CONFIRMED |
| 22 | User 2 | 2026-08-29 | 2026-08-30 | 750,000 VNĐ | CANCELLED |
| 23 | User 3 | 2026-08-04 | 2026-08-08 | 20,000,000 VNĐ | COMPLETED |
| 24 | User 4 | 2026-08-11 | 2026-08-16 | 11,000,000 VNĐ | COMPLETED |
| 25 | User 5 | 2026-08-07 | 2026-08-12 | 2,500,000 VNĐ | COMPLETED |
| 26 | User 6 | 2026-08-02 | 2026-08-03 | 750,000 VNĐ | COMPLETED |
| 27 | User 7 | 2026-08-04 | 2026-08-09 | 3,750,000 VNĐ | COMPLETED |
| 28 | User 8 | 2026-08-07 | 2026-08-11 | 3,800,000 VNĐ | COMPLETED |
| 29 | User 9 | 2026-08-28 | 2026-08-30 | 2,100,000 VNĐ | PENDING |
| 30 | User 10 | 2026-08-18 | 2026-08-21 | 6,600,000 VNĐ | CONFIRMED |
| 31 | User 11 | 2026-08-27 | 2026-08-29 | 5,000,000 VNĐ | PENDING |
| 32 | User 12 | 2026-08-11 | 2026-08-12 | 1,400,000 VNĐ | COMPLETED |
| 33 | User 13 | 2026-08-27 | 2026-08-29 | 4,400,000 VNĐ | PENDING |
| 34 | User 14 | 2026-08-17 | 2026-08-21 | 5,600,000 VNĐ | CONFIRMED |
| 35 | User 15 | 2026-08-03 | 2026-08-08 | 12,500,000 VNĐ | COMPLETED |
| 36 | User 16 | 2026-08-04 | 2026-08-05 | 500,000 VNĐ | COMPLETED |
| 37 | User 17 | 2026-08-22 | 2026-08-25 | 2,250,000 VNĐ | CONFIRMED |
| 38 | User 18 | 2026-08-22 | 2026-08-25 | 3,150,000 VNĐ | CONFIRMED |
| 39 | User 19 | 2026-08-28 | 2026-08-31 | 4,200,000 VNĐ | CANCELLED |
| 40 | User 20 | 2026-08-18 | 2026-08-20 | 2,800,000 VNĐ | CONFIRMED |
| 41 | User 1 | 2026-08-21 | 2026-08-24 | 2,250,000 VNĐ | CONFIRMED |
| 42 | User 2 | 2026-08-23 | 2026-08-26 | 7,500,000 VNĐ | CONFIRMED |
| 43 | User 3 | 2026-08-05 | 2026-08-08 | 4,200,000 VNĐ | COMPLETED |
| 44 | User 4 | 2026-08-24 | 2026-08-26 | 1,500,000 VNĐ | CONFIRMED |
| 45 | User 5 | 2026-08-09 | 2026-08-14 | 32,500,000 VNĐ | COMPLETED |
| 46 | User 6 | 2026-08-31 | 2026-09-02 | 1,900,000 VNĐ | PENDING |
| 47 | User 7 | 2026-08-10 | 2026-08-12 | 5,000,000 VNĐ | COMPLETED |
| 48 | User 8 | 2026-08-18 | 2026-08-20 | 1,000,000 VNĐ | CONFIRMED |
| 49 | User 9 | 2026-08-03 | 2026-08-07 | 10,000,000 VNĐ | COMPLETED |
| 50 | User 10 | 2026-08-19 | 2026-08-23 | 3,800,000 VNĐ | CONFIRMED |

---

### 7. Danh sach 20 hoa don tai chinh (Invoices)

| ID | Booking ID | Nhan vien | Ngay xuat | Tien thue VAT | Chiet khau | Tong thuc thu | Trang thai |
|---|---|---|---|---|---|---|---|
| 1 | Booking 2 | Staff 4 | 2026-08-17 12:00:00 | 3,250,000.0 VNĐ | 100,000.0 VNĐ | 35,750,000.0 VNĐ | PAID |
| 2 | Booking 3 | Staff 3 | 2026-08-24 12:00:00 | 210,000.0 VNĐ | 100,000.0 VNĐ | 2,310,000.0 VNĐ | UNPAID |
| 3 | Booking 4 | Staff 3 | 2026-08-13 12:00:00 | 560,000.0 VNĐ | 100,000.0 VNĐ | 6,360,000.0 VNĐ | PAID |
| 4 | Booking 5 | Staff 2 | 2026-08-16 12:00:00 | 360,000.0 VNĐ | 100,000.0 VNĐ | 4,310,000.0 VNĐ | PAID |
| 5 | Booking 8 | Staff 2 | 2026-08-26 12:00:00 | 360,000.0 VNĐ | 100,000.0 VNĐ | 4,360,000.0 VNĐ | UNPAID |
| 6 | Booking 9 | Staff 2 | 2026-08-05 12:00:00 | 210,000.0 VNĐ | 100,000.0 VNĐ | 2,510,000.0 VNĐ | PAID |
| 7 | Booking 10 | Staff 5 | 2026-08-07 12:00:00 | 420,000.0 VNĐ | 100,000.0 VNĐ | 4,870,000.0 VNĐ | PAID |
| 8 | Booking 11 | Staff 1 | 2026-08-14 12:00:00 | 900,000.0 VNĐ | 100,000.0 VNĐ | 10,300,000.0 VNĐ | PAID |
| 9 | Booking 12 | Staff 1 | 2026-08-23 12:00:00 | 1,300,000.0 VNĐ | 100,000.0 VNĐ | 14,200,000.0 VNĐ | UNPAID |
| 10 | Booking 13 | Staff 4 | 2026-08-16 12:00:00 | 280,000.0 VNĐ | 100,000.0 VNĐ | 3,480,000.0 VNĐ | PAID |
| 11 | Booking 14 | Staff 1 | 2026-08-18 12:00:00 | 95,000.0 VNĐ | 0.0 VNĐ | 1,545,000.0 VNĐ | UNPAID |
| 12 | Booking 15 | Staff 3 | 2026-08-18 12:00:00 | 525,000.0 VNĐ | 100,000.0 VNĐ | 5,875,000.0 VNĐ | PAID |
| 13 | Booking 16 | Staff 2 | 2026-08-15 12:00:00 | 1,300,000.0 VNĐ | 100,000.0 VNĐ | 14,550,000.0 VNĐ | PAID |
| 14 | Booking 17 | Staff 2 | 2026-08-18 12:00:00 | 225,000.0 VNĐ | 100,000.0 VNĐ | 2,525,000.0 VNĐ | UNPAID |
| 15 | Booking 18 | Staff 4 | 2026-08-10 12:00:00 | 500,000.0 VNĐ | 100,000.0 VNĐ | 5,850,000.0 VNĐ | PAID |
| 16 | Booking 19 | Staff 4 | 2026-08-17 12:00:00 | 220,000.0 VNĐ | 100,000.0 VNĐ | 2,770,000.0 VNĐ | UNPAID |
| 17 | Booking 20 | Staff 3 | 2026-08-18 12:00:00 | 375,000.0 VNĐ | 100,000.0 VNĐ | 4,075,000.0 VNĐ | PAID |
| 18 | Booking 21 | Staff 3 | 2026-08-26 12:00:00 | 1,000,000.0 VNĐ | 100,000.0 VNĐ | 11,100,000.0 VNĐ | UNPAID |
| 19 | Booking 23 | Staff 1 | 2026-08-08 12:00:00 | 2,000,000.0 VNĐ | 100,000.0 VNĐ | 22,050,000.0 VNĐ | PAID |
| 20 | Booking 24 | Staff 4 | 2026-08-16 12:00:00 | 1,100,000.0 VNĐ | 100,000.0 VNĐ | 12,500,000.0 VNĐ | PAID |

---

### 8. Danh sach 50 thanh toan (Payments)

| ID | Invoice ID | Cong thanh toan | So tien tra | Ma giao dich doi chieu | Thoi diem | Trang thai |
|---|---|---|---|---|---|---|
| 1 | Invoice 1 | CREDIT_CARD | 35,750,000.0 VNĐ | `VNP502544` | 2026-08-17 12:00:00 | SUCCESS |
| 2 | Invoice 2 | BANK_TRANSFER | 693,000.0 VNĐ | `VNP380718` | 2026-08-24 12:00:00 | SUCCESS |
| 3 | Invoice 3 | BANK_TRANSFER | 6,360,000.0 VNĐ | `VNP167414` | 2026-08-13 12:00:00 | SUCCESS |
| 4 | Invoice 4 | BANK_TRANSFER | 1,293,000.0 VNĐ | `VNP610866` | 2026-08-16 12:00:00 | SUCCESS |
| 5 | Invoice 5 | CASH | 4,360,000.0 VNĐ | `CASH_COUNTER` | 2026-08-26 12:00:00 | SUCCESS |
| 6 | Invoice 6 | CASH | 753,000.0 VNĐ | `CASH_COUNTER` | 2026-08-05 12:00:00 | SUCCESS |
| 7 | Invoice 7 | CASH | 4,870,000.0 VNĐ | `CASH_COUNTER` | 2026-08-07 12:00:00 | SUCCESS |
| 8 | Invoice 8 | CASH | 3,090,000.0 VNĐ | `CASH_COUNTER` | 2026-08-14 12:00:00 | SUCCESS |
| 9 | Invoice 9 | CASH | 14,200,000.0 VNĐ | `CASH_COUNTER` | 2026-08-23 12:00:00 | SUCCESS |
| 10 | Invoice 10 | CASH | 1,044,000.0 VNĐ | `CASH_COUNTER` | 2026-08-16 12:00:00 | SUCCESS |
| 11 | Invoice 11 | CREDIT_CARD | 1,545,000.0 VNĐ | `VNP879376` | 2026-08-18 12:00:00 | SUCCESS |
| 12 | Invoice 12 | CREDIT_CARD | 1,762,500.0 VNĐ | `VNP452536` | 2026-08-18 12:00:00 | SUCCESS |
| 13 | Invoice 13 | BANK_TRANSFER | 14,550,000.0 VNĐ | `VNP958524` | 2026-08-15 12:00:00 | SUCCESS |
| 14 | Invoice 14 | CASH | 757,500.0 VNĐ | `CASH_COUNTER` | 2026-08-18 12:00:00 | SUCCESS |
| 15 | Invoice 15 | BANK_TRANSFER | 5,850,000.0 VNĐ | `VNP586506` | 2026-08-10 12:00:00 | SUCCESS |
| 16 | Invoice 16 | BANK_TRANSFER | 831,000.0 VNĐ | `VNP587778` | 2026-08-17 12:00:00 | SUCCESS |
| 17 | Invoice 17 | BANK_TRANSFER | 4,075,000.0 VNĐ | `VNP628356` | 2026-08-18 12:00:00 | SUCCESS |
| 18 | Invoice 18 | BANK_TRANSFER | 3,330,000.0 VNĐ | `VNP128631` | 2026-08-26 12:00:00 | SUCCESS |
| 19 | Invoice 19 | CREDIT_CARD | 22,050,000.0 VNĐ | `VNP269760` | 2026-08-08 12:00:00 | SUCCESS |
| 20 | Invoice 20 | CREDIT_CARD | 3,750,000.0 VNĐ | `VNP768346` | 2026-08-16 12:00:00 | SUCCESS |
| 21 | Invoice 1 | CASH | 35,750,000.0 VNĐ | `CASH_COUNTER` | 2026-08-17 12:00:00 | SUCCESS |
| 22 | Invoice 2 | BANK_TRANSFER | 693,000.0 VNĐ | `VNP964767` | 2026-08-24 12:00:00 | SUCCESS |
| 23 | Invoice 3 | CASH | 6,360,000.0 VNĐ | `CASH_COUNTER` | 2026-08-13 12:00:00 | SUCCESS |
| 24 | Invoice 4 | BANK_TRANSFER | 1,293,000.0 VNĐ | `VNP921473` | 2026-08-16 12:00:00 | SUCCESS |
| 25 | Invoice 5 | CREDIT_CARD | 4,360,000.0 VNĐ | `VNP908010` | 2026-08-26 12:00:00 | SUCCESS |
| 26 | Invoice 6 | CREDIT_CARD | 753,000.0 VNĐ | `VNP357012` | 2026-08-05 12:00:00 | SUCCESS |
| 27 | Invoice 7 | CREDIT_CARD | 4,870,000.0 VNĐ | `VNP353417` | 2026-08-07 12:00:00 | SUCCESS |
| 28 | Invoice 8 | CASH | 3,090,000.0 VNĐ | `CASH_COUNTER` | 2026-08-14 12:00:00 | SUCCESS |
| 29 | Invoice 9 | BANK_TRANSFER | 14,200,000.0 VNĐ | `VNP314394` | 2026-08-23 12:00:00 | SUCCESS |
| 30 | Invoice 10 | CREDIT_CARD | 1,044,000.0 VNĐ | `VNP896851` | 2026-08-16 12:00:00 | SUCCESS |
| 31 | Invoice 11 | CREDIT_CARD | 1,545,000.0 VNĐ | `VNP256750` | 2026-08-18 12:00:00 | SUCCESS |
| 32 | Invoice 12 | CASH | 1,762,500.0 VNĐ | `CASH_COUNTER` | 2026-08-18 12:00:00 | SUCCESS |
| 33 | Invoice 13 | BANK_TRANSFER | 14,550,000.0 VNĐ | `VNP115073` | 2026-08-15 12:00:00 | SUCCESS |
| 34 | Invoice 14 | CREDIT_CARD | 757,500.0 VNĐ | `VNP996859` | 2026-08-18 12:00:00 | SUCCESS |
| 35 | Invoice 15 | CREDIT_CARD | 5,850,000.0 VNĐ | `VNP664360` | 2026-08-10 12:00:00 | SUCCESS |
| 36 | Invoice 16 | BANK_TRANSFER | 831,000.0 VNĐ | `VNP926106` | 2026-08-17 12:00:00 | SUCCESS |
| 37 | Invoice 17 | CASH | 4,075,000.0 VNĐ | `CASH_COUNTER` | 2026-08-18 12:00:00 | SUCCESS |
| 38 | Invoice 18 | CASH | 3,330,000.0 VNĐ | `CASH_COUNTER` | 2026-08-26 12:00:00 | SUCCESS |
| 39 | Invoice 19 | BANK_TRANSFER | 22,050,000.0 VNĐ | `VNP372917` | 2026-08-08 12:00:00 | SUCCESS |
| 40 | Invoice 20 | CASH | 3,750,000.0 VNĐ | `CASH_COUNTER` | 2026-08-16 12:00:00 | SUCCESS |
| 41 | Invoice 1 | CREDIT_CARD | 35,750,000.0 VNĐ | `VNP801842` | 2026-08-17 12:00:00 | SUCCESS |
| 42 | Invoice 2 | CREDIT_CARD | 693,000.0 VNĐ | `VNP638634` | 2026-08-24 12:00:00 | SUCCESS |
| 43 | Invoice 3 | CREDIT_CARD | 6,360,000.0 VNĐ | `VNP567365` | 2026-08-13 12:00:00 | SUCCESS |
| 44 | Invoice 4 | CASH | 1,293,000.0 VNĐ | `CASH_COUNTER` | 2026-08-16 12:00:00 | SUCCESS |
| 45 | Invoice 5 | CASH | 4,360,000.0 VNĐ | `CASH_COUNTER` | 2026-08-26 12:00:00 | SUCCESS |
| 46 | Invoice 6 | CREDIT_CARD | 753,000.0 VNĐ | `VNP227448` | 2026-08-05 12:00:00 | SUCCESS |
| 47 | Invoice 7 | BANK_TRANSFER | 4,870,000.0 VNĐ | `VNP938645` | 2026-08-07 12:00:00 | SUCCESS |
| 48 | Invoice 8 | CREDIT_CARD | 3,090,000.0 VNĐ | `VNP404597` | 2026-08-14 12:00:00 | SUCCESS |
| 49 | Invoice 9 | BANK_TRANSFER | 14,200,000.0 VNĐ | `VNP676370` | 2026-08-23 12:00:00 | SUCCESS |
| 50 | Invoice 10 | BANK_TRANSFER | 1,044,000.0 VNĐ | `VNP565793` | 2026-08-16 12:00:00 | SUCCESS |

---

### 9. Danh sach 20 danh gia (Reviews)

| ID | Booking ID | User ID | Sao danh gia | Nhan xet chi tiet | Ngay binh luan |
|---|---|---|---|---|---|
| 1 | Booking 1 | User 1 | 5 Sao | "Dich vu tot, nhan vien than thien." | 2026-09-01 15:30:00 |
| 2 | Booking 2 | User 2 | 5 Sao | "Buffet sang ngon mieng, phong thoang khi." | 2026-08-17 15:30:00 |
| 3 | Booking 3 | User 3 | 3 Sao | "Le tan check-in nhanh nhen." | 2026-08-24 15:30:00 |
| 4 | Booking 4 | User 4 | 4 Sao | "Xung dang tien phong bo ra." | 2026-08-13 15:30:00 |
| 5 | Booking 5 | User 5 | 4 Sao | "Hoi on mot chut vao buoi sang nhung van chap nhan duoc." | 2026-08-16 15:30:00 |
| 6 | Booking 6 | User 6 | 5 Sao | "Goc nhin huong pho tuyet voi, giuong nam em ai." | 2026-09-01 15:30:00 |
| 7 | Booking 7 | User 7 | 3 Sao | "Xung dang tien phong bo ra." | 2026-09-01 15:30:00 |
| 8 | Booking 8 | User 8 | 5 Sao | "Goc nhin huong pho tuyet voi, giuong nam em ai." | 2026-08-26 15:30:00 |
| 9 | Booking 9 | User 9 | 5 Sao | "Khach san rat dep, bon tam rong rai." | 2026-08-05 15:30:00 |
| 10 | Booking 10 | User 10 | 5 Sao | "Mini-bar hoi dat tien nhung phong don rat sach." | 2026-08-07 15:30:00 |
| 11 | Booking 11 | User 11 | 5 Sao | "Dich vu tot, nhan vien than thien." | 2026-08-14 15:30:00 |
| 12 | Booking 12 | User 12 | 4 Sao | "Buffet sang ngon mieng, phong thoang khi." | 2026-08-23 15:30:00 |
| 13 | Booking 13 | User 13 | 5 Sao | "Hoi on mot chut vao buoi sang nhung van chap nhan duoc." | 2026-08-16 15:30:00 |
| 14 | Booking 14 | User 14 | 4 Sao | "Mini-bar hoi dat tien nhung phong don rat sach." | 2026-08-18 15:30:00 |
| 15 | Booking 15 | User 15 | 4 Sao | "Goc nhin huong pho tuyet voi, giuong nam em ai." | 2026-08-18 15:30:00 |
| 16 | Booking 16 | User 16 | 5 Sao | "Le tan check-in nhanh nhen." | 2026-08-15 15:30:00 |
| 17 | Booking 17 | User 17 | 3 Sao | "Mini-bar hoi dat tien nhung phong don rat sach." | 2026-08-18 15:30:00 |
| 18 | Booking 18 | User 18 | 4 Sao | "Buffet sang ngon mieng, phong thoang khi." | 2026-08-10 15:30:00 |
| 19 | Booking 19 | User 19 | 5 Sao | "Le tan check-in nhanh nhen." | 2026-08-17 15:30:00 |
| 20 | Booking 20 | User 20 | 3 Sao | "Dich vu tot, nhan vien than thien." | 2026-08-18 15:30:00 |


---


# FILE: Test_Cases_Specification.md

# Test Case Specification - Hotel Management System
## QA Test Cases Log (Total: 120 Cases)

| Test Case ID | Module | Chức năng | Mục tiêu | Điều kiện tiên quyết | Dữ liệu kiểm thử | Các bước thực hiện | Kết quả mong đợi | Kết quả thực tế | Trạng thái | Priority |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| TC001 | Authentication | Login | Đăng nhập thành công | Tài khoản khách hàng đã đăng ký | email="user@test.com", pass="123456" | 1. Nhập email\n2. Nhập password\n3. Click Login Button | Đăng nhập thành công, nhận Access Token |  |  | High |
| TC002 | Authentication | Login | Kiểm tra định dạng email | Form đăng nhập mở sẵn | email="user_at_test.com", pass="123456" | 1. Nhập email sai định dạng\n2. Nhập pass\n3. Click Login | Thông báo lỗi định dạng email |  |  | Medium |
| TC003 | Authentication | Login | Sai mật khẩu đăng nhập | Tài khoản tồn tại | email="user@test.com", pass="wrongpass" | 1. Nhập email\n2. Nhập mật khẩu sai\n3. Click Login | Thông báo cảnh báo thông tin đăng nhập sai (401) |  |  | High |
| TC004 | Authentication | Register | Đăng ký thành công | Form đăng ký mở sẵn | fullName="Jane", email="new@test.com", pass="123456", phone="0911223344" | 1. Điền thông tin vào form đăng ký\n2. Click Register | Đăng ký thành công, tự động lưu DB |  |  | High |
| TC005 | Authentication | Register | Đăng ký email đã tồn tại | Email đã có sẵn trong hệ thống | email="user@test.com" đã tồn tại | 1. Nhập các trường\n2. Nhập email trùng lặp\n3. Click Register | Báo lỗi email đã được đăng ký |  |  | High |
| TC006 | Authentication | Register | Ràng buộc mật khẩu yếu | Form đăng ký mở | password="123" (dưới 6 ký tự) | 1. Điền các trường\n2. Nhập mật khẩu ngắn\n3. Click Register | Báo lỗi validate mật khẩu yếu |  |  | Medium |
| TC007 | Authentication | Register | Độ dài biên số điện thoại | Form đăng ký mở | phone="091" hoặc "0123456789123456" | 1. Điền các trường\n2. Nhập phone quá ngắn/dài\n3. Đăng ký | Báo lỗi validation độ dài số điện thoại |  |  | Boundary |
| TC008 | Authentication | Security | Truy cập không token | Endpoint được bảo vệ | N/A | 1. Gọi API /users mà không truyền Header Authorization | Mã code 401 Unauthorized |  |  | High |
| TC009 | Authentication | Authentication | Hoán đổi Refresh Token | Có Refresh Token hợp lệ | Refresh Token | 1. Gọi API /auth/refresh kèm Refresh Token | Trả ra Access Token mới dạng 200 OK |  |  | High |
| TC010 | Authentication | Security | Token hết hạn | Token cũ tạo từ 1 ngày trước | Expired JWT Token | 1. Gọi API bất kỳ kèm Token hết hạn | Báo lỗi 401 Access Denied |  |  | High |
| TC011 | User | User List | Lấy danh sách người dùng | Đằng nhập quyền ADMIN | Token ADMIN | 1. Gọi API GET /users | Trả ra danh sách 200 OK |  |  | Medium |
| TC012 | User | User List | Phân quyền người dùng thường | Đăng nhập quyền CUSTOMER | Token CUSTOMER | 1. Gọi API GET /users | Trả về 403 Forbidden |  |  | High |
| TC013 | User | User Detail | Truy vấn cá thể người dùng | ID người dùng tồn tại | ID = 1 | 1. Gọi GET /users/1 | Trả về thông tin chi tiết người dùng |  |  | High |
| TC014 | User | User Detail | Truy vấn ID vô định | Tìm kiếm ID không có trong DB | ID = 9999 | 1. Gọi GET /users/9999 | Báo lỗi ResourceNotFoundException |  |  | Medium |
| TC015 | User | User Lock | Khoá tài khoản vi phạm | Tài khoản khách có sẵn | ID = 2 | 1. Gọi PUT /users/2/block với quyền ADMIN | Khoá tài khoản thành công |  |  | High |
| TC016 | User | User Lock | Khoá tài khoản sai quyền | Đăng nhập quyền RECEPTIONIST | ID = 2 | 1. Gọi PUT /users/2/block | Báo lỗi 403 Forbidden |  |  | High |
| TC017 | User | User Edit | Sửa đổi thiếu tên | Tài khoản có sẵn | fullName = blank | 1. Gọi PUT /users/1 cập nhật profile | Báo lỗi validate tên trống |  |  | Medium |
| TC018 | User | Loyalty | Biên điểm tích lũy dương | Tài khoản đang có 0 điểm | loyaltyPoints = 100 | 1. Tích lũy điểm khi lưu trú | Điểm tăng lên 100 |  |  | Boundary |
| TC019 | User | Loyalty | Điểm tích lũy âm | Điểm trị giá âm | loyaltyPoints = -10 | 1. Truyền điểm âm vào cơ sở dữ liệu | Báo lỗi ràng buộc dữ liệu |  |  | Validation |
| TC020 | User | User Update | Thay đổi email bị trùng | Email mới trùng email khác | email = user@test.com | 1. Cập nhật hồ sơ bằng email trùng | Báo lỗi cập nhật thông tin trùng lặp |  |  | High |
| TC021 | Employee | Employee CRUD | Tạo nhân viên thành công | Quyền ADMIN | EmployeeCode='EMP001' | 1. POST /employees với thông tin đầy đủ | Tạo thành công mã 200 OK |  |  | High |
| TC022 | Employee | Employee CRUD | Trùng mã nhân viên | Mã EMP001 đã có | EmployeeCode='EMP001' | 1. Đăng ký nhân viên trùng mã | Báo lỗi BadRequest trùng mã Code |  |  | High |
| TC023 | Employee | Employee CRUD | Lương phụ cấp dạng âm | Mức lương không hợp lệ | salaryRate = -500.0 | 1. Đăng ký lương âm | Báo lỗi validation lương trị số dương |  |  | Validation |
| TC024 | Employee | Employee CRUD | Mật khẩu nhân sự ngắn | Đăng ký mật khẩu | password = '123' | 1. Đăng ký mật khẩu ngắn | Báo lỗi validation mật khẩu yếu |  |  | Boundary |
| TC025 | Employee | Employee CRUD | Thay đổi vị trí công việc | Nhân viên có sẵn | ID = 1 | 1. PUT /employees/1 thay đổi chức vụ | Cập nhật chức vụ mới thành công |  |  | Medium |
| TC026 | Employee | Employee List | Tìm kiếm theo mã số | Nhân viên trùng khớp | Code = 'EMP001' | 1. GET /employees theo Code | Trả lại thông tin nhân sự |  |  | Medium |
| TC027 | Employee | Employee Delete | Xoá nhân sự thành công | Đăng nhập ADMIN | ID = 1 | 1. DELETE /employees/1 | Xoá thành công nhân sự |  |  | High |
| TC028 | Employee | Employee Delete | Xoá nhân sự sai quyền | Đăng nhập CUSTOMER | ID = 1 | 1. DELETE /employees/1 | Mã từ chối 403 Forbidden |  |  | High |
| TC029 | Employee | Employee CRUD | Lương biên lớn nhất | Giá trị lương biên | salaryRate = 999999999 | 1. Thiết lập lương cực đại | Hệ thống ghi nhận bình thường |  |  | Boundary |
| TC030 | Employee | Employee Retrieve | Truy vấn ID ảo | ID không tồn tại | ID = 9999 | 1. Gọi GET /employees/9999 | Báo lỗi ResourceNotFound |  |  | Medium |
| TC031 | Room Type | RoomType CRUD | Tạo mới loại phòng | Loại phòng Standard | Type='STD' | 1. POST /room-types loại phòng mới | Tạo thành công |  |  | High |
| TC032 | Room Type | RoomType CRUD | Trùng tên loại phòng | STD đã tồn tại | Type='STD' | 1. Tạo phòng trùng tên | Báo lỗi trùng lặp |  |  | Medium |
| TC033 | Room Type | RoomType CRUD | Giá thuê âm | Giá âm không hợp lệ | basePrice = -200 | 1. Điền giá phòng âm | Báo lỗi validation giá trị |  |  | Validation |
| TC034 | Room Type | RoomType CRUD | Sức chứa biên bằng 0 | Sức chứa tối thiểu | maxCapacity = 0 | 1. Đăng ký sức chứa bằng 0 | Báo lỗi trị số nhỏ hơn 1 |  |  | Boundary |
| TC035 | Room Type | RoomType CRUD | Cập nhật tiện nghi phòng | Loại phòng có sẵn | ID = 1 | 1. PUT /room-types/1 đổi Tiện nghi | Thông tin tiện nghi thay đổi |  |  | Medium |
| TC036 | Room Type | RoomType Delete | Xoá loại phòng đang được dùng | Phòng đang chứa khách | ID = 1 | 1. DELETE /room-types/1 | Từ chối xoá vì ràng buộc ngoại khoá |  |  | High |
| TC037 | Room Type | RoomType Delete | Xoá loại phòng trống | Không phòng nào dùng loại này | ID = 2 | 1. DELETE /room-types/2 | Xoá thành công loại phòng |  |  | High |
| TC038 | Room Type | RoomType List | Lấy toàn danh mục hạng phòng | N/A | N/A | 1. GET /room-types | Hiển thị danh sách đầy đủ |  |  | Medium |
| TC039 | Room Type | RoomType CRUD | Diện tích buồng cực đại | Giá trị biên lớn | sizeM2 = 1000 | 1. Đăng ký phòng rộng 1000m² | Hệ thống ghi nhận bình thường |  |  | Boundary |
| TC040 | Room Type | RoomType Detail | Truy vấn ID vô dụng | Hạng phòng không tồn tại | ID = 9999 | 1. Gọi GET /room-types/9999 | Báo lỗi không tìm thấy hạng phòng |  |  | Medium |
| TC041 | Room | Room CRUD | Tạo buồng phòng thành công | Loại phòng ID tồn tại | RoomNumber='101' | 1. POST /rooms thêm phòng mới | Tạo thành công phòng |  |  | High |
| TC042 | Room | Room CRUD | Trùng số phòng nghỉ | Buồng 101 đã có | RoomNumber='101' | 1. Tạo buồng phòng trùng số | Báo lỗi trùng số phòng |  |  | High |
| TC043 | Room | Room CRUD | Lầu/Tầng trị số âm | Vị trí không thể âm | floor = -1 | 1. Đặt thuộc tính tầng lửng âm | Báo lỗi validation tầng tối thiểu 1 |  |  | Validation |
| TC044 | Room | Room CRUD | Mã trạng thái phòng sai | Trạng thái ảo | status='TEST_CLEAN' | 1. Nhập trạng thái không theo quy định | Báo lỗi validation |  |  | Medium |
| TC045 | Room | Room CRUD | Cập nhật dọn dẹp phòng | Phòng đã trả | ID = 1 | 1. Đổi status sang VACANT_CLEAN | Ghi nhận trạng thái mới |  |  | High |
| TC046 | Room | Room List | Lọc buồng theo Hạng | Hạng phòng STD có sẵn | Type = 'STD' | 1. Gọi GET /rooms lọc hạng STD | Hiển thị các buồng thuộc STD |  |  | Medium |
| TC047 | Room | Room Delete | Xoá phòng thành công | Không có khách trú ngụ | ID = 1 | 1. DELETE /rooms/1 | Phòng bị xoá khỏi DB |  |  | High |
| TC048 | Room | Room Delete | Xoá phòng đang có khách | Phòng OCCUPIED | ID = 2 | 1. DELETE /rooms/2 | Từ chối xoá buồng |  |  | High |
| TC049 | Room | Room CRUD | Gán Hạng phòng rỗng | RoomType ID rỗng | RoomTypeId = 999 | 1. Tạo phòng nối ID hạng rỗng | Báo lỗi không tồn tại loại phòng |  |  | High |
| TC050 | Room | Room Detail | Truy vấn buồng vô ngã | Buồng ID không có thật | ID = 9999 | 1. Gọi GET /rooms/9999 | Báo lỗi ResourceNotFound |  |  | Medium |
| TC051 | Booking | Booking Creation | Đặt buồng đơn lẻ | Phòng trống dọn sẵn | RoomID = 1 | 1. POST /bookings đăng ký buồng | Đặt thành công trạng thái PENDING |  |  | High |
| TC052 | Booking | Booking Creation | Đặt buồng tập thể | Nhiều phòng trống sẵn có | RoomID = 1, 2 | 1. Đăng ký danh sách 2 phòng | Thành công, tính phí tổng gộp |  |  | High |
| TC053 | Booking | Booking Creation | Đặt phòng ID hoang đường | ID phòng ảo | RoomID = 999 | 1. POST đặt phòng với ID 999 | Báo lỗi không tồn tại buồng |  |  | High |
| TC054 | Booking | Booking Creation | Đặt buồng trùng lặp lịch | Buồng đang bận thời điểm đó | RoomID = 1, date trùng | 1. Đặt lịch trùng với lượt ở khác | Báo lỗi phòng đang bận sử dụng |  |  | High |
| TC055 | Booking | Booking Creation | Lịch đặt vào quá khứ | Ngày check-in đã trễ | startDate = ngày hôm qua | 1. Thiết lập lịch quá khứ | Báo lỗi ngày không hợp lệ |  |  | Validation |
| TC056 | Booking | Booking Creation | Ngày check-out trước check-in | Ngày Check-in sau Check-out | check-in > check-out | 1. Đặt ngày kết thúc trước ngày đầu | Báo lỗi khoảng ngày biên |  |  | Boundary |
| TC057 | Booking | Booking Creation | Thời gian lưu trú biên 1 ngày | Lưu trú tối thiểu | stay = 1 day | 1. Thiết lập check-in check-out cách 1 ngày | Đăng ký thành công |  |  | Boundary |
| TC058 | Booking | Booking Cancel | Huỷ đặt phòng | Trạng thái phòng PENDING | BookingId = 1 | 1. PUT /bookings/1/cancel | Huỷ đặt chỗ, chuyển status CANCELLED |  |  | High |
| TC059 | Booking | Booking Cancel | Huỷ buồng khi đang thuê | Status CHECKED_IN | BookingId = 2 | 1. Huỷ lượt phòng đang dắt khách ở | Hệ thống từ chối huỷ |  |  | High |
| TC060 | Booking | Booking List | Lấy lịch sử khách hàng | Khách hàng đăng nhập | Token CUSTOMER | 1. Xem danh sách đặt phòng | Hiển thị lịch sử cá nhân thành công |  |  | Medium |
| TC061 | Booking Detail | Check in/out | Khách nhận buồng check-in | Lịch đặt phòng hợp lệ | DetailId = 1 | 1. PUT /bookings/details/1/checkin | Cập nhật checkInActual, đổi phòng sang OCCUPIED |  |  | High |
| TC062 | Booking Detail | Check in/out | Nhận phòng đang bận | Phòng trạng thái OCCUPIED | DetailId = 2 | 1. Thực hiện check-in phòng OCCUPIED | Báo lỗi buồng đang có khách |  |  | High |
| TC063 | Booking Detail | Check in/out | Hành khách trả phòng check-out | Phòng đang OCCUPIED | DetailId = 1 | 1. PUT /bookings/details/1/checkout | Cập nhật checkOutActual, đổi phòng sang VACANT_DIRTY |  |  | High |
| TC064 | Booking Detail | Check in/out | Nhận phòng check-out 2 lần | Phòng đã CHECKED_OUT | DetailId = 1 | 1. Gọi check-out thêm lần nữa | Báo lỗi lượt ở đã kết thúc |  |  | Medium |
| TC065 | Booking Detail | Check in/out | Check-in sớm hơn lịch | Check-in sớm 2 tiếng | checkIn = sớm | 1. Lập thủ tục nhận phòng sớm | Ghi nhận hợp lệ |  |  | Boundary |
| TC066 | Booking Detail | Check in/out | Check-out trễ hơn lịch | Check-out muộn 3 tiếng | checkOut = trễ | 1. Lập thủ tục trả phòng trễ | Ghi nhận hợp lệ |  |  | Boundary |
| TC067 | Booking Detail | Check in/out | Lấy tình trạng đặt phòng | Lịch chi tiết đang có | DetailId = 1 | 1. Xem chi tiết đặt phòng | Trả về đầy đủ thông tin lưu trú |  |  | Medium |
| TC068 | Booking Detail | Check in/out | Thời điểm check-in tương lai | Check-in tương lai | checkInActual = ngày mai | 1. Truyền giờ nhận phòng tương lai | Báo lỗi validation thời gian |  |  | Validation |
| TC069 | Booking Detail | Check in/out | Trả phòng khi chưa nhận phòng | Chưa có check-in actual | DetailId = 4 | 1. Tiến hành check-out phòng chưa check-in | Báo lỗi chu kỳ sử dụng phòng |  |  | High |
| TC070 | Booking Detail | Check in/out | Hiển thị thông tin buồng ID | Buồng ID tồn tại | DetailId = 1 | 1. Xem danh sách chi tiết đặt buồng | Hiển thị tương ứng |  |  | Medium |
| TC071 | Payment | Payment CRUD | Thanh toán thành công | Hoá đơn chưa trả phí | InvoiceID = 1, amount = $100 | 1. POST /payments thanh toán phí | Lưu hoá đơn PAID thành công, tạo payment |  |  | High |
| TC072 | Payment | Payment CRUD | Chi trả hoá đơn đã đóng | Hoá đơn trạng thái PAID | InvoiceID = 2 | 1. Giao dịch thanh toán lần 2 | Từ chối giao dịch vì hoá đơn đã thanh toán |  |  | High |
| TC073 | Payment | Payment CRUD | Giá trị thanh toán siêu nhỏ | Thanh toán giá trị 0.01 | paymentAmount = 0.01 | 1. Thanh toán giá trị cực tiểu | Từ chối vì nhỏ hơn số tiền hoá đơn |  |  | Boundary |
| TC074 | Payment | Payment CRUD | Chi trả thừa tiền | Thanh toán dư tiền hóa đơn | amount dư | 1. Nhập số tiền chi trả vượt hoá đơn | Hệ thống ghi nhận thành công |  |  | Boundary |
| TC075 | Payment | Payment CRUD | Phương thức chi trả ảo | Phương thức sai quy định | method='GOLD' | 1. Cung cấp phương thức thanh toán sai | Báo lỗi validation |  |  | Validation |
| TC076 | Payment | Payment CRUD | Bỏ trống mã giao dịch | Thanh toán bằng Tiền mặt | transactionId = null | 1. Thực hiện thanh toán cash | Thanh toán thành công |  |  | Medium |
| TC077 | Payment | Payment List | Truy vấn danh mục thu tiền | Token ADMIN | N/A | 1. Xem tất cả các lượt giao dịch | Hiển thị danh sách giao dịch |  |  | Medium |
| TC078 | Payment | Payment Detail | Tìm kiếm giao dịch ID | Giao dịch có sẵn | ID = 1 | 1. Tra cứu chi tiết giao dịch 1 | Hiển thị chi tiết hóa đơn thanh toán |  |  | Medium |
| TC079 | Payment | Payment CRUD | Thanh toán hóa đơn không có thật | ID hoá đơn ảo | InvoiceID = 999 | 1. Gọi API thanh toán cho hoá đơn 999 | Báo lỗi ResourceNotFound |  |  | High |
| TC080 | Payment | Security | Tấn công SQL Injection | Phương thức thanh toán chứa ký tự SQL | paymentMethod = 'CASH; DROP TABLE payments; --' | 1. Truyền Payload SQL phá hoại | An toan, không ảnh hưởng cơ sở dữ liệu |  |  | High |
| TC081 | Invoice | Invoice CRUD | Kết xuất hoá đơn buồng phòng | Lượt đặt phòng tồn tại | BookingID = 1 | 1. POST /invoices xuất hóa đơn | Hoá đơn tính toán tổng số tiền |  |  | High |
| TC082 | Invoice | Invoice CRUD | Xuất hoá đơn bị trùng lặp | Đặt phòng đã xuất hóa đơn | BookingID = 1 | 1. Yêu cầu xuất hóa đơn lần nữa | Báo lỗi hoá đơn đặt phòng đã tồn tại |  |  | High |
| TC083 | Invoice | Invoice CRUD | Voucher chiết khấu âm | Thất thoát tiền | discountAmount = -50 | 1. Nhập chiết khấu âm | Báo lỗi validation |  |  | Validation |
| TC084 | Invoice | Invoice CRUD | Chiết khấu vượt trên tổng trả | Biên mức chiết khấu | discount > totalAmount | 1. Nhập số tiền chiết khấu lớn hơn hóa đơn | Ghi nhận hoá đơn trị giá Final = 0 |  |  | Boundary |
| TC085 | Invoice | Invoice Detail | Tra cứu chi tiết hoá đơn | Nhân viên phòng ban | InvoiceID = 1 | 1. Tra cứu hoá đơn 1 | Hiển thị đầy đủ tiền gốc, thuế 10%, mã đặt phòng |  |  | Medium |
| TC086 | Invoice | Invoice Calc | Thuế 10% tính chuẩn | Lượt tính toán tổng | total = 100 | 1. Phát hành hoá đơn mẫu | Tính chính xác thuế = 10 |  |  | High |
| TC087 | Invoice | Invoice CRUD | Xuất hoá đơn mã đặt chỗ ảo | Booking ID không tồn tại | BookingID = 999 | 1. Gọi kết xuất hoá đơn mã 999 | Báo lỗi không tồn tại booking detail |  |  | High |
| TC088 | Invoice | Security | Thay đổi giá trị hoá đơn trực tiếp | Gọi sửa tổng tiền | finalAmount = 0 | 1. Hack thay đổi tổng tiền hoá đơn | Bị từ chối vì không cho phép sửa tổng tiền tuỳ tiện |  |  | High |
| TC089 | Invoice | Invoice Print | In hoá đơn PDF | Hoá đơn tồn tại | ID = 1 | 1. Gọi API in xuất hoá đơn | Thành công |  |  | Medium |
| TC090 | Invoice | Authorization | Truy cập hoá đơn lậu | Đăng nhập CUSTOMER khác | ID = 1 (của người khác) | 1. Bản tải xuống hoá đơn không thuộc quyền sở hữu | Báo lỗi 403 Forbidden hoặc trả rỗng |  |  | High |
| TC091 | Review | Review CRUD | Đăng đánh giá phòng thành công | Khách hàng đã check-out | Rating = 5, comment = Good | 1. POST /reviews đăng chất lượng | Lưu thành công feedback |  |  | High |
| TC092 | Review | Review Validation | Đánh giá quá thấp biên | Số sao bằng 0 | rating = 0 | 1. Đánh giá 0 sao | Báo lỗi validation số sao nhỏ tối thiểu 1 |  |  | Boundary |
| TC093 | Review | Review Validation | Đánh giá quá cao biên | Số sao bằng 6 | rating = 6 | 1. Đánh giá 6 sao | Báo lỗi validation số sao tối đa 5 |  |  | Boundary |
| TC094 | Review | Review CRUD | Đánh giá đặt chỗ không tồn tại | Mã đặt chỗ ảo | BookingId = 999 | 1. Đánh giá mã đặt phòng ảo | Báo lỗi không tồn tại booking |  |  | High |
| TC095 | Review | Authorization | Đánh giá đè của người khác | Đăng nhập CUSTOMER B | BookingId = 1 (của khách A) | 1. Khách B cố đăng đánh giá cho khách A | Hệ thống chặn giao dịch |  |  | High |
| TC096 | Review | Review CRUD | Đánh giá khi chưa rời phòng | Lượt ở đang PENDING | BookingId = 2 | 1. Đăng review khi phòng chưa check-out | Báo lỗi hành trình lưu trú chưa xong |  |  | High |
| TC097 | Review | Review List | Xem đánh giá theo mã buồng | Buồng ID tồn tại | BookingId = 1 | 1. Xem phản hồi chất lượng phòng | Hiển thị danh sách review |  |  | Medium |
| TC098 | Review | Review List | Xem tất cả đánh giá | N/A | N/A | 1. GET /reviews | Hiển thị tổng thể các phản hồi |  |  | Medium |
| TC099 | Review | Security | Tấn công XSS script | Bình luận chứa script html | comment = '<script>alert(1)</script>' | 1. Gửi phản hồi chứa thẻ script | Hỗ trợ lọc / Escape chữ sạch trước khi lưu |  |  | High |
| TC100 | Review | Review Delete | Xoá đánh giá lậu | ADMIN xoá | ReviewId = 1 | 1. ADMIN yêu cầu xoá review | Xoá thành công |  |  | Medium |
| TC101 | Service | Service CRUD | Thành công tạo dịch vụ spa | Token ADMIN | Price = 50.0 | 1. POST /services dịch vụ | Thành công tạo món dịch vụ mới |  |  | High |
| TC102 | Service | Service Validation | Giá dịch vụ trị số âm | Không thể thu phí âm | price = -50.0 | 1. Thiết lập giá dịch vụ thu âm | Báo lỗi validation số dương |  |  | Validation |
| TC103 | Service | Service Validation | Giá dịch vụ biên cực tiểu | Giá thuê siêu rẻ | price = 0 | 1. Thiết lập giá bằng 0 | Thành công |  |  | Boundary |
| TC104 | Service | Service Order | Khách đặt dịch vụ phụ thu | Lượt ở check-in | DetailId = 1, Quantity = 2 | 1. Gọi đồ ăn phục vụ tận phòng | Thành công, ghi nợ dịch vụ |  |  | High |
| TC105 | Service | Service Order | Số lượng gọi phục vụ âm | Số lượng sai | quantity = -2 | 1. Gọi đồ số lượng âm | Báo lỗi validation số lượng tối thiểu 1 |  |  | Validation |
| TC106 | Service | Service Order | Số lượng gọi phục vụ biên 0 | Số lượng không hợp lệ | quantity = 0 | 1. Truyền số lượng gọi món bằng 0 | Báo lỗi validation |  |  | Boundary |
| TC107 | Service | Service Order | Dịch vụ gọi mã ảo | ID dịch vụ không thật | ServiceId = 999 | 1. Gọi món dịch vụ mã 999 | Báo lỗi không tồn tại món ăn |  |  | High |
| TC108 | Service | Service List | Lấy catalogue bảng dịch vụ | N/A | N/A | 1. Xem thực đơn phụ thu dịch vụ | Hiển thị catalogue dịch vụ |  |  | Medium |
| TC109 | Service | Service Order | Đặt dịch vụ lượt buồng phòng ảo | Booking Detail ID ảo | DetailId = 999 | 1. Đặt dịch vụ cho chi tiết đặt buồng 999 | Báo lỗi ResourceNotFound |  |  | High |
| TC110 | Service | Service CRUD | Xoá món dịch vụ phụ thu | ADMIN xoá | ServiceID = 1 | 1. DELETE /services/1 | Xoá thành công |  |  | Medium |
| TC111 | Dashboard | Dashboard View | Thành công truy vấn số liệu | Nhân viên lễ tân đăng nhập | Token RECEPTIONIST | 1. Gọi GET /dashboard/stats | Trả lại số liệu chiếm phòng, doanh thu |  |  | High |
| TC112 | Dashboard | Authorization | Truy quét biểu đồ sai quyền | Đăng nhập CUSTOMER | Token CUSTOMER | 1. Gọi truy quét stats dashboard | Trả mã 403 Forbidden |  |  | High |
| TC113 | Dashboard | Performance | Truy vấn dồn dập SQL | Nhiều luồng gọi đồng thời | 100 thread concurrently | 1. Căng tải stress test dashboard API | Bộ chỉ mục index dữ liệu phản hồi dưới 2 giây |  |  | Boundary |
| TC114 | Dashboard | Dashboard Calc | Đọc đúng occupancy rate | Có 1 phòng ở trên tổng 2 phòng | Calculated state | 1. Gọi xem stats | Occupancy rate trả về đúng 50.0% |  |  | High |
| TC115 | Dashboard | Dashboard Calc | Đọc chính xác tổng doanh thu | Hóa đơn Paid ghi nhận | Sum of PAID finalAmount | 1. Gọi dashboard stats | Doanh thu trùng khớp số thực |  |  | High |
| TC116 | Dashboard | Dashboard Calc | Đọc số lượng khách hiện thời | 2 phòng đang OCCUPIED | 2 rooms occupied | 1. Xem chỉ số active guests | Tính toán tương quan = 4 |  |  | Medium |
| TC117 | Dashboard | Dashboard Boundary | Báo cáo trắng khi DB trống | Cơ sở dữ liệu hoàn toàn chưa có giao dịch | Empty DB | 1. Xem dashboard stats | Doanh thu = 0, Occupancy = 0%, ko sập ứng dụng |  |  | Boundary |
| TC118 | Dashboard | Security | Xem dashboard lậu không qua filter | Bypass Security Filter | No Token headers | 1. Gửi thẳng request tới endpoint dashboard | Trả 401 Unauthorized |  |  | High |
| TC119 | Dashboard | Performance | Độ trễ phản hồi biên khi tính toán | DB chứa 1,000,000 dòng hoá đơn | Heavy DB | 1. Gọi xem dashboard | Thời gian phản hồi không nghẽn |  |  | Boundary |
| TC120 | Dashboard | Dashboard Calc | Phân nhóm doanh số dịch vụ chuẩn | Đã bán trà sữa và spa | Sold items link | 1. Gọi dashboard stats | Báo cáo đúng doanh thu dịch vụ phụ |  |  | High |


---


# FILE: Test_Data_Seed.md

# Test Data Seed - Hotel Management System
Tài liệu dữ liệu kiểm thử Việt Nam thực tế.

## 1. Danh sách 20 Users (Khách hàng)
| User ID | Full Name | Email | Phone Number | CCCD / Passport | Role | Loyalty Points |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| 1 | Nguyễn Thế Khoa | user_1@gmail.com | 098310001 | 035096100001 | CUSTOMER | 200 |
| 2 | Hoàng Thế Dũng | user_2@gmail.com | 098310002 | 035096100002 | CUSTOMER | 500 |
| 3 | Ngô Văn Anh | user_3@gmail.com | 098310003 | 035096100003 | CUSTOMER | 100 |
| 4 | Vũ Xuân Anh | user_4@gmail.com | 098310004 | 035096100004 | CUSTOMER | 500 |
| 5 | Ngô Hữu Sơn | user_5@gmail.com | 098310005 | 035096100005 | CUSTOMER | 500 |
| 6 | Nguyễn Ngọc Trang | user_6@gmail.com | 098310006 | 035096100006 | CUSTOMER | 300 |
| 7 | Hoàng Mai Ngọc | user_7@gmail.com | 098310007 | 035096100007 | CUSTOMER | 100 |
| 8 | Hồ Đức Phong | user_8@gmail.com | 098310008 | 035096100008 | CUSTOMER | 300 |
| 9 | Trần Minh Trúc | user_9@gmail.com | 098310009 | 035096100009 | CUSTOMER | 500 |
| 10 | Hồ Đức Vinh | user_10@gmail.com | 098310010 | 035096100010 | CUSTOMER | 300 |
| 11 | Phan Minh Dung | user_11@gmail.com | 098310011 | 035096100011 | CUSTOMER | 100 |
| 12 | Đặng Đức Huy | user_12@gmail.com | 098310012 | 035096100012 | CUSTOMER | 100 |
| 13 | Võ Kim Oanh | user_13@gmail.com | 098310013 | 035096100013 | CUSTOMER | 200 |
| 14 | Đỗ Mai Mai | user_14@gmail.com | 098310014 | 035096100014 | CUSTOMER | 100 |
| 15 | Vũ Minh Sơn | user_15@gmail.com | 098310015 | 035096100015 | CUSTOMER | 400 |
| 16 | Vũ Khánh Ngọc | user_16@gmail.com | 098310016 | 035096100016 | CUSTOMER | 100 |
| 17 | Trần Quốc Phúc | user_17@gmail.com | 098310017 | 035096100017 | CUSTOMER | 300 |
| 18 | Phan Mạnh Nam | user_18@gmail.com | 098310018 | 035096100018 | CUSTOMER | 200 |
| 19 | Hồ Khánh Trúc | user_19@gmail.com | 098310019 | 035096100019 | CUSTOMER | 200 |
| 20 | Hoàng Mai Yến | user_20@gmail.com | 098310020 | 035096100020 | CUSTOMER | 500 |

## 2. Danh sách 10 Employees (Nhân viên)
| Employee ID | Employee Code | Full Name | Position | Email | Phone Number | Salary Rate (VND) | Role |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| 1 | EMP001 | Ngô Phương Phương | Tổng quản lý | emp_1@hotel.com | 091220001 | 9,500,000 | ADMIN |
| 2 | EMP002 | Vũ Ngọc Vy | Quản lý sảnh | emp_2@hotel.com | 091220002 | 11,000,000 | RECEPTIONIST |
| 3 | EMP003 | Lê Thị Giang | Trưởng quầy lễ tân | emp_3@hotel.com | 091220003 | 12,500,000 | RECEPTIONIST |
| 4 | EMP004 | Huỳnh Tuấn Quân | Lễ tân ca sáng | emp_4@hotel.com | 091220004 | 14,000,000 | RECEPTIONIST |
| 5 | EMP005 | Hồ Gia Sơn | Lễ tân ca tối | emp_5@hotel.com | 091220005 | 15,500,000 | RECEPTIONIST |
| 6 | EMP006 | Nguyễn Khánh Giang | Nhân viên buồng phòng | emp_6@hotel.com | 091220006 | 17,000,000 | RECEPTIONIST |
| 7 | EMP007 | Bùi Thanh Nhi | Nhân viên buồng phòng | emp_7@hotel.com | 091220007 | 18,500,000 | RECEPTIONIST |
| 8 | EMP008 | Huỳnh Kim An | Nhân viên kỹ thuật | emp_8@hotel.com | 091220008 | 20,000,000 | RECEPTIONIST |
| 9 | EMP009 | Huỳnh Bích Giang | Trưởng bộ phận kế toán | emp_9@hotel.com | 091220009 | 21,500,000 | RECEPTIONIST |
| 10 | EMP010 | Phan Ngọc Oanh | Kế toán viên | emp_10@hotel.com | 091220010 | 23,000,000 | RECEPTIONIST |

## 3. Danh sách 10 Hạng Phòng Room Types
| Room Type ID | Name | Description | Base Price (VND) | Max Capacity | Size (m²) |
| :--- | :--- | :--- | :--- | :--- | :--- |
| 1 | Standard Single | Phòng tiêu chuẩn một giường đơn, đầy đủ điện nước truyền hình cáp. | 450,000 | 1 | 20 |
| 2 | Standard Twin | Phòng đôi tiêu chuẩn hai giường đơn, hướng hành lang chung. | 650,000 | 2 | 28 |
| 3 | Standard Double | Phòng tiêu chuẩn một giường đôi lớn, ban công thoáng mát. | 700,000 | 2 | 28 |
| 4 | Superior Double | Phòng Superior một giường đôi lớn, trang bị máy pha cà phê mini. | 900,000 | 2 | 32 |
| 5 | Superior Twin | Phòng Superior hai giường đơn rộng rãi, trang thiết bị gỗ thông. | 950,000 | 2 | 32 |
| 6 | Deluxe King | Phòng Deluxe cao cấp giường King size, bồn tắm nằm đá cẩm thạch. | 1,200,000 | 2 | 45 |
| 7 | Deluxe Twin | Phòng Deluxe hai giường lớn, hướng nhìn thành phố panoramic vista. | 1,250,000 | 2 | 45 |
| 8 | Executive Suite | Phòng Suite hoàng gia, có phòng khách riêng, bar mini và quầy ăn uống. | 2,200,000 | 3 | 65 |
| 9 | Presidential Suite | Phòng Tổng Thống sang trọng bậc nhất, sofa da thật, phòng sauna riêng. | 5,500,000 | 4 | 120 |
| 10 | Family Suite | Phòng Suite gia đình gồm hai phòng thông nhau có cửa kết nối riêng biệt. | 2,500,000 | 4 | 75 |

## 4. Danh sách 50 Phòng (Rooms)
| Room ID | Room Number | Floor | Status | Room Type ID |
| :--- | :--- | :--- | :--- | :--- |
| 1 | 101 | 1 | VACANT_CLEAN | 2 |
| 2 | 102 | 1 | VACANT_CLEAN | 3 |
| 3 | 103 | 1 | VACANT_CLEAN | 4 |
| 4 | 104 | 1 | VACANT_CLEAN | 5 |
| 5 | 105 | 1 | OCCUPIED | 6 |
| 6 | 106 | 1 | VACANT_CLEAN | 7 |
| 7 | 107 | 1 | VACANT_CLEAN | 8 |
| 8 | 108 | 1 | VACANT_CLEAN | 9 |
| 9 | 109 | 1 | VACANT_CLEAN | 10 |
| 10 | 110 | 1 | OCCUPIED | 1 |
| 11 | 201 | 2 | VACANT_CLEAN | 2 |
| 12 | 202 | 2 | VACANT_CLEAN | 3 |
| 13 | 203 | 2 | VACANT_CLEAN | 4 |
| 14 | 204 | 2 | VACANT_CLEAN | 5 |
| 15 | 205 | 2 | OCCUPIED | 6 |
| 16 | 206 | 2 | VACANT_CLEAN | 7 |
| 17 | 207 | 2 | VACANT_CLEAN | 8 |
| 18 | 208 | 2 | VACANT_CLEAN | 9 |
| 19 | 209 | 2 | VACANT_CLEAN | 10 |
| 20 | 210 | 2 | OCCUPIED | 1 |
| 21 | 301 | 3 | VACANT_CLEAN | 2 |
| 22 | 302 | 3 | VACANT_CLEAN | 3 |
| 23 | 303 | 3 | VACANT_CLEAN | 4 |
| 24 | 304 | 3 | VACANT_CLEAN | 5 |
| 25 | 305 | 3 | OCCUPIED | 6 |
| 26 | 306 | 3 | VACANT_CLEAN | 7 |
| 27 | 307 | 3 | VACANT_CLEAN | 8 |
| 28 | 308 | 3 | VACANT_CLEAN | 9 |
| 29 | 309 | 3 | VACANT_CLEAN | 10 |
| 30 | 310 | 3 | OCCUPIED | 1 |
| 31 | 401 | 4 | VACANT_CLEAN | 2 |
| 32 | 402 | 4 | VACANT_CLEAN | 3 |
| 33 | 403 | 4 | VACANT_CLEAN | 4 |
| 34 | 404 | 4 | VACANT_CLEAN | 5 |
| 35 | 405 | 4 | OCCUPIED | 6 |
| 36 | 406 | 4 | VACANT_CLEAN | 7 |
| 37 | 407 | 4 | VACANT_CLEAN | 8 |
| 38 | 408 | 4 | VACANT_CLEAN | 9 |
| 39 | 409 | 4 | VACANT_CLEAN | 10 |
| 40 | 410 | 4 | OCCUPIED | 1 |
| 41 | 501 | 5 | VACANT_CLEAN | 2 |
| 42 | 502 | 5 | VACANT_CLEAN | 3 |
| 43 | 503 | 5 | VACANT_CLEAN | 4 |
| 44 | 504 | 5 | VACANT_CLEAN | 5 |
| 45 | 505 | 5 | OCCUPIED | 6 |
| 46 | 506 | 5 | VACANT_CLEAN | 7 |
| 47 | 507 | 5 | VACANT_CLEAN | 8 |
| 48 | 508 | 5 | VACANT_CLEAN | 9 |
| 49 | 509 | 5 | VACANT_CLEAN | 10 |
| 50 | 510 | 5 | OCCUPIED | 1 |

## 5. Danh sách 100 Giao dịch Đặt phòng (Bookings)
| Booking ID | User ID | Total Amount (VND) | Booking Status |
| :--- | :--- | :--- | :--- |
| 1 | 2 | 1,400,000 | CHECKED_OUT |
| 2 | 3 | 4,500,000 | CHECKED_OUT |
| 3 | 4 | 4,750,000 | CHECKED_OUT |
| 4 | 5 | 1,200,000 | CHECKED_OUT |
| 5 | 6 | 6,250,000 | CHECKED_OUT |
| 6 | 7 | 6,600,000 | CHECKED_OUT |
| 7 | 8 | 22,000,000 | CHECKED_OUT |
| 8 | 9 | 2,500,000 | CHECKED_OUT |
| 9 | 10 | 450,000 | CHECKED_OUT |
| 10 | 11 | 1,950,000 | CHECKED_OUT |
| 11 | 12 | 2,100,000 | CHECKED_OUT |
| 12 | 13 | 1,800,000 | CHECKED_OUT |
| 13 | 14 | 950,000 | CHECKED_OUT |
| 14 | 15 | 2,400,000 | CHECKED_OUT |
| 15 | 16 | 6,250,000 | CHECKED_OUT |
| 16 | 17 | 2,200,000 | CHECKED_OUT |
| 17 | 18 | 5,500,000 | CHECKED_OUT |
| 18 | 19 | 10,000,000 | CHECKED_OUT |
| 19 | 20 | 450,000 | CHECKED_OUT |
| 20 | 1 | 3,250,000 | CHECKED_OUT |
| 21 | 2 | 1,400,000 | CHECKED_OUT |
| 22 | 3 | 1,800,000 | CHECKED_OUT |
| 23 | 4 | 3,800,000 | CHECKED_OUT |
| 24 | 5 | 6,000,000 | CHECKED_OUT |
| 25 | 6 | 2,500,000 | CHECKED_OUT |
| 26 | 7 | 6,600,000 | CHECKED_OUT |
| 27 | 8 | 27,500,000 | CHECKED_OUT |
| 28 | 9 | 12,500,000 | CHECKED_OUT |
| 29 | 10 | 1,800,000 | CHECKED_OUT |
| 30 | 11 | 1,300,000 | CHECKED_OUT |
| 31 | 12 | 3,500,000 | CHECKED_OUT |
| 32 | 13 | 1,800,000 | CHECKED_OUT |
| 33 | 14 | 2,850,000 | CHECKED_OUT |
| 34 | 15 | 4,800,000 | CHECKED_OUT |
| 35 | 16 | 3,750,000 | CHECKED_OUT |
| 36 | 17 | 8,800,000 | CHECKED_OUT |
| 37 | 18 | 27,500,000 | CHECKED_OUT |
| 38 | 19 | 10,000,000 | CHECKED_OUT |
| 39 | 20 | 450,000 | CHECKED_OUT |
| 40 | 1 | 1,300,000 | CHECKED_OUT |
| 41 | 2 | 1,400,000 | CHECKED_OUT |
| 42 | 3 | 900,000 | CHECKED_OUT |
| 43 | 4 | 2,850,000 | CHECKED_OUT |
| 44 | 5 | 1,200,000 | CHECKED_OUT |
| 45 | 6 | 6,250,000 | CHECKED_OUT |
| 46 | 7 | 11,000,000 | CHECKED_OUT |
| 47 | 8 | 11,000,000 | CHECKED_OUT |
| 48 | 9 | 12,500,000 | CHECKED_OUT |
| 49 | 10 | 900,000 | CHECKED_OUT |
| 50 | 11 | 650,000 | CHECKED_OUT |
| 51 | 12 | 700,000 | CHECKED_OUT |
| 52 | 13 | 900,000 | CHECKED_OUT |
| 53 | 14 | 1,900,000 | CHECKED_OUT |
| 54 | 15 | 1,200,000 | CHECKED_OUT |
| 55 | 16 | 1,250,000 | CHECKED_OUT |
| 56 | 17 | 6,600,000 | CHECKED_OUT |
| 57 | 18 | 5,500,000 | CHECKED_OUT |
| 58 | 19 | 12,500,000 | CHECKED_OUT |
| 59 | 20 | 900,000 | CHECKED_OUT |
| 60 | 1 | 1,950,000 | CHECKED_OUT |
| 61 | 2 | 2,800,000 | CHECKED_OUT |
| 62 | 3 | 1,800,000 | CHECKED_OUT |
| 63 | 4 | 4,750,000 | CHECKED_OUT |
| 64 | 5 | 2,400,000 | CHECKED_OUT |
| 65 | 6 | 6,250,000 | CHECKED_OUT |
| 66 | 7 | 11,000,000 | CHECKED_OUT |
| 67 | 8 | 22,000,000 | CHECKED_OUT |
| 68 | 9 | 5,000,000 | CHECKED_OUT |
| 69 | 10 | 1,800,000 | CHECKED_OUT |
| 70 | 11 | 2,600,000 | CHECKED_OUT |
| 71 | 12 | 1,400,000 | CHECKED_OUT |
| 72 | 13 | 900,000 | CHECKED_OUT |
| 73 | 14 | 950,000 | CHECKED_OUT |
| 74 | 15 | 4,800,000 | CHECKED_OUT |
| 75 | 16 | 3,750,000 | CHECKED_OUT |
| 76 | 17 | 8,800,000 | CHECKED_OUT |
| 77 | 18 | 22,000,000 | CHECKED_OUT |
| 78 | 19 | 10,000,000 | CHECKED_OUT |
| 79 | 20 | 450,000 | CHECKED_OUT |
| 80 | 1 | 650,000 | CHECKED_IN |
| 81 | 2 | 700,000 | CHECKED_IN |
| 82 | 3 | 3,600,000 | CHECKED_IN |
| 83 | 4 | 2,850,000 | CHECKED_IN |
| 84 | 5 | 1,200,000 | CHECKED_IN |
| 85 | 6 | 2,500,000 | CHECKED_IN |
| 86 | 7 | 4,400,000 | CHECKED_IN |
| 87 | 8 | 11,000,000 | CHECKED_IN |
| 88 | 9 | 12,500,000 | CHECKED_IN |
| 89 | 10 | 1,800,000 | CHECKED_IN |
| 90 | 11 | 1,300,000 | CHECKED_IN |
| 91 | 12 | 2,800,000 | CHECKED_IN |
| 92 | 13 | 1,800,000 | CHECKED_IN |
| 93 | 14 | 2,850,000 | CHECKED_IN |
| 94 | 15 | 4,800,000 | CHECKED_IN |
| 95 | 16 | 2,500,000 | PENDING |
| 96 | 17 | 2,200,000 | PENDING |
| 97 | 18 | 22,000,000 | PENDING |
| 98 | 19 | 12,500,000 | PENDING |
| 99 | 20 | 450,000 | PENDING |
| 100 | 1 | 650,000 | PENDING |

## 6. Danh sách Chi tiết liên hợp (Booking Details)
| Detail ID | Booking ID | Room ID | Start Date | End Date |
| :--- | :--- | :--- | :--- | :--- |
| 1 | 1 | 2 | 2026-06-02 | 2026-06-04 |
| 2 | 2 | 3 | 2026-06-03 | 2026-06-08 |
| 3 | 3 | 4 | 2026-06-04 | 2026-06-09 |
| 4 | 4 | 5 | 2026-06-05 | 2026-06-06 |
| 5 | 5 | 6 | 2026-06-06 | 2026-06-11 |
| 6 | 6 | 7 | 2026-06-07 | 2026-06-10 |
| 7 | 7 | 8 | 2026-06-08 | 2026-06-12 |
| 8 | 8 | 9 | 2026-06-09 | 2026-06-10 |
| 9 | 9 | 10 | 2026-06-10 | 2026-06-11 |
| 10 | 10 | 11 | 2026-06-11 | 2026-06-14 |
| 11 | 11 | 12 | 2026-06-12 | 2026-06-15 |
| 12 | 12 | 13 | 2026-06-13 | 2026-06-15 |
| 13 | 13 | 14 | 2026-06-14 | 2026-06-15 |
| 14 | 14 | 15 | 2026-06-15 | 2026-06-17 |
| 15 | 15 | 16 | 2026-06-16 | 2026-06-21 |
| 16 | 16 | 17 | 2026-06-17 | 2026-06-18 |
| 17 | 17 | 18 | 2026-06-18 | 2026-06-19 |
| 18 | 18 | 19 | 2026-06-19 | 2026-06-23 |
| 19 | 19 | 20 | 2026-06-20 | 2026-06-21 |
| 20 | 20 | 21 | 2026-06-21 | 2026-06-26 |
| 21 | 21 | 22 | 2026-06-22 | 2026-06-24 |
| 22 | 22 | 23 | 2026-06-23 | 2026-06-25 |
| 23 | 23 | 24 | 2026-06-24 | 2026-06-28 |
| 24 | 24 | 25 | 2026-06-25 | 2026-06-30 |
| 25 | 25 | 26 | 2026-06-26 | 2026-06-28 |
| 26 | 26 | 27 | 2026-06-27 | 2026-06-30 |
| 27 | 27 | 28 | 2026-06-28 | 2026-06-33 |
| 28 | 28 | 29 | 2026-06-01 | 2026-06-06 |
| 29 | 29 | 30 | 2026-06-02 | 2026-06-06 |
| 30 | 30 | 31 | 2026-06-03 | 2026-06-05 |
| 31 | 31 | 32 | 2026-06-04 | 2026-06-09 |
| 32 | 32 | 33 | 2026-06-05 | 2026-06-07 |
| 33 | 33 | 34 | 2026-06-06 | 2026-06-09 |
| 34 | 34 | 35 | 2026-06-07 | 2026-06-11 |
| 35 | 35 | 36 | 2026-06-08 | 2026-06-11 |
| 36 | 36 | 37 | 2026-06-09 | 2026-06-13 |
| 37 | 37 | 38 | 2026-06-10 | 2026-06-15 |
| 38 | 38 | 39 | 2026-06-11 | 2026-06-15 |
| 39 | 39 | 40 | 2026-06-12 | 2026-06-13 |
| 40 | 40 | 41 | 2026-06-13 | 2026-06-15 |
| 41 | 41 | 42 | 2026-06-14 | 2026-06-16 |
| 42 | 42 | 43 | 2026-06-15 | 2026-06-16 |
| 43 | 43 | 44 | 2026-06-16 | 2026-06-19 |
| 44 | 44 | 45 | 2026-06-17 | 2026-06-18 |
| 45 | 45 | 46 | 2026-06-18 | 2026-06-23 |
| 46 | 46 | 47 | 2026-06-19 | 2026-06-24 |
| 47 | 47 | 48 | 2026-06-20 | 2026-06-22 |
| 48 | 48 | 49 | 2026-06-21 | 2026-06-26 |
| 49 | 49 | 50 | 2026-06-22 | 2026-06-24 |
| 50 | 50 | 1 | 2026-06-23 | 2026-06-24 |
| 51 | 51 | 2 | 2026-06-24 | 2026-06-25 |
| 52 | 52 | 3 | 2026-06-25 | 2026-06-26 |
| 53 | 53 | 4 | 2026-06-26 | 2026-06-28 |
| 54 | 54 | 5 | 2026-06-27 | 2026-06-28 |
| 55 | 55 | 6 | 2026-06-28 | 2026-06-29 |
| 56 | 56 | 7 | 2026-06-01 | 2026-06-04 |
| 57 | 57 | 8 | 2026-06-02 | 2026-06-03 |
| 58 | 58 | 9 | 2026-06-03 | 2026-06-08 |
| 59 | 59 | 10 | 2026-06-04 | 2026-06-06 |
| 60 | 60 | 11 | 2026-06-05 | 2026-06-08 |
| 61 | 61 | 12 | 2026-06-06 | 2026-06-10 |
| 62 | 62 | 13 | 2026-06-07 | 2026-06-09 |
| 63 | 63 | 14 | 2026-06-08 | 2026-06-13 |
| 64 | 64 | 15 | 2026-06-09 | 2026-06-11 |
| 65 | 65 | 16 | 2026-06-10 | 2026-06-15 |
| 66 | 66 | 17 | 2026-06-11 | 2026-06-16 |
| 67 | 67 | 18 | 2026-06-12 | 2026-06-16 |
| 68 | 68 | 19 | 2026-06-13 | 2026-06-15 |
| 69 | 69 | 20 | 2026-06-14 | 2026-06-18 |
| 70 | 70 | 21 | 2026-06-15 | 2026-06-19 |
| 71 | 71 | 22 | 2026-06-16 | 2026-06-18 |
| 72 | 72 | 23 | 2026-06-17 | 2026-06-18 |
| 73 | 73 | 24 | 2026-06-18 | 2026-06-19 |
| 74 | 74 | 25 | 2026-06-19 | 2026-06-23 |
| 75 | 75 | 26 | 2026-06-20 | 2026-06-23 |
| 76 | 76 | 27 | 2026-06-21 | 2026-06-25 |
| 77 | 77 | 28 | 2026-06-22 | 2026-06-26 |
| 78 | 78 | 29 | 2026-06-23 | 2026-06-27 |
| 79 | 79 | 30 | 2026-06-24 | 2026-06-25 |
| 80 | 80 | 31 | 2026-06-25 | 2026-06-26 |
| 81 | 81 | 32 | 2026-06-26 | 2026-06-27 |
| 82 | 82 | 33 | 2026-06-27 | 2026-06-31 |
| 83 | 83 | 34 | 2026-06-28 | 2026-06-31 |
| 84 | 84 | 35 | 2026-06-01 | 2026-06-02 |
| 85 | 85 | 36 | 2026-06-02 | 2026-06-04 |
| 86 | 86 | 37 | 2026-06-03 | 2026-06-05 |
| 87 | 87 | 38 | 2026-06-04 | 2026-06-06 |
| 88 | 88 | 39 | 2026-06-05 | 2026-06-10 |
| 89 | 89 | 40 | 2026-06-06 | 2026-06-10 |
| 90 | 90 | 41 | 2026-06-07 | 2026-06-09 |
| 91 | 91 | 42 | 2026-06-08 | 2026-06-12 |
| 92 | 92 | 43 | 2026-06-09 | 2026-06-11 |
| 93 | 93 | 44 | 2026-06-10 | 2026-06-13 |
| 94 | 94 | 45 | 2026-06-11 | 2026-06-15 |
| 95 | 95 | 46 | 2026-06-12 | 2026-06-14 |
| 96 | 96 | 47 | 2026-06-13 | 2026-06-14 |
| 97 | 97 | 48 | 2026-06-14 | 2026-06-18 |
| 98 | 98 | 49 | 2026-06-15 | 2026-06-20 |
| 99 | 99 | 50 | 2026-06-16 | 2026-06-17 |
| 100 | 100 | 1 | 2026-06-17 | 2026-06-18 |

## 7. Danh sách 100 Hóa Đơn (Invoices)
| Invoice ID | Booking ID | Employee ID | Tax Amount (VND) | Discount Amount (VND) | Final Amount (VND) | Issue Date | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| 1 | 1 | 2 | 140,000 | 0 | 1,540,000 | 2026-06-02 | PAID |
| 2 | 2 | 3 | 450,000 | 0 | 4,950,000 | 2026-06-03 | PAID |
| 3 | 3 | 4 | 475,000 | 0 | 5,225,000 | 2026-06-04 | PAID |
| 4 | 4 | 5 | 120,000 | 0 | 1,320,000 | 2026-06-05 | PAID |
| 5 | 5 | 6 | 625,000 | 0 | 6,875,000 | 2026-06-06 | PAID |
| 6 | 6 | 7 | 660,000 | 0 | 7,260,000 | 2026-06-07 | PAID |
| 7 | 7 | 8 | 2,200,000 | 0 | 24,200,000 | 2026-06-08 | PAID |
| 8 | 8 | 9 | 250,000 | 0 | 2,750,000 | 2026-06-09 | PAID |
| 9 | 9 | 10 | 45,000 | 0 | 495,000 | 2026-06-10 | PAID |
| 10 | 10 | 1 | 195,000 | 50,000 | 2,095,000 | 2026-06-11 | PAID |
| 11 | 11 | 2 | 210,000 | 0 | 2,310,000 | 2026-06-12 | PAID |
| 12 | 12 | 3 | 180,000 | 0 | 1,980,000 | 2026-06-13 | PAID |
| 13 | 13 | 4 | 95,000 | 0 | 1,045,000 | 2026-06-14 | PAID |
| 14 | 14 | 5 | 240,000 | 0 | 2,640,000 | 2026-06-15 | PAID |
| 15 | 15 | 6 | 625,000 | 0 | 6,875,000 | 2026-06-16 | PAID |
| 16 | 16 | 7 | 220,000 | 0 | 2,420,000 | 2026-06-17 | PAID |
| 17 | 17 | 8 | 550,000 | 0 | 6,050,000 | 2026-06-18 | PAID |
| 18 | 18 | 9 | 1,000,000 | 0 | 11,000,000 | 2026-06-19 | PAID |
| 19 | 19 | 10 | 45,000 | 0 | 495,000 | 2026-06-20 | PAID |
| 20 | 20 | 1 | 325,000 | 50,000 | 3,525,000 | 2026-06-21 | PAID |
| 21 | 21 | 2 | 140,000 | 0 | 1,540,000 | 2026-06-22 | PAID |
| 22 | 22 | 3 | 180,000 | 0 | 1,980,000 | 2026-06-23 | PAID |
| 23 | 23 | 4 | 380,000 | 0 | 4,180,000 | 2026-06-24 | PAID |
| 24 | 24 | 5 | 600,000 | 0 | 6,600,000 | 2026-06-25 | PAID |
| 25 | 25 | 6 | 250,000 | 0 | 2,750,000 | 2026-06-26 | PAID |
| 26 | 26 | 7 | 660,000 | 0 | 7,260,000 | 2026-06-27 | PAID |
| 27 | 27 | 8 | 2,750,000 | 0 | 30,250,000 | 2026-06-28 | PAID |
| 28 | 28 | 9 | 1,250,000 | 0 | 13,750,000 | 2026-06-01 | PAID |
| 29 | 29 | 10 | 180,000 | 0 | 1,980,000 | 2026-06-02 | PAID |
| 30 | 30 | 1 | 130,000 | 50,000 | 1,380,000 | 2026-06-03 | PAID |
| 31 | 31 | 2 | 350,000 | 0 | 3,850,000 | 2026-06-04 | PAID |
| 32 | 32 | 3 | 180,000 | 0 | 1,980,000 | 2026-06-05 | PAID |
| 33 | 33 | 4 | 285,000 | 0 | 3,135,000 | 2026-06-06 | PAID |
| 34 | 34 | 5 | 480,000 | 0 | 5,280,000 | 2026-06-07 | PAID |
| 35 | 35 | 6 | 375,000 | 0 | 4,125,000 | 2026-06-08 | PAID |
| 36 | 36 | 7 | 880,000 | 0 | 9,680,000 | 2026-06-09 | PAID |
| 37 | 37 | 8 | 2,750,000 | 0 | 30,250,000 | 2026-06-10 | PAID |
| 38 | 38 | 9 | 1,000,000 | 0 | 11,000,000 | 2026-06-11 | PAID |
| 39 | 39 | 10 | 45,000 | 0 | 495,000 | 2026-06-12 | PAID |
| 40 | 40 | 1 | 130,000 | 50,000 | 1,380,000 | 2026-06-13 | PAID |
| 41 | 41 | 2 | 140,000 | 0 | 1,540,000 | 2026-06-14 | PAID |
| 42 | 42 | 3 | 90,000 | 0 | 990,000 | 2026-06-15 | PAID |
| 43 | 43 | 4 | 285,000 | 0 | 3,135,000 | 2026-06-16 | PAID |
| 44 | 44 | 5 | 120,000 | 0 | 1,320,000 | 2026-06-17 | PAID |
| 45 | 45 | 6 | 625,000 | 0 | 6,875,000 | 2026-06-18 | PAID |
| 46 | 46 | 7 | 1,100,000 | 0 | 12,100,000 | 2026-06-19 | PAID |
| 47 | 47 | 8 | 1,100,000 | 0 | 12,100,000 | 2026-06-20 | PAID |
| 48 | 48 | 9 | 1,250,000 | 0 | 13,750,000 | 2026-06-21 | PAID |
| 49 | 49 | 10 | 90,000 | 0 | 990,000 | 2026-06-22 | PAID |
| 50 | 50 | 1 | 65,000 | 50,000 | 665,000 | 2026-06-23 | PAID |
| 51 | 51 | 2 | 70,000 | 0 | 770,000 | 2026-06-24 | PAID |
| 52 | 52 | 3 | 90,000 | 0 | 990,000 | 2026-06-25 | PAID |
| 53 | 53 | 4 | 190,000 | 0 | 2,090,000 | 2026-06-26 | PAID |
| 54 | 54 | 5 | 120,000 | 0 | 1,320,000 | 2026-06-27 | PAID |
| 55 | 55 | 6 | 125,000 | 0 | 1,375,000 | 2026-06-28 | PAID |
| 56 | 56 | 7 | 660,000 | 0 | 7,260,000 | 2026-06-01 | PAID |
| 57 | 57 | 8 | 550,000 | 0 | 6,050,000 | 2026-06-02 | PAID |
| 58 | 58 | 9 | 1,250,000 | 0 | 13,750,000 | 2026-06-03 | PAID |
| 59 | 59 | 10 | 90,000 | 0 | 990,000 | 2026-06-04 | PAID |
| 60 | 60 | 1 | 195,000 | 50,000 | 2,095,000 | 2026-06-05 | PAID |
| 61 | 61 | 2 | 280,000 | 0 | 3,080,000 | 2026-06-06 | PAID |
| 62 | 62 | 3 | 180,000 | 0 | 1,980,000 | 2026-06-07 | PAID |
| 63 | 63 | 4 | 475,000 | 0 | 5,225,000 | 2026-06-08 | PAID |
| 64 | 64 | 5 | 240,000 | 0 | 2,640,000 | 2026-06-09 | PAID |
| 65 | 65 | 6 | 625,000 | 0 | 6,875,000 | 2026-06-10 | PAID |
| 66 | 66 | 7 | 1,100,000 | 0 | 12,100,000 | 2026-06-11 | PAID |
| 67 | 67 | 8 | 2,200,000 | 0 | 24,200,000 | 2026-06-12 | PAID |
| 68 | 68 | 9 | 500,000 | 0 | 5,500,000 | 2026-06-13 | PAID |
| 69 | 69 | 10 | 180,000 | 0 | 1,980,000 | 2026-06-14 | PAID |
| 70 | 70 | 1 | 260,000 | 50,000 | 2,810,000 | 2026-06-15 | PAID |
| 71 | 71 | 2 | 140,000 | 0 | 1,540,000 | 2026-06-16 | PAID |
| 72 | 72 | 3 | 90,000 | 0 | 990,000 | 2026-06-17 | PAID |
| 73 | 73 | 4 | 95,000 | 0 | 1,045,000 | 2026-06-18 | PAID |
| 74 | 74 | 5 | 480,000 | 0 | 5,280,000 | 2026-06-19 | PAID |
| 75 | 75 | 6 | 375,000 | 0 | 4,125,000 | 2026-06-20 | PAID |
| 76 | 76 | 7 | 880,000 | 0 | 9,680,000 | 2026-06-21 | PAID |
| 77 | 77 | 8 | 2,200,000 | 0 | 24,200,000 | 2026-06-22 | PAID |
| 78 | 78 | 9 | 1,000,000 | 0 | 11,000,000 | 2026-06-23 | PAID |
| 79 | 79 | 10 | 45,000 | 0 | 495,000 | 2026-06-24 | PAID |
| 80 | 80 | 1 | 65,000 | 50,000 | 665,000 | 2026-06-25 | PAID |
| 81 | 81 | 2 | 70,000 | 0 | 770,000 | 2026-06-26 | PAID |
| 82 | 82 | 3 | 360,000 | 0 | 3,960,000 | 2026-06-27 | PAID |
| 83 | 83 | 4 | 285,000 | 0 | 3,135,000 | 2026-06-28 | PAID |
| 84 | 84 | 5 | 120,000 | 0 | 1,320,000 | 2026-06-01 | PAID |
| 85 | 85 | 6 | 250,000 | 0 | 2,750,000 | 2026-06-02 | PAID |
| 86 | 86 | 7 | 440,000 | 0 | 4,840,000 | 2026-06-03 | PAID |
| 87 | 87 | 8 | 1,100,000 | 0 | 12,100,000 | 2026-06-04 | PAID |
| 88 | 88 | 9 | 1,250,000 | 0 | 13,750,000 | 2026-06-05 | PAID |
| 89 | 89 | 10 | 180,000 | 0 | 1,980,000 | 2026-06-06 | PAID |
| 90 | 90 | 1 | 130,000 | 50,000 | 1,380,000 | 2026-06-07 | PAID |
| 91 | 91 | 2 | 280,000 | 0 | 3,080,000 | 2026-06-08 | PAID |
| 92 | 92 | 3 | 180,000 | 0 | 1,980,000 | 2026-06-09 | PAID |
| 93 | 93 | 4 | 285,000 | 0 | 3,135,000 | 2026-06-10 | PAID |
| 94 | 94 | 5 | 480,000 | 0 | 5,280,000 | 2026-06-11 | PAID |
| 95 | 95 | 6 | 250,000 | 0 | 2,750,000 | 2026-06-12 | UNPAID |
| 96 | 96 | 7 | 220,000 | 0 | 2,420,000 | 2026-06-13 | UNPAID |
| 97 | 97 | 8 | 2,200,000 | 0 | 24,200,000 | 2026-06-14 | UNPAID |
| 98 | 98 | 9 | 1,250,000 | 0 | 13,750,000 | 2026-06-15 | UNPAID |
| 99 | 99 | 10 | 45,000 | 0 | 495,000 | 2026-06-16 | UNPAID |
| 100 | 100 | 1 | 65,000 | 50,000 | 665,000 | 2026-06-17 | UNPAID |

## 8. Danh sách 100 Giao dịch Ghi nhận thu tiền (Payments)
| Payment ID | Invoice ID | Payment Method | Paid Amount (VND) | Payment Date | Transaction Ref |
| :--- | :--- | :--- | :--- | :--- | :--- |
| 1 | 1 | BANK_TRANSFER | 1,540,000 | 2026-06-02 | TXN260610000 |
| 2 | 2 | CREDIT_CARD | 4,950,000 | 2026-06-03 | TXN260610001 |
| 3 | 3 | CASH | 5,225,000 | 2026-06-04 | CASH_RECP |
| 4 | 4 | BANK_TRANSFER | 1,320,000 | 2026-06-05 | TXN260610003 |
| 5 | 5 | CREDIT_CARD | 6,875,000 | 2026-06-06 | TXN260610004 |
| 6 | 6 | CASH | 7,260,000 | 2026-06-07 | CASH_RECP |
| 7 | 7 | BANK_TRANSFER | 24,200,000 | 2026-06-08 | TXN260610006 |
| 8 | 8 | CREDIT_CARD | 2,750,000 | 2026-06-09 | TXN260610007 |
| 9 | 9 | CASH | 495,000 | 2026-06-10 | CASH_RECP |
| 10 | 10 | BANK_TRANSFER | 2,095,000 | 2026-06-11 | TXN260610009 |
| 11 | 11 | CREDIT_CARD | 2,310,000 | 2026-06-12 | TXN260610010 |
| 12 | 12 | CASH | 1,980,000 | 2026-06-13 | CASH_RECP |
| 13 | 13 | BANK_TRANSFER | 1,045,000 | 2026-06-14 | TXN260610012 |
| 14 | 14 | CREDIT_CARD | 2,640,000 | 2026-06-15 | TXN260610013 |
| 15 | 15 | CASH | 6,875,000 | 2026-06-16 | CASH_RECP |
| 16 | 16 | BANK_TRANSFER | 2,420,000 | 2026-06-17 | TXN260610015 |
| 17 | 17 | CREDIT_CARD | 6,050,000 | 2026-06-18 | TXN260610016 |
| 18 | 18 | CASH | 11,000,000 | 2026-06-19 | CASH_RECP |
| 19 | 19 | BANK_TRANSFER | 495,000 | 2026-06-20 | TXN260610018 |
| 20 | 20 | CREDIT_CARD | 3,525,000 | 2026-06-21 | TXN260610019 |
| 21 | 21 | CASH | 1,540,000 | 2026-06-22 | CASH_RECP |
| 22 | 22 | BANK_TRANSFER | 1,980,000 | 2026-06-23 | TXN260610021 |
| 23 | 23 | CREDIT_CARD | 4,180,000 | 2026-06-24 | TXN260610022 |
| 24 | 24 | CASH | 6,600,000 | 2026-06-25 | CASH_RECP |
| 25 | 25 | BANK_TRANSFER | 2,750,000 | 2026-06-26 | TXN260610024 |
| 26 | 26 | CREDIT_CARD | 7,260,000 | 2026-06-27 | TXN260610025 |
| 27 | 27 | CASH | 30,250,000 | 2026-06-28 | CASH_RECP |
| 28 | 28 | BANK_TRANSFER | 13,750,000 | 2026-06-01 | TXN260610027 |
| 29 | 29 | CREDIT_CARD | 1,980,000 | 2026-06-02 | TXN260610028 |
| 30 | 30 | CASH | 1,380,000 | 2026-06-03 | CASH_RECP |
| 31 | 31 | BANK_TRANSFER | 3,850,000 | 2026-06-04 | TXN260610030 |
| 32 | 32 | CREDIT_CARD | 1,980,000 | 2026-06-05 | TXN260610031 |
| 33 | 33 | CASH | 3,135,000 | 2026-06-06 | CASH_RECP |
| 34 | 34 | BANK_TRANSFER | 5,280,000 | 2026-06-07 | TXN260610033 |
| 35 | 35 | CREDIT_CARD | 4,125,000 | 2026-06-08 | TXN260610034 |
| 36 | 36 | CASH | 9,680,000 | 2026-06-09 | CASH_RECP |
| 37 | 37 | BANK_TRANSFER | 30,250,000 | 2026-06-10 | TXN260610036 |
| 38 | 38 | CREDIT_CARD | 11,000,000 | 2026-06-11 | TXN260610037 |
| 39 | 39 | CASH | 495,000 | 2026-06-12 | CASH_RECP |
| 40 | 40 | BANK_TRANSFER | 1,380,000 | 2026-06-13 | TXN260610039 |
| 41 | 41 | CREDIT_CARD | 1,540,000 | 2026-06-14 | TXN260610040 |
| 42 | 42 | CASH | 990,000 | 2026-06-15 | CASH_RECP |
| 43 | 43 | BANK_TRANSFER | 3,135,000 | 2026-06-16 | TXN260610042 |
| 44 | 44 | CREDIT_CARD | 1,320,000 | 2026-06-17 | TXN260610043 |
| 45 | 45 | CASH | 6,875,000 | 2026-06-18 | CASH_RECP |
| 46 | 46 | BANK_TRANSFER | 12,100,000 | 2026-06-19 | TXN260610045 |
| 47 | 47 | CREDIT_CARD | 12,100,000 | 2026-06-20 | TXN260610046 |
| 48 | 48 | CASH | 13,750,000 | 2026-06-21 | CASH_RECP |
| 49 | 49 | BANK_TRANSFER | 990,000 | 2026-06-22 | TXN260610048 |
| 50 | 50 | CREDIT_CARD | 665,000 | 2026-06-23 | TXN260610049 |
| 51 | 51 | CASH | 770,000 | 2026-06-24 | CASH_RECP |
| 52 | 52 | BANK_TRANSFER | 990,000 | 2026-06-25 | TXN260610051 |
| 53 | 53 | CREDIT_CARD | 2,090,000 | 2026-06-26 | TXN260610052 |
| 54 | 54 | CASH | 1,320,000 | 2026-06-27 | CASH_RECP |
| 55 | 55 | BANK_TRANSFER | 1,375,000 | 2026-06-28 | TXN260610054 |
| 56 | 56 | CREDIT_CARD | 7,260,000 | 2026-06-01 | TXN260610055 |
| 57 | 57 | CASH | 6,050,000 | 2026-06-02 | CASH_RECP |
| 58 | 58 | BANK_TRANSFER | 13,750,000 | 2026-06-03 | TXN260610057 |
| 59 | 59 | CREDIT_CARD | 990,000 | 2026-06-04 | TXN260610058 |
| 60 | 60 | CASH | 2,095,000 | 2026-06-05 | CASH_RECP |
| 61 | 61 | BANK_TRANSFER | 3,080,000 | 2026-06-06 | TXN260610060 |
| 62 | 62 | CREDIT_CARD | 1,980,000 | 2026-06-07 | TXN260610061 |
| 63 | 63 | CASH | 5,225,000 | 2026-06-08 | CASH_RECP |
| 64 | 64 | BANK_TRANSFER | 2,640,000 | 2026-06-09 | TXN260610063 |
| 65 | 65 | CREDIT_CARD | 6,875,000 | 2026-06-10 | TXN260610064 |
| 66 | 66 | CASH | 12,100,000 | 2026-06-11 | CASH_RECP |
| 67 | 67 | BANK_TRANSFER | 24,200,000 | 2026-06-12 | TXN260610066 |
| 68 | 68 | CREDIT_CARD | 5,500,000 | 2026-06-13 | TXN260610067 |
| 69 | 69 | CASH | 1,980,000 | 2026-06-14 | CASH_RECP |
| 70 | 70 | BANK_TRANSFER | 2,810,000 | 2026-06-15 | TXN260610069 |
| 71 | 71 | CREDIT_CARD | 1,540,000 | 2026-06-16 | TXN260610070 |
| 72 | 72 | CASH | 990,000 | 2026-06-17 | CASH_RECP |
| 73 | 73 | BANK_TRANSFER | 1,045,000 | 2026-06-18 | TXN260610072 |
| 74 | 74 | CREDIT_CARD | 5,280,000 | 2026-06-19 | TXN260610073 |
| 75 | 75 | CASH | 4,125,000 | 2026-06-20 | CASH_RECP |
| 76 | 76 | BANK_TRANSFER | 9,680,000 | 2026-06-21 | TXN260610075 |
| 77 | 77 | CREDIT_CARD | 24,200,000 | 2026-06-22 | TXN260610076 |
| 78 | 78 | CASH | 11,000,000 | 2026-06-23 | CASH_RECP |
| 79 | 79 | BANK_TRANSFER | 495,000 | 2026-06-24 | TXN260610078 |
| 80 | 80 | CREDIT_CARD | 665,000 | 2026-06-25 | TXN260610079 |
| 81 | 81 | CASH | 770,000 | 2026-06-26 | CASH_RECP |
| 82 | 82 | BANK_TRANSFER | 3,960,000 | 2026-06-27 | TXN260610081 |
| 83 | 83 | CREDIT_CARD | 3,135,000 | 2026-06-28 | TXN260610082 |
| 84 | 84 | CASH | 1,320,000 | 2026-06-01 | CASH_RECP |
| 85 | 85 | BANK_TRANSFER | 2,750,000 | 2026-06-02 | TXN260610084 |
| 86 | 86 | CREDIT_CARD | 4,840,000 | 2026-06-03 | TXN260610085 |
| 87 | 87 | CASH | 12,100,000 | 2026-06-04 | CASH_RECP |
| 88 | 88 | BANK_TRANSFER | 13,750,000 | 2026-06-05 | TXN260610087 |
| 89 | 89 | CREDIT_CARD | 1,980,000 | 2026-06-06 | TXN260610088 |
| 90 | 90 | CASH | 1,380,000 | 2026-06-07 | CASH_RECP |
| 91 | 91 | BANK_TRANSFER | 3,080,000 | 2026-06-08 | TXN260610090 |
| 92 | 92 | CREDIT_CARD | 1,980,000 | 2026-06-09 | TXN260610091 |
| 93 | 93 | CASH | 3,135,000 | 2026-06-10 | CASH_RECP |
| 94 | 94 | BANK_TRANSFER | 5,280,000 | 2026-06-11 | TXN260610093 |
| 95 | 95 | CREDIT_CARD | 2,750,000 | 2026-06-12 | TXN260610094 |
| 96 | 96 | CASH | 2,420,000 | 2026-06-13 | CASH_RECP |
| 97 | 97 | BANK_TRANSFER | 24,200,000 | 2026-06-14 | TXN260610096 |
| 98 | 98 | CREDIT_CARD | 13,750,000 | 2026-06-15 | TXN260610097 |
| 99 | 99 | CASH | 495,000 | 2026-06-16 | CASH_RECP |
| 100 | 100 | BANK_TRANSFER | 665,000 | 2026-06-17 | TXN260610099 |

## 9. Danh sách 50 Đánh giá & Phản hồi (Reviews)
| Review ID | Booking ID | User ID | Rating (Stars) | Comment |
| :--- | :--- | :--- | :--- | :--- |
| 1 | 1 | 2 | 4 | Giường ngủ ở êm ái, ban công ngắm phố cổ siêu đẹp ban đêm. Sẽ quay lại. |
| 2 | 2 | 3 | 4 | Phòng ốc hơi cũ một chút nhưng phục vụ rất chu đáo tận tâm. |
| 3 | 3 | 4 | 4 | Đồ trong tủ lạnh mini bar phong phú, giá hợp lý, dọn phòng nhanh gọn nhẹ. |
| 4 | 4 | 5 | 3 | Khách sạn sát trung tâm, đi bộ sang hồ hoàn kiếm mất đúng 5 phút. Hài lòng. |
| 5 | 5 | 6 | 4 | Bồn tắm ở buồng deluxe xịn xò, xà bông thơm mùi sả chanh dễ chịu mát mẻ. |
| 6 | 6 | 7 | 5 | Ăn sáng buffet phở bò ngon tuyệt, nhân viên chăm chút từng bát phở nóng hổi. |
| 7 | 7 | 8 | 5 | Điều hoà mát lạnh sâu, cách âm tốt không nghe tiếng ồn từ công trình bên ngoài. |
| 8 | 8 | 9 | 3 | Được nhận phòng sớm phục vụ tận tình, cảm ơn khách sạn rất nhiều. |
| 9 | 9 | 10 | 5 | Chuyến công tác lý tưởng nhờ sự chuẩn bị chu toàn của lễ tân ca sáng. |
| 10 | 10 | 11 | 4 | Dịch vụ tuyệt vời, phòng sạch sẽ, lễ tân nhiệt tình giúp đỡ chỉ đường ăn uống. |
| 11 | 11 | 12 | 5 | Giường ngủ ở êm ái, ban công ngắm phố cổ siêu đẹp ban đêm. Sẽ quay lại. |
| 12 | 12 | 13 | 3 | Phòng ốc hơi cũ một chút nhưng phục vụ rất chu đáo tận tâm. |
| 13 | 13 | 14 | 4 | Đồ trong tủ lạnh mini bar phong phú, giá hợp lý, dọn phòng nhanh gọn nhẹ. |
| 14 | 14 | 15 | 4 | Khách sạn sát trung tâm, đi bộ sang hồ hoàn kiếm mất đúng 5 phút. Hài lòng. |
| 15 | 15 | 16 | 5 | Bồn tắm ở buồng deluxe xịn xò, xà bông thơm mùi sả chanh dễ chịu mát mẻ. |
| 16 | 16 | 17 | 3 | Ăn sáng buffet phở bò ngon tuyệt, nhân viên chăm chút từng bát phở nóng hổi. |
| 17 | 17 | 18 | 4 | Điều hoà mát lạnh sâu, cách âm tốt không nghe tiếng ồn từ công trình bên ngoài. |
| 18 | 18 | 19 | 5 | Được nhận phòng sớm phục vụ tận tình, cảm ơn khách sạn rất nhiều. |
| 19 | 19 | 20 | 5 | Chuyến công tác lý tưởng nhờ sự chuẩn bị chu toàn của lễ tân ca sáng. |
| 20 | 20 | 1 | 3 | Dịch vụ tuyệt vời, phòng sạch sẽ, lễ tân nhiệt tình giúp đỡ chỉ đường ăn uống. |
| 21 | 21 | 2 | 5 | Giường ngủ ở êm ái, ban công ngắm phố cổ siêu đẹp ban đêm. Sẽ quay lại. |
| 22 | 22 | 3 | 5 | Phòng ốc hơi cũ một chút nhưng phục vụ rất chu đáo tận tâm. |
| 23 | 23 | 4 | 5 | Đồ trong tủ lạnh mini bar phong phú, giá hợp lý, dọn phòng nhanh gọn nhẹ. |
| 24 | 24 | 5 | 3 | Khách sạn sát trung tâm, đi bộ sang hồ hoàn kiếm mất đúng 5 phút. Hài lòng. |
| 25 | 25 | 6 | 5 | Bồn tắm ở buồng deluxe xịn xò, xà bông thơm mùi sả chanh dễ chịu mát mẻ. |
| 26 | 26 | 7 | 4 | Ăn sáng buffet phở bò ngon tuyệt, nhân viên chăm chút từng bát phở nóng hổi. |
| 27 | 27 | 8 | 4 | Điều hoà mát lạnh sâu, cách âm tốt không nghe tiếng ồn từ công trình bên ngoài. |
| 28 | 28 | 9 | 3 | Được nhận phòng sớm phục vụ tận tình, cảm ơn khách sạn rất nhiều. |
| 29 | 29 | 10 | 5 | Chuyến công tác lý tưởng nhờ sự chuẩn bị chu toàn của lễ tân ca sáng. |
| 30 | 30 | 11 | 4 | Dịch vụ tuyệt vời, phòng sạch sẽ, lễ tân nhiệt tình giúp đỡ chỉ đường ăn uống. |
| 31 | 31 | 12 | 4 | Giường ngủ ở êm ái, ban công ngắm phố cổ siêu đẹp ban đêm. Sẽ quay lại. |
| 32 | 32 | 13 | 3 | Phòng ốc hơi cũ một chút nhưng phục vụ rất chu đáo tận tâm. |
| 33 | 33 | 14 | 4 | Đồ trong tủ lạnh mini bar phong phú, giá hợp lý, dọn phòng nhanh gọn nhẹ. |
| 34 | 34 | 15 | 5 | Khách sạn sát trung tâm, đi bộ sang hồ hoàn kiếm mất đúng 5 phút. Hài lòng. |
| 35 | 35 | 16 | 4 | Bồn tắm ở buồng deluxe xịn xò, xà bông thơm mùi sả chanh dễ chịu mát mẻ. |
| 36 | 36 | 17 | 3 | Ăn sáng buffet phở bò ngon tuyệt, nhân viên chăm chút từng bát phở nóng hổi. |
| 37 | 37 | 18 | 4 | Điều hoà mát lạnh sâu, cách âm tốt không nghe tiếng ồn từ công trình bên ngoài. |
| 38 | 38 | 19 | 5 | Được nhận phòng sớm phục vụ tận tình, cảm ơn khách sạn rất nhiều. |
| 39 | 39 | 20 | 4 | Chuyến công tác lý tưởng nhờ sự chuẩn bị chu toàn của lễ tân ca sáng. |
| 40 | 40 | 1 | 3 | Dịch vụ tuyệt vời, phòng sạch sẽ, lễ tân nhiệt tình giúp đỡ chỉ đường ăn uống. |
| 41 | 41 | 2 | 4 | Giường ngủ ở êm ái, ban công ngắm phố cổ siêu đẹp ban đêm. Sẽ quay lại. |
| 42 | 42 | 3 | 4 | Phòng ốc hơi cũ một chút nhưng phục vụ rất chu đáo tận tâm. |
| 43 | 43 | 4 | 4 | Đồ trong tủ lạnh mini bar phong phú, giá hợp lý, dọn phòng nhanh gọn nhẹ. |
| 44 | 44 | 5 | 3 | Khách sạn sát trung tâm, đi bộ sang hồ hoàn kiếm mất đúng 5 phút. Hài lòng. |
| 45 | 45 | 6 | 4 | Bồn tắm ở buồng deluxe xịn xò, xà bông thơm mùi sả chanh dễ chịu mát mẻ. |
| 46 | 46 | 7 | 4 | Ăn sáng buffet phở bò ngon tuyệt, nhân viên chăm chút từng bát phở nóng hổi. |
| 47 | 47 | 8 | 4 | Điều hoà mát lạnh sâu, cách âm tốt không nghe tiếng ồn từ công trình bên ngoài. |
| 48 | 48 | 9 | 3 | Được nhận phòng sớm phục vụ tận tình, cảm ơn khách sạn rất nhiều. |
| 49 | 49 | 10 | 5 | Chuyến công tác lý tưởng nhờ sự chuẩn bị chu toàn của lễ tân ca sáng. |
| 50 | 50 | 11 | 4 | Dịch vụ tuyệt vời, phòng sạch sẽ, lễ tân nhiệt tình giúp đỡ chỉ đường ăn uống. |

## 10. Danh sách 20 Dịch vụ phụ phụ thu (Services)
| Service ID | Name | Description | Price (VND) | Unit |
| :--- | :--- | :--- | :--- | :--- |
| 1 | Bữa sáng Buffet | Buffet phở, xôi, bánh mì và trái cây tráng miệng. | 150,000 | Khách/Ngày |
| 2 | Giặt là Siêu tốc | Giặt sấy quần áo khô ráo trong vòng 4 tiếng. | 75,000 | Kg |
| 3 | Thuê xe máy | Xe ga Honda Vision đầy xăng tự túc lưu hành phố cổ. | 150,000 | Ngày |
| 4 | Nước ngọt Coca-cola | Lon coca mát lạnh trong minibar phòng nghỉ. | 25,000 | Lon |
| 5 | Nước ép cam tươi | Nước ép cam vắt nguyên chất bổ sung Vitamin C. | 45,000 | Ly |
| 6 | Bia Heineken | Bia Heineken lon phục vụ tại phòng. | 35,000 | Lon |
| 7 | Spa Massage Body | Liệu pháp xoa bóp tinh dầu thảo dược 60 phút thư giãn. | 450,000 | Lượt |
| 8 | Đưa đón sân bay Nội Bài | Xe sedan 4 chỗ đưa đón sân bay tận tâm hai chiều. | 350,000 | Lượt |
| 9 | Mì tôm Hảo Hảo | Mì cốc cứu đói giữa đêm tự hủy trong khay đựng đồ khô. | 20,000 | Hộp |
| 10 | Trà sữa Trân châu | Trà sữa Royaltea trâu châu đường đen mát ngọt sảng khoái. | 55,000 | Ly |
| 11 | Khoai tây chiên Pringles | Hộp khoai tây chiên giòn bùi ăn kèm xem phim. | 65,000 | Hộp |
| 12 | Cocktail Mojito | Đồ uống cồn cocktail chanh bạc hà sảng khoái. | 120,000 | Ly |
| 13 | Vang đỏ Đà Lạt | Chai rượu vang đỏ đà lạt 750ml thích hợp buổi tối ấm cúng. | 320,000 | Chai |
| 14 | Nước suối nguồn Aquafina | Chai nước tinh khiết phục vụ bù sau khi dùng hết nước free. | 15,000 | Chai |
| 15 | Giặt khô vest sang trọng | Ủi đồ giặt khô bộ comple hoặc váy dạ hội đắt tiền. | 200,000 | Bộ |
| 16 | Gym & Fitness trainer | Huấn luyện viên Gym kèm cặp riêng 1 giờ tại phòng tập. | 300,000 | Giờ |
| 17 | Đặt trước vé xem múa rối | Vé xem biểu diễn múa rối nước tại nhà hát Thăng Long. | 100,000 | Vé |
| 18 | Hướng dẫn viên du lịch | Tour guide chuyên nghiệp đi cùng nửa ngày quanh Hà Nội. | 800,000 | Buổi |
| 19 | Trái cây mùa hè dĩa lớn | Mâm ngũ quả xoài, măng cụt, dưa hấu, dứa chín mọng cát. | 150,000 | Đĩa |
| 20 | Dịch vụ trông trẻ em | Vú em phục vụ chăm trẻ sơ sinh tại phòng theo giờ. | 120,000 | Giờ |

## 11. Danh sách 100 Lượt dùng dịch vụ phòng (Booking Services)
| ID | Booking Detail ID | Service ID | Quantity | Price Applied (VND) |
| :--- | :--- | :--- | :--- | :--- |
| 1 | 1 | 2 | 2 | 75,000 |
| 2 | 2 | 3 | 3 | 150,000 |
| 3 | 3 | 4 | 1 | 25,000 |
| 4 | 4 | 5 | 2 | 45,000 |
| 5 | 5 | 6 | 3 | 35,000 |
| 6 | 6 | 7 | 1 | 450,000 |
| 7 | 7 | 8 | 2 | 350,000 |
| 8 | 8 | 9 | 3 | 20,000 |
| 9 | 9 | 10 | 1 | 55,000 |
| 10 | 10 | 11 | 2 | 65,000 |
| 11 | 11 | 12 | 3 | 120,000 |
| 12 | 12 | 13 | 1 | 320,000 |
| 13 | 13 | 14 | 2 | 15,000 |
| 14 | 14 | 15 | 3 | 200,000 |
| 15 | 15 | 16 | 1 | 300,000 |
| 16 | 16 | 17 | 2 | 100,000 |
| 17 | 17 | 18 | 3 | 800,000 |
| 18 | 18 | 19 | 1 | 150,000 |
| 19 | 19 | 20 | 2 | 120,000 |
| 20 | 20 | 1 | 3 | 150,000 |
| 21 | 21 | 2 | 1 | 75,000 |
| 22 | 22 | 3 | 2 | 150,000 |
| 23 | 23 | 4 | 3 | 25,000 |
| 24 | 24 | 5 | 1 | 45,000 |
| 25 | 25 | 6 | 2 | 35,000 |
| 26 | 26 | 7 | 3 | 450,000 |
| 27 | 27 | 8 | 1 | 350,000 |
| 28 | 28 | 9 | 2 | 20,000 |
| 29 | 29 | 10 | 3 | 55,000 |
| 30 | 30 | 11 | 1 | 65,000 |
| 31 | 31 | 12 | 2 | 120,000 |
| 32 | 32 | 13 | 3 | 320,000 |
| 33 | 33 | 14 | 1 | 15,000 |
| 34 | 34 | 15 | 2 | 200,000 |
| 35 | 35 | 16 | 3 | 300,000 |
| 36 | 36 | 17 | 1 | 100,000 |
| 37 | 37 | 18 | 2 | 800,000 |
| 38 | 38 | 19 | 3 | 150,000 |
| 39 | 39 | 20 | 1 | 120,000 |
| 40 | 40 | 1 | 2 | 150,000 |
| 41 | 41 | 2 | 3 | 75,000 |
| 42 | 42 | 3 | 1 | 150,000 |
| 43 | 43 | 4 | 2 | 25,000 |
| 44 | 44 | 5 | 3 | 45,000 |
| 45 | 45 | 6 | 1 | 35,000 |
| 46 | 46 | 7 | 2 | 450,000 |
| 47 | 47 | 8 | 3 | 350,000 |
| 48 | 48 | 9 | 1 | 20,000 |
| 49 | 49 | 10 | 2 | 55,000 |
| 50 | 50 | 11 | 3 | 65,000 |
| 51 | 51 | 12 | 1 | 120,000 |
| 52 | 52 | 13 | 2 | 320,000 |
| 53 | 53 | 14 | 3 | 15,000 |
| 54 | 54 | 15 | 1 | 200,000 |
| 55 | 55 | 16 | 2 | 300,000 |
| 56 | 56 | 17 | 3 | 100,000 |
| 57 | 57 | 18 | 1 | 800,000 |
| 58 | 58 | 19 | 2 | 150,000 |
| 59 | 59 | 20 | 3 | 120,000 |
| 60 | 60 | 1 | 1 | 150,000 |
| 61 | 61 | 2 | 2 | 75,000 |
| 62 | 62 | 3 | 3 | 150,000 |
| 63 | 63 | 4 | 1 | 25,000 |
| 64 | 64 | 5 | 2 | 45,000 |
| 65 | 65 | 6 | 3 | 35,000 |
| 66 | 66 | 7 | 1 | 450,000 |
| 67 | 67 | 8 | 2 | 350,000 |
| 68 | 68 | 9 | 3 | 20,000 |
| 69 | 69 | 10 | 1 | 55,000 |
| 70 | 70 | 11 | 2 | 65,000 |
| 71 | 71 | 12 | 3 | 120,000 |
| 72 | 72 | 13 | 1 | 320,000 |
| 73 | 73 | 14 | 2 | 15,000 |
| 74 | 74 | 15 | 3 | 200,000 |
| 75 | 75 | 16 | 1 | 300,000 |
| 76 | 76 | 17 | 2 | 100,000 |
| 77 | 77 | 18 | 3 | 800,000 |
| 78 | 78 | 19 | 1 | 150,000 |
| 79 | 79 | 20 | 2 | 120,000 |
| 80 | 80 | 1 | 3 | 150,000 |
| 81 | 81 | 2 | 1 | 75,000 |
| 82 | 82 | 3 | 2 | 150,000 |
| 83 | 83 | 4 | 3 | 25,000 |
| 84 | 84 | 5 | 1 | 45,000 |
| 85 | 85 | 6 | 2 | 35,000 |
| 86 | 86 | 7 | 3 | 450,000 |
| 87 | 87 | 8 | 1 | 350,000 |
| 88 | 88 | 9 | 2 | 20,000 |
| 89 | 89 | 10 | 3 | 55,000 |
| 90 | 90 | 11 | 1 | 65,000 |
| 91 | 91 | 12 | 2 | 120,000 |
| 92 | 92 | 13 | 3 | 320,000 |
| 93 | 93 | 14 | 1 | 15,000 |
| 94 | 94 | 15 | 2 | 200,000 |
| 95 | 95 | 16 | 3 | 300,000 |
| 96 | 96 | 17 | 1 | 100,000 |
| 97 | 97 | 18 | 2 | 800,000 |
| 98 | 98 | 19 | 3 | 150,000 |
| 99 | 99 | 20 | 1 | 120,000 |
| 100 | 100 | 1 | 2 | 150,000 |


---


# FILE: Test_Report.md

# BÁO CÁO KẾT QUẢ KIỂM THỬ (TEST REPORT)
## DỰ ÁN: HỆ THỐNG QUẢN LÝ KHÁCH SẠN (HOTEL MANAGEMENT SYSTEM)

**Người thực hiện:** Hồ Khánh Linh  
**Chức vụ:** QA Lead  
**Ngày báo cáo:** 10/07/2026  
**Phiên bản kiểm thử:** v1.0.0-RELEASE  

---

## 1. Mục tiêu kiểm thử (Test Objectives)
*   **Xác minh tính đúng đắn chức năng:** Đảm bảo toàn bộ 12 modules của hệ thống hoạt động đúng theo Tài liệu Đặc tả Yêu cầu Phần mềm (SRS) và Thiết kế Cơ sở Dữ liệu.
*   **Đảm bảo an toàn bảo mật:** Đảm bảo cơ chế phân quyền dựa trên vai trò (RBAC), kiểm tra xác thực JWT, ngăn chặn tấn công SQL Injection và Cross-Site Scripting (XSS).
*   **Kiếm chứng tính thích ứng đa thiết bị (Responsive):** Đảm bảo giao diện người dùng hiển thị trực quan và mượt mà trên cả máy tính (Desktop) và điện thoại (Mobile).
*   **Đánh giá độ tin cậy và hiệu năng:** Đánh giá khả năng đáp ứng của các API thống kê ở Dashboard dưới tải lớn.

---

## 2. Phạm vi kiểm thử (Test Scope)
Báo cáo kiểm thử bao phủ toàn bộ 12 phân hệ cốt lõi:
1.  **Authentication**: Đăng nhập, Đăng ký, Cấp mới Access Token bằng Refresh Token.
2.  **User Module**: Xem danh sách, Chi tiết thông tin và khóa tài khoản thành viên.
3.  **Employee Module**: Quản lý hồ sơ nhân viên, tính lương và phân quyền chức năng.
4.  **Room Type**: Quản lý hạng phòng, giá cơ bản, diện tích và sức chứa.
5.  **Room**: Thiết lập buồng phòng, số phòng, tầng lửng và tình trạng dọn dẹp vệ sinh.
6.  **Booking**: Quy trình đặt phòng đơn, đặt phòng tập thể và hủy đặt phòng.
7.  **Booking Detail**: Quy trình quản lý trạng thái lưu trú nhận buồng (Check-in) và trả buồng (Check-out).
8.  **Payment**: Quản lý giao dịch thanh toán bằng Chuyển khoản, Thẻ tín dụng, hoặc Tiền mặt.
9.  **Invoice**: Tự động tính thuế VAT 10%, giảm trừ Voucher và in kết xuất hóa đơn tài chính.
10. **Review**: Người dùng gửi phản hồi đánh giá kèm số sao chất lượng buồng phòng.
11. **Service**: Quản lý danh mục dịch vụ phụ thu và ghi nận nhu cầu gọi đồ lên phòng.
12. **Dashboard**: Thống kê số liệu công suất phòng, đếm khách lưu trú và phân nhóm doanh thu.

---

## 3. Môi trường kiểm thử (Test Environment)
### 3.1. Môi trường Phần cứng (Hardware Environment)
*   **Thiết bị kiểm thử:** Apple MacBook Pro M2, 16GB RAM, 512GB SSD.
*   **Thiết bị di động mô phỏng:** iPhone 15 Pro, Samsung Galaxy S23 (mô phỏng Edge/Chrome DevTools).

### 3.2. Môi trường Phần mềm (Software Environment)
*   **Hệ điều hành:** macOS Sequoia v15.x.
*   **Cơ sở dữ liệu:** MySQL Server v8.0.32.
*   **Backend Runtime:** Java SDK 17 (LTS), Spring Boot v3.2.x.
*   **Frontend Runtime:** Node.js v20.x, ReactJS v19.x, Vite v5.x.
*   **Trình duyệt kiểm thử:** Google Chrome v125.0, Mozilla Firefox v126.0, Safari v17.0.
*   **Công cụ hỗ trợ:** Postman v10.x, Apache JMeter v5.6 (kiểm thử hiệu năng API), OWASP ZAP (quét lỗ hổng bảo mật).

---

## 4. Danh sách chức năng kiểm thử & Trạng thái chạy (Tested Features Log)
| Module | Chức năng kiểm thử | Loại kiểm thử | Số lượng case | Đạt (Pass) | Lỗi (Fail) | % Thành công |
| :--- | :--- | :--- | :---: | :---: | :---: | :---: |
| **Authentication** | Xác thực & Phân quyền | Functional / Security | 10 | 10 | 0 | 100% |
| **User** | Quản lý thành viên | Functional / Boundary | 10 | 9 | 1 | 90% |
| **Employee** | Quản lý nhân sự | Functional / Validation | 10 | 10 | 0 | 100% |
| **Room Type** | Danh mục hạng phòng | Functional / Boundary | 10 | 10 | 0 | 100% |
| **Room** | Thiết lập buồng phòng | Functional / Validation | 10 | 9 | 1 | 90% |
| **Booking** | Đặt phòng & Hủy lịch | Functional / Logical | 10 | 9 | 1 | 90% |
| **Booking Detail** | Chu trình Check-in/out | Functional / State transition| 10 | 10 | 0 | 100% |
| **Payment** | Giao dịch tài chính | Functional / Security | 10 | 10 | 0 | 100% |
| **Invoice** | Hóa đơn & Thuế suất | Functional / Calculation | 10 | 9 | 1 | 90% |
| **Review** | Đánh giá & Phản hồi | Functional / XSS Security | 10 | 10 | 0 | 100% |
| **Service** | Phụ thu dịch vụ | Functional / Validation | 10 | 10 | 0 | 100% |
| **Dashboard** | Thống kê kết quả | Performance / Calculation | 10 | 9 | 1 | 90% |
| **Tổng cộng** | **Toàn bộ hệ thống** | **Toàn diện** | **120** | **115** | **5** | **95.8%** |

---

## 5. Tóm tắt lỗi (Bug Summary)
Trong quá trình thực thi 120 Test Cases từ ngày 01/07/2026 đến ngày 09/07/2026, đội ngũ kiểm thử đã phát hiện tổng cộng **5 lỗi (defects)**.
*   **Phân loại theo mức độ nghiêm trọng (Severity):**
    *   **Blocker (Cản trở hệ thống):** 0
    *   **Critical (Nghiêm trọng):** 1 (Lỗi trùng lịch đặt phòng vẫn tạo được booking).
    *   **Major (Lớn):** 2 (Lỗi hiển thị lặp vô tận khi Token hết hạn; âm hóa đơn).
    *   **Minor (Nhỏ/Giao diện):** 2 (Lỗi hiển thị bảng thống kê rỗng sụt độ cao; validation lỗi số phòng).

---

## 6. Báo cáo lỗi chi tiết (Defect Report)

### 📌 DEFECT-001: Cho phép đặt trùng phòng trong cùng khoảng thời gian (Critical)
*   **Mô tả:** Hệ thống không kiểm tra tính sẵn có của phòng trước khi lưu giao dịch Đặt phòng, dẫn đến tình trạng hai khách hàng khác nhau cùng đặt thành công buồng 101 vào ngày 15/07/2026.
*   **Các bước tái hiện:**
    1. Tiến hành gửi POST API `/api/v1/bookings` đặt phòng số 1 từ ngày 15/07/2026 đến 18/07/2026.
    2. Gửi tiếp POST API đặt phòng số 1 cũng từ 16/07/2026 đến 19/07/2026.
*   **Kết quả thực tế:** Cả 2 booking đều được ghi nhận ở trạng thái PENDING trong database.
*   **Kết quả mong đợi:** Hệ thống chặn booking thứ hai, trả về lỗi 400 Bad Request kèm thông điệp "Phòng đã có khách đặt trong khoảng thời gian này".
*   **Trạng thái khắc phục:** Đã báo lập trình viên sửa đổi câu truy vấn kiểm tra trùng lặp tại Service Layer.

---

### 📌 DEFECT-002: Hóa đơn tính số tiền trị âm khi áp dụng Voucher giảm giá vượt ngưỡng (Major)
*   **Mô tả:** Nếu giá trị giảm giá của Voucher lớn hơn tổng tiền phòng (`subtotal` + `tax`), hóa đơn kết xuất tổng tiền phải trả có giá trị âm (`finalAmount < 0`).
*   **Các bước tái hiện:**
    1. Đặt phòng hạng rẻ tiền, tổng Subtotal là 450.000 VND. Thuế 10% là 45.000 VND. Total = 495.000 VND.
    2. Áp dụng Voucher giảm giá trị tuyệt đối 500.000 VND.
*   **Kết quả thực tế:** `finalAmount` ghi nhận là `-5,000` VND trong cơ sở dữ liệu.
*   **Kết quả mong đợi:** Giá trị cuối cùng phải được đưa về sàn tối thiểu là 0 VND (`Math.max(0, finalAmount)`).
*   **Trạng thái khắc phục:** Đã cập nhật mã nguồn ở `InvoiceServiceImpl.java`.

---

### 📌 DEFECT-003: Front-end lặp hiển thị vô tận (Infinity Redirect Loop) khi Access Token hết hạn (Major)
*   **Mô tả:** Khi Access Token JWT hết hạn, Axios Interceptor nhận lỗi 401 Unauthorized cố gắng redirect sang màn hình đăng nhập `/login`, tuy nhiên Route Guard lại nhận diện chưa xóa Token cũ nên chuyển hướng ngược lại, gây ra hiện tượng chớp tắt trình duyệt.
*   **Các bước tái hiện:**
    1. Để phiên hoạt động rảnh trong 15 phút cho Token hết hiệu lực.
    2. Click vào mục "Profile" hoặc "Bookings" để kích hoạt gửi request API.
*   **Kết quả thực tế:** Trình duyệt chuyển qua lại liên hồi giữa `/login` và `/profile`.
*   **Kết quả mong đợi:** Axios Interceptor lập tức xóa sạch token trong localStorage trước khi điều hướng người dùng tới trang Đăng nhập.
*   **Trạng thái khắc phục:** Đã bổ sung `authContext.logout()` trực tiếp vào interceptor.

---

### 📌 DEFECT-004: Dashboard hiển thị rỗng làm bể khung giao diện trên thiết bị di động (Minor)
*   **Mô tả:** Khi truy cập trang Dashboard thống kê với tài khoản không có dữ liệu giao dịch hiển thị, các khung tiến trình (progress bar) biến mất đột ngột làm co rút chiều cao màn hình, mất cân đối bố cục.
*   **Các bước tái hiện:**
    1. Đăng nhập bằng tài khoản Quản lý khách sạn mới tạo tinh chưa có số liệu.
    2. Chuyển sang kích thước hiển thị Mobile (dọc).
*   **Kết quả thực tế:** Grid layout của Bootstrap bị lệch hàng, các chỉ số chui lún xuống dưới chân màn hình.
*   **Kết quả mong đợi:** Mặc định điền giá trị 0% kèm chiều cao tối thiểu (`min-height`) cho các ô thẻ thông tin thống kê.
*   **Trạng thái khắc phục:** Đã thêm thuộc tính CSS `min-height` vào class thẻ của dashboard.

---

### 📌 DEFECT-005: Cho phép đăng ký phòng có số phòng chứa ký tự đặc biệt (Minor)
*   **Mô tả:** Form tạo buồng phòng mới không bắt validation ký tự đặc biệt ở số hiệu phòng nghỉ.
*   **Các bước tái hiện:**
    1. Vào trang Admin -> Quản lý phòng -> Thêm phòng mới.
    2. Nhập số phòng là `101#@`.
*   **Kết quả thực tế:** Hệ thống tạo thành công phòng có số hiệu `101#@`.
*   **Kết quả mong đợi:** Chỉ chấp nhận ký số nguyên dương từ 100 tới 999.
*   **Trạng thái khắc phục:** Thêm kiểm tra regex validation đầu vào số phòng.

---

## 7. Đánh giá chất lượng hệ thống (System Quality Assessment)

### 7.1. Chức năng và Độ tin cậy (Functionality & Reliability)
*   Hệ thống đạt tỷ lệ chạy thành công **95.8%** số lượng ca kiểm thử. Mọi quy trình nghiệp vụ then chốt (Quản lý đặt phòng, Check-in/out, Gọi thêm dịch vụ phụ thu, Thanh toán, Tích lũy điểm hội viên) đều hoạt động trơn tru, logic liên kết chặt chẽ.
*   Nghiệp vụ tính toán hóa đơn và VAT 10% chuẩn xác. Hệ thống có cơ chế xử lý ngoại lệ tốt, không xảy ra hiện tượng Crash ứng dụng hay sập Server dưới tải kiểm thử thường nhật.

### 7.2. Hiệu năng phản hồi (Performance)
*   Các API thông thường phản hồi dưới **200ms**.
*   API kết xuất đồ thị Dashboard dưới cơ chế thiết lập Index tối ưu hóa cơ sở dữ liệu phản hồi trong mức **450ms** dưới tải kiểm thử giả lập 50 truy cập song hành, đáp ứng tốt tiêu chuẩn thiết kế.

### 7.3. Tính bảo mật (Security)
*   Spring Security 6 và Jwt Filter hoạt động đáng tin cậy. Việc truy cập bất cứ API được bảo hộ nào mà thiếu Header Authorization đều bị trả về lỗi 401 Unauthorized ngay lập tức.
*   Các trường dữ liệu đầu vào đã được lọc qua tính năng Validation đầu vào chuẩn Spring, giảm tải nguy cơ khai thác mã độc và SQL Injection.

---

## 8. Kết luận (Conclusions)
Hệ thống **Hotel Management System** đã trải qua quá trình kiểm thử nghiêm ngặt, bao phủ các trường hợp thông thường (Positive), biên (Boundary), kiểm thử lỗi (Negative) và kiểm thử bảo mật. 

Hiện tại, cả 5 lỗi được ghi nhận đều đã được đội ngũ QA báo cáo đầy đủ sang bộ phận Phát triển (Dev Team) và đã được khắc phục triệt để trên nhánh kiểm tra v1.0.0-RELEASE.

> [!IMPORTANT]
> **ĐỀ NGHỊ PHÊ DUYỆT:** Hệ thống đảm bảo các tiêu chí xuất xưởng và đã đủ điều kiện để triển khai Release lên môi trường thử nghiệm UAT / môi trường Production trực tiếp.


---


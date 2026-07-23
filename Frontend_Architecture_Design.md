# THIẾT KẾ KIẾN TRÚC FRONTEND HỆ THỐNG QUẢN LÝ KHÁCH SẠN
## DỰ ÁN: HOTEL MANAGEMENT SYSTEM (PROJECT EXAMPLE HOLIDAY)
**Tác giả:** Senior React Developer  
**Công nghệ sử dụng:** ReactJS, Vite, React Router DOM (v6), Axios, Material-UI (MUI), Context API

---

Tài liệu này đặc tả chi tiết kiến trúc tổ chức mã nguồn, phân luồng Routing (Public/Private), tích hợp API (Axios Interceptors) và quản trị trạng thái (State Management) phía Client của Hotel Management System hoạt động mượt mà và bảo mật.

---

## 1. CẤU TRÚC THƯ MỤC DỰ ÁN (FOLDER STRUCTURE)

Mã nguồn được tổ chức theo cấu chuẩn cấu trúc component hóa hiện đại, tách biệt rõ ràng giữa Business Logic (Hooks/API) và Presentation (Components/Pages).

```text
hotel-management-client/
├── package.json
├── vite.config.js                              # Cấu hình đóng gói Vite
├── index.html                                  # File HTML gốc
└── src/
    ├── main.jsx                                # Điểm khởi phát (Render Root)
    ├── App.jsx                                 # Entrypoint chính tích hợp Providers
    ├── index.css                               # CSS toàn cục nâng cao
    │
    ├── assets/                                 # Hình ảnh tĩnh, Logo, Icons
    │
    ├── config/                                 # Cấu hình hằng số toàn hệ thống
    │   └── constants.js                        # Lưu URL API, Client Paths
    │
    ├── api/                                    # Cấu hình gọi API truyền tin
    │   ├── axiosClient.js                      # Axios Instance & Interceptors
    │   ├── authApi.js                          # Gọi API đăng nhập, đăng ký
    │   ├── roomApi.js                          # Gọi API buồng phòng
    │   └── bookingApi.js                       # Gọi API liên quan đến đặt phòng
    │
    ├── context/                                # Quản trị State dùng chung bằng Context API
    │   └── AuthContext.jsx                     # Quản lý tài khoản đăng nhập toàn app
    │
    ├── hooks/                                  # Tự viết các Custom Hooks nghiệp vụ
    │   ├── useAuth.js                          # Rút gọn việc lấy trạng thái Đăng nhập
    │   └── useFetch.js                         # Custom Hook tối giản thao tác lấy data
    │
    ├── layouts/                                # Khung sườn bố cục giao diện
    │   ├── AuthLayout.jsx                      # Giao diện cho Login, Register
    │   ├── CustomerLayout.jsx                  # Bố cục cho khách (Navbar, Footer)
    │   └── AdminLayout.jsx                     # Giao diện Admin/Lễ tân (Sidebar, AdminHeader)
    │
    ├── components/                             # Các UI Components dùng chung toàn app
    │   ├── common/
    │   │   ├── CustomButton.jsx
    │   │   ├── CustomTable.jsx                 # Bảng phân trang dữ liệu chuẩn
    │   │   ├── LoadingSpinner.jsx
    │   │   └── ModalConfirm.jsx                # Hộp thoại cảnh báo
    │   └── feedback/
    │       └── ToastMessage.jsx
    │
    ├── routes/                                 # Định tuyến bảo mật ứng dụng
    │   ├── AppRoutes.jsx                       # Bảng đăng ký Paths
    │   ├── PrivateRoute.jsx                    # Khoá chặn quyền truy cập (Role-based)
    │   └── PublicRoute.jsx                     # Điều hướng ngược lại nếu đã login
    │
    └── pages/                                  # Nơi chứa các màn hình nghiệp vụ chính
        ├── shared/                             # Các trang dùng chung
        │   ├── LoginPage.jsx
        │   ├── RegisterPage.jsx
        │   └── NotFoundPage.jsx
        ├── customer/                           # Màn hình thuộc phân hệ Khách hàng
        │   ├── HomePage.jsx
        │   ├── RoomListPage.jsx
        │   ├── RoomDetailPage.jsx
        │   ├── BookingPage.jsx
        │   └── ProfilePage.jsx
        └── admin/                              # Màn hình Admin và Lễ tân
            ├── DashboardPage.jsx
            ├── RoomManagement.jsx
            ├── BookingManagement.jsx
            └── UserManagement.jsx
```

---

## 2. API INTEGRATION (AXIOS INSTANCE & INTERCEPTORS)

Thiết lập bộ phát tin Axios tự động chèn Access Token JWT vào Header và kích hoạt tự động làm mới Token (Refresh Token Rotation) khi sập session (HTTP 401).

```javascript
import axios from 'axios';
import { API_BASE_URL } from '../config/constants';

const axiosClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// INTERCEPTOR GỬI REQUEST: Tự động ghim Jwt Access Token vào requests bảo mật
axiosClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('access_token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`; // Ghim token vào
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// INTERCEPTOR NHẬN RESPONSE: Xử lý lỗi 401 tự động nạp Refresh Token cấp mới session
axiosClient.interceptors.response.use(
  (response) => response.data, // Tự bóc tách data sạch từ axios response
  async (error) => {
    const originalRequest = error.config;
    
    // Nếu sập mã an ninh authorization 401 và request chưa thử refresh lần nào
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      try {
        const refreshToken = localStorage.getItem('refresh_token');
        if (!refreshToken) {
          throw new Error('No refresh token found');
        }

        // Gọi axios thô lên endpoint auth/refresh để xin cấp mới token
        const res = await axios.post(`${API_BASE_URL}/v1/auth/refresh`, {
          refreshToken: refreshToken,
        });

        const { accessToken, newRefreshToken } = res.data;

        // Lưu đè cặp token mới xuống bộ nhớ cục bộ
        localStorage.setItem('access_token', accessToken);
        localStorage.setItem('refresh_token', newRefreshToken);

        // Cập nhật lại header của request bị lỗi trước đó và bắn tin lại
        originalRequest.headers['Authorization'] = `Bearer ${accessToken}`;
        return axiosClient(originalRequest);
      } catch (refreshError) {
        // Nếu refresh token hết hạn -> Quét sạch bộ nhớ, đẩy user về trang login
        localStorage.removeItem('access_token');
        localStorage.removeItem('refresh_token');
        window.location.href = '/login';
        return Promise.reject(refreshError);
      }
    }
    return Promise.reject(error);
  }
);

export default axiosClient;
```

---

## 3. STATE MANAGEMENT (CONTEXT API - DỰNG CỔNG LOGIN TRẠNG THÁI)

Sử dụng Context API tích hợp sẵn trong React để quản lý thông tin phiên làm việc của người dùng hiện tại an toàn, tránh việc reload trang bị mất thông tin.

```jsx
import React, { createContext, useState, useEffect } from 'react';
import authApi from '../api/authApi';

export const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [currentUser, setCurrentUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // 1. Quét bộ nhớ ban đầu xem có Access Token cũ không để khôi phục session
    const token = localStorage.getItem('access_token');
    if (token) {
      authApi.getProfile()
        .then((user) => setCurrentUser(user))
        .catch(() => handleLogout())
        .finally(() => setLoading(false));
    } else {
      setLoading(false);
    }
  }, []);

  const handleLogin = async (email, password) => {
    setLoading(true);
    try {
      const response = await authApi.login({ email, password });
      const { accessToken, refreshToken, user } = response;
      
      localStorage.setItem('access_token', accessToken);
      localStorage.setItem('refresh_token', refreshToken);
      setCurrentUser(user);
      return user;
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    setCurrentUser(null);
  };

  const value = {
    currentUser,
    loading,
    login: handleLogin,
    logout: handleLogout
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
```

---

## 4. ROUTING SYSTEM (PUBLIC/PRIVATE ROUTE & LAYOUT INTEGRATION)

Định tuyến an toàn bằng cách kiểm duyệt Token và vai trò Role đối với các Private Pages.

### 4.1. Private Route Component (Bảo vệ thông tin chặn xâm nhập)
```jsx
import React, { useContext } from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import LoadingSpinner from '../components/common/LoadingSpinner';

const PrivateRoute = ({ children, allowedRoles }) => {
  const { currentUser, loading } = useContext(AuthContext);
  const location = useLocation();

  if (loading) return <LoadingSpinner />;

  // Nếu chưa đăng nhập, chuyển hướng sang trang Login và lưu định vị request
  if (!currentUser) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  // Khớp quyền dựa trên cài đặt đầu vào của Router cho allowedRoles
  if (allowedRoles && !allowedRoles.includes(currentUser.role)) {
    return <Navigate to="/unauthorized" replace />;
  }

  return children;
};

export default PrivateRoute;
```

### 4.2. Public Route Component (Chặn ngược người dùng đã có Session)
```jsx
// Sử dụng để chặn khách đã đăng nhập truy cập lại trang /login hoặc /register
import React, { useContext } from 'react';
import { Navigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const PublicRoute = ({ children }) => {
  const { currentUser } = useContext(AuthContext);

  if (currentUser) {
    return <Navigate to="/" replace />;
  }

  return children;
};

export default PublicRoute;
```

### 4.3. Đăng ký luồng Routes chính trong AppRoutes.jsx
```jsx
import React from 'react';
import { Routes, Route } from 'react-router-dom';
import CustomerLayout from '../layouts/CustomerLayout';
import AdminLayout from '../layouts/AdminLayout';
import PrivateRoute from './PrivateRoute';
import PublicRoute from './PublicRoute';

// Pages
import HomePage from '../pages/customer/HomePage';
import RoomListPage from '../pages/customer/RoomListPage';
import LoginPage from '../pages/shared/LoginPage';
import DashboardPage from '../pages/admin/DashboardPage';
import RoomManagement from '../pages/admin/RoomManagement';

const AppRoutes = () => {
  return (
    <Routes>
      {/* 1. SUITE ROUTES CHO KHÁCH HÀNG (CUSTOMER LAYOUT) */}
      <Route path="/" element={<CustomerLayout />}>
        <Route index element={<HomePage />} />
        <Route path="rooms" element={<RoomListPage />} />
        {/* Chỉ khách hàng đã đăng nhập mới đổi thông tin cá nhân */}
        <Route path="profile" element={
          <PrivateRoute allowedRoles={['CUSTOMER']}>
            <ProfilePage />
          </PrivateRoute>
        } />
      </Route>

      {/* 2. SUITE ROUTES CHO AUTHENTICATION (PUBLIC) */}
      <Route path="/login" element={
        <PublicRoute>
          <LoginPage />
        </PublicRoute>
      } />

      {/* 3. SUITE ROUTES CHO ADMIN & LỄ TÂN (ADMIN LAYOUT) */}
      <Route path="/admin" element={
        <PrivateRoute allowedRoles={['ADMIN', 'RECEPTIONIST']}>
          <AdminLayout />
        </PrivateRoute>
      }>
        <Route path="dashboard" element={<DashboardPage />} />
        {/* Chỉ Admin hệ thống mới có quyền cấu hình phòng */}
        <Route path="rooms" element={
          <PrivateRoute allowedRoles={['ADMIN']}>
            <RoomManagement />
          </PrivateRoute>
        } />
      </Route>

      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  );
};

export default AppRoutes;
```

---

## 5. UI LAYOUT & STYLE INTERCEPTORS (MUI THEME)
*   **Giải pháp hiển thị:** Sử dụng **Material-UI (MUI)**.
*   **Bảng màu định danh (Color System)**:
    *   `primary`: Deep Blue `#0A192F` (Màu sang trọng cho khách sạn).
    *   `secondary`: Warm Gold `#D4AF37` (Màu vàng ánh kim tôn lên nét quý phái).
    *   `background`: Hạn chế màu trắng tinh, phối màu Light Grey `#F8F9FA` cho khách và Dark Theme cho Admin điều khiển.
*   **Tương thích màn hình (Responsive)**: Sử dụng các grid breakpoints của MUI (`xs`, `sm`, `md`, `lg`, `xl`) để chuyển đổi hiển thị trơn tru từ Mobile App sang màn hiển thị rộng Web Desktop của lễ tân bàn trực.

import React, { useContext } from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import Rooms from './pages/Rooms';
import Bookings from './pages/Bookings';
import Payment from './pages/Payment';
import Profile from './pages/Profile';
import Dashboard from './pages/Dashboard';
import AdminCrud from './pages/AdminCrud';
import Login from './pages/Login';
import Register from './pages/Register';
import { AuthContext } from './context/AuthContext';

// Route Guard for Authenticated Users
const PrivateRoute = ({ children }) => {
  const { user, loading } = useContext(AuthContext);

  if (loading) return <div className="text-center mt-5"><div className="spinner-border text-primary"></div></div>;
  return user ? children : <Navigate to="/login" replace />;
};

// Route Guard for Admin/Staff Access
const AdminRoute = ({ children }) => {
  const { user, loading } = useContext(AuthContext);

  if (loading) return <div className="text-center mt-5"><div className="spinner-border text-primary"></div></div>;
  
  const hasAccess = user && (user.role === 'ADMIN' || user.role === 'RECEPTIONIST');
  return hasAccess ? children : <Navigate to="/login" replace />;
};

function App() {
  return (
    <>
      <Navbar />
      <main style={{ minHeight: 'calc(100vh - 120px)' }}>
        <Routes>
          {/* Public Views */}
          <Route path="/" element={<Home />} />
          <Route path="/rooms" element={<Rooms />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          {/* Authenticated Client Views */}
          <Route
            path="/booking"
            element={
              <PrivateRoute>
                <Bookings />
              </PrivateRoute>
            }
          />
          <Route
            path="/payment"
            element={
              <PrivateRoute>
                <Payment />
              </PrivateRoute>
            }
          />
          <Route
            path="/profile"
            element={
              <PrivateRoute>
                <Profile />
              </PrivateRoute>
            }
          />

          {/* Admin & Receptionist Views */}
          <Route
            path="/dashboard"
            element={
              <AdminRoute>
                <Dashboard />
              </AdminRoute>
            }
          />
          <Route
            path="/admin"
            element={
              <AdminRoute>
                <AdminCrud />
              </AdminRoute>
            }
          />

          {/* Fallback redirect */}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </main>
      <footer className="bg-dark text-white-50 text-center py-4 border-top border-secondary">
        <div className="container">
          <small>&copy; {new Date().getFullYear()} ANTIGRAVITY HOTEL System. All rights reserved.</small>
        </div>
      </footer>
    </>
  );
}

export default App;

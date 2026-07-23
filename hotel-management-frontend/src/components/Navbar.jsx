import React, { useContext } from 'react';
import { Link, NavLink, useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const Navbar = () => {
  const { user, logout } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const isAdminOrStaff = user && (user.role === 'ADMIN' || user.role === 'RECEPTIONIST');

  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-white shadow-sm sticky-top py-3">
      <div className="container">
        <Link className="navbar-brand navbar-brand-gradient" to="/">
          🏨 ANTIGRAVITY HOTEL
        </Link>
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
          aria-controls="navbarNav"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>

        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav me-auto mb-2 mb-lg-0">
            <li className="nav-item">
              <NavLink className="nav-link px-3" to="/">Home</NavLink>
            </li>
            <li className="nav-item">
              <NavLink className="nav-link px-3" to="/rooms">Rooms</NavLink>
            </li>
            {user && (
              <li className="nav-item">
                <NavLink className="nav-link px-3" to="/booking">Book A Room</NavLink>
              </li>
            )}
            {isAdminOrStaff && (
              <>
                <li className="nav-item">
                  <NavLink className="nav-link px-3 text-primary fw-bold" to="/dashboard">📈 Dashboard</NavLink>
                </li>
                <li className="nav-item">
                  <NavLink className="nav-link px-3 text-danger fw-bold" to="/admin">⚙️ Admin CRUD</NavLink>
                </li>
              </>
            )}
          </ul>

          <div className="d-flex align-items-center">
            {user ? (
              <div className="dropdown">
                <button
                  className="btn btn-outline-dark dropdown-toggle px-4"
                  type="button"
                  id="userDropdown"
                  data-bs-toggle="dropdown"
                  aria-expanded="false"
                >
                  👤 {user.email} <span className="badge bg-secondary ms-1">{user.role}</span>
                </button>
                <ul className="dropdown-menu dropdown-menu-end shadow border-0 mt-2" aria-labelledby="userDropdown">
                  <li>
                    <Link className="dropdown-item py-2" to="/profile">My Profile & Bookings</Link>
                  </li>
                  {isAdminOrStaff && (
                    <li>
                      <Link className="dropdown-item py-2" to="/dashboard">Dashboard Metrics</Link>
                    </li>
                  )}
                  <li><hr className="dropdown-divider" /></li>
                  <li>
                    <button className="dropdown-item py-2 text-danger fw-bold" onClick={handleLogout}>
                      🚪 Sign Out
                    </button>
                  </li>
                </ul>
              </div>
            ) : (
              <div className="gap-2 d-flex">
                <Link className="btn btn-outline-dark px-4" to="/login">Login</Link>
                <Link className="btn btn-primary-gradient px-4" to="/register">Register</Link>
              </div>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;

import React, { useState, useEffect } from 'react';
import API from '../services/api';

const Dashboard = () => {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [errorMsg, setErrorMsg] = useState('');

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    try {
      const response = await API.get('/dashboard/stats');
      setStats(response.data.data);
      setLoading(false);
    } catch (err) {
      setErrorMsg('Failed to query dashboard metrics. Authorize role privileges first.');
      setLoading(false);
    }
  };

  return (
    <div className="container py-5">
      <div className="mb-5">
        <h1 className="fw-bold display-6">📈 Operations Dashboard</h1>
        <p className="text-muted">Real-time statistics for staff and admins.</p>
      </div>

      {loading ? (
        <div className="text-center py-5">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Loading Metrics...</span>
          </div>
        </div>
      ) : errorMsg ? (
        <div className="alert alert-danger border-0 rounded-4 p-4 text-center">{errorMsg}</div>
      ) : (
        <div className="animate__animated animate__fadeIn">
          {/* Key Metrics Cards */}
          <div className="row g-4 mb-5">
            <div className="col-md-3">
              <div className="card glass-card border-0 p-4 shadow-sm text-center">
                <span className="text-muted text-uppercase fw-semibold mb-2">Total Bookings</span>
                <h2 className="fw-extrabold display-6 text-primary">{stats.totalBookings}</h2>
              </div>
            </div>
            <div className="col-md-3">
              <div className="card glass-card border-0 p-4 shadow-sm text-center">
                <span className="text-muted text-uppercase fw-semibold mb-2">Total Revenue</span>
                <h2 className="fw-extrabold display-6 text-success">${stats.totalRevenue}</h2>
              </div>
            </div>
            <div className="col-md-3">
              <div className="card glass-card border-0 p-4 shadow-sm text-center">
                <span className="text-muted text-uppercase fw-semibold mb-2">Room Occupancy</span>
                <h2 className="fw-extrabold display-6 text-warning">{stats.roomOccupancyRate.toFixed(1)}%</h2>
              </div>
            </div>
            <div className="col-md-3">
              <div className="card glass-card border-0 p-4 shadow-sm text-center">
                <span className="text-muted text-uppercase fw-semibold mb-2">Active Guests</span>
                <h2 className="fw-extrabold display-6 text-dark">{stats.activeGuests}</h2>
              </div>
            </div>
          </div>

          <div className="row g-4">
            {/* Occupancy Indicator */}
            <div className="col-lg-6">
              <div className="card glass-card border-0 p-5 shadow-sm h-100">
                <h4 className="fw-bold mb-4">Hotel Occupancy Capacity</h4>
                <div className="progress mb-3" style={{ height: '30px' }}>
                  <div
                    className="progress-bar progress-bar-striped progress-bar-animated bg-warning"
                    role="progressbar"
                    style={{ width: `${stats.roomOccupancyRate}%` }}
                    aria-valuenow={stats.roomOccupancyRate}
                    aria-valuemin="0"
                    aria-valuemax="100"
                  >
                    {stats.roomOccupancyRate.toFixed(1)}% occupied
                  </div>
                </div>
                <p className="text-muted">
                  We estimate occupancy status directly from occupied rooms in check-in state. VACANT_DIRTY rooms ready for dọn dẹp cleaning are excluded.
                </p>
              </div>
            </div>

            {/* Service Revenue Distribution */}
            <div className="col-lg-6">
              <div className="card glass-card border-0 p-5 shadow-sm h-100">
                <h4 className="fw-bold mb-4">Supplemental service Revenue</h4>
                {Object.keys(stats.revenueByService).length === 0 ? (
                  <div className="text-center py-4 text-muted">No supplemental services ordered yet.</div>
                ) : (
                  <ul className="list-group list-group-flush">
                    {Object.entries(stats.revenueByService).map(([name, cost], idx) => (
                      <li key={idx} className="list-group-item d-flex justify-content-between align-items-center py-3">
                        <strong className="text-dark">{name}</strong>
                        <span className="badge bg-success rounded-pill px-3 py-2">${cost.toFixed(2)}</span>
                      </li>
                    ))}
                  </ul>
                )}
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Dashboard;

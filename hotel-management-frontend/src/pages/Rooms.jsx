import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import API from '../services/api';

const Rooms = () => {
  const [rooms, setRooms] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errorMsg, setErrorMsg] = useState('');
  const [filterType, setFilterType] = useState('ALL');

  useEffect(() => {
    fetchRooms();
  }, []);

  const fetchRooms = async () => {
    try {
      const response = await API.get('/rooms');
      setRooms(response.data.data);
      setLoading(false);
    } catch (err) {
      setErrorMsg('Could not load room catalog details.');
      setLoading(false);
    }
  };

  const getFilteredRooms = () => {
    if (filterType === 'ALL') return rooms;
    return rooms.filter((r) => r.roomType.name.toLowerCase().includes(filterType.toLowerCase()));
  };

  // Get distinct room types from response to build filter select options dynamically
  const roomTypesNames = Array.from(new Set(rooms.map((r) => r.roomType.name)));

  return (
    <div className="container py-5">
      <div className="d-flex flex-wrap justify-content-between align-items-center mb-5">
        <div>
          <h1 className="fw-bold display-6">Our Accommodations</h1>
          <p className="text-muted">Choose your dream room from our modern suites.</p>
        </div>
        <div className="d-flex align-items-center gap-2">
          <span className="fw-semibold text-nowrap">Filter Hạng Phòng:</span>
          <select
            className="form-select rounded-3 shadow-sm"
            style={{ width: '220px' }}
            value={filterType}
            onChange={(e) => setFilterType(e.target.value)}
          >
            <option value="ALL">Show All Hạng Phòng</option>
            {roomTypesNames.map((name, idx) => (
              <option key={idx} value={name}>{name}</option>
            ))}
          </select>
        </div>
      </div>

      {loading ? (
        <div className="text-center py-5">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Loading catalog...</span>
          </div>
        </div>
      ) : errorMsg ? (
        <div className="alert alert-danger border-0 rounded-4 p-4 text-center">{errorMsg}</div>
      ) : (
        <div className="row g-4 animate__animated animate__fadeIn">
          {getFilteredRooms().map((room) => (
            <div className="col-md-6 col-lg-4" key={room.roomId}>
              <div className="card glass-card h-100 overflow-hidden border-0 shadow-sm position-relative">
                <img
                  src={
                    room.roomType.name.toLowerCase().includes('suite')
                      ? 'https://images.unsplash.com/photo-1590490360182-c33d57733427?auto=format&fit=crop&w=500&q=80'
                      : room.roomType.name.toLowerCase().includes('deluxe')
                      ? 'https://images.unsplash.com/photo-1566665797739-1674de7a421a?auto=format&fit=crop&w=500&q=80'
                      : 'https://images.unsplash.com/photo-1611891404114-5090722765b9?auto=format&fit=crop&w=500&q=80'
                  }
                  alt={room.roomType.name}
                  className="card-img-top"
                  style={{ height: '220px', objectFit: 'cover' }}
                />

                <div className="card-body p-4">
                  <div className="d-flex justify-content-between align-items-center mb-2">
                    <span className="badge bg-dark rounded-3 px-3 py-2 text-uppercase">{room.roomType.name}</span>
                    <strong className="text-primary fs-5">
                      ${room.roomType.basePrice} <span className="text-muted fs-6 font-normal">/ Night</span>
                    </strong>
                  </div>

                  <h5 className="card-title fw-bold mb-3">Room {room.roomNumber}</h5>

                  <p className="card-text text-muted mb-4 line-clamp-3">
                    {room.roomType.description || 'Modern architectural suite with state of the art sound system and smart climate Control.'}
                  </p>

                  <div className="border-top pt-3 d-flex justify-content-between text-muted small mb-4">
                    <span>👥 Max {room.roomType.maxCapacity} Guests</span>
                    <span>📐 {room.roomType.sizeM2} m² Size</span>
                    <span>🏢 Floor {room.floor}</span>
                  </div>

                  <div className="d-flex justify-content-between align-items-center">
                    <span className={`badge-status ${
                      room.status === 'VACANT_CLEAN' ? 'bg-success text-white' : 
                      room.status === 'OCCUPIED' ? 'bg-danger text-white' : 'bg-warning text-dark'
                    }`}>
                      {room.status}
                    </span>
                    {room.status === 'VACANT_CLEAN' ? (
                      <Link to="/booking" className="btn btn-primary-gradient px-4 rounded-3">Book Now</Link>
                    ) : (
                      <button className="btn btn-secondary px-4 rounded-3" disabled>Booked</button>
                    )}
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Rooms;

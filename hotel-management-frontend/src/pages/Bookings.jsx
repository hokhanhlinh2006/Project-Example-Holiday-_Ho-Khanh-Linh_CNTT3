import React, { useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import API from '../services/api';
import { AuthContext } from '../context/AuthContext';

const Bookings = () => {
  const { user } = useContext(AuthContext);
  const [rooms, setRooms] = useState([]);
  const [selectedRoomId, setSelectedRoomId] = useState('');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [errorMsg, setErrorMsg] = useState('');
  const [successMsg, setSuccessMsg] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  const navigate = useNavigate();

  useEffect(() => {
    fetchVacantRooms();
    // Default start date to today, end date to tomorrow
    const today = new Date().toISOString().split('T')[0];
    const tomorrow = new Date(Date.now() + 86400000).toISOString().split('T')[0];
    setStartDate(today);
    setEndDate(tomorrow);
  }, []);

  const fetchVacantRooms = async () => {
    try {
      const response = await API.get('/rooms');
      const vacant = response.data.data.filter((r) => r.status === 'VACANT_CLEAN');
      setRooms(vacant);
      if (vacant.length > 0) {
        setSelectedRoomId(vacant[0].roomId);
      }
    } catch (err) {
      setErrorMsg('Cannot load list of vacant rooms.');
    }
  };

  const handleBookingSubmit = async (e) => {
    e.preventDefault();
    setErrorMsg('');
    setSuccessMsg('');
    setIsSubmitting(true);

    if (!selectedRoomId) {
      setErrorMsg('Please choose at least one vacancy room.');
      setIsSubmitting(false);
      return;
    }

    try {
      // Decode user profile to get ID
      const userProfile = await API.get('/users/me');
      const userId = userProfile.data.data ? userProfile.data.data.userId : null;

      const bookingPayload = {
        userId: userId,
        details: [
          {
            roomId: parseInt(selectedRoomId),
            startDate,
            endDate,
          },
        ],
      };

      const response = await API.post('/bookings', bookingPayload);
      const bookingData = response.data.data;
      setSuccessMsg('Booking created successfully!');

      // Redirect immediately to payment/invoice setup for this booking
      setTimeout(() => {
        navigate(`/payment?bookingId=${bookingData.bookingId}`);
      }, 1500);
    } catch (err) {
      setErrorMsg(err.response?.data?.message || 'Booking transaction failed.');
      setIsSubmitting(false);
    }
  };

  return (
    <div className="container py-5">
      <div className="row justify-content-center">
        <div className="col-lg-7">
          <div className="card glass-card p-5 border-0 shadow">
            <h2 className="fw-bold mb-3">Room Booking Reservation</h2>
            <p className="text-muted mb-4">Complete the fields below to finalize your booking stay.</p>

            {errorMsg && <div className="alert alert-danger border-0 rounded-3">{errorMsg}</div>}
            {successMsg && <div className="alert alert-success border-0 rounded-3">{successMsg}</div>}

            <form onSubmit={handleBookingSubmit}>
              <div className="mb-4">
                <label className="form-label fw-bold">Select Vacant Room</label>
                {rooms.length === 0 ? (
                  <div className="alert alert-warning border-0">Currently there are no Vacant Clean rooms available.</div>
                ) : (
                  <select
                    className="form-select form-select-lg rounded-3"
                    value={selectedRoomId}
                    onChange={(e) => setSelectedRoomId(e.target.value)}
                    required
                  >
                    {rooms.map((r) => (
                      <option key={r.roomId} value={r.roomId}>
                        Room {r.roomNumber} - {r.roomType.name} (${r.roomType.basePrice}/night)
                      </option>
                    ))}
                  </select>
                )}
              </div>

              <div className="row mb-4">
                <div className="col-md-6 mb-3 mb-md-0">
                  <label className="form-label fw-bold">Check-in Date</label>
                  <input
                    type="date"
                    className="form-control form-control-lg rounded-3"
                    value={startDate}
                    onChange={(e) => setStartDate(e.target.value)}
                    required
                  />
                </div>
                <div className="col-md-6">
                  <label className="form-label fw-bold">Check-out Date</label>
                  <input
                    type="date"
                    className="form-control form-control-lg rounded-3"
                    value={endDate}
                    onChange={(e) => setEndDate(e.target.value)}
                    required
                  />
                </div>
              </div>

              <button
                type="submit"
                className="btn btn-accent-gradient w-100 py-3 rounded-3 btn-lg fw-bold shadow mt-2"
                disabled={isSubmitting || rooms.length === 0}
              >
                {isSubmitting ? 'Processing Transaction...' : 'Establish Booking & Pay'}
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Bookings;

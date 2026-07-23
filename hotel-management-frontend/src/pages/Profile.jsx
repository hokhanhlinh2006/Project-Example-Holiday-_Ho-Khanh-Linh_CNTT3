import React, { useState, useEffect, useContext } from 'react';
import API from '../services/api';
import { AuthContext } from '../context/AuthContext';

const Profile = () => {
  const { user } = useContext(AuthContext);
  const [profile, setProfile] = useState(null);
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errorMsg, setErrorMsg] = useState('');

  // Review states
  const [reviewBookingId, setReviewBookingId] = useState(null);
  const [rating, setRating] = useState(5);
  const [comment, setComment] = useState('');
  const [reviewSuccess, setReviewSuccess] = useState('');

  useEffect(() => {
    fetchProfileAndBookings();
  }, [user]);

  const fetchProfileAndBookings = async () => {
    if (!user) return;
    try {
      const uRes = await API.get('/users/me');
      setProfile(uRes.data.data);

      const bRes = await API.get('/bookings/my-bookings');
      setBookings(bRes.data.data);

      setLoading(false);
    } catch (err) {
      setErrorMsg('Could not fetch profile metadata.');
      setLoading(false);
    }
  };

  const handlePostReview = async (e) => {
    e.preventDefault();
    setReviewSuccess('');
    try {
      await API.post('/reviews', {
        bookingId: reviewBookingId,
        userId: profile.userId,
        rating,
        comment,
      });
      setReviewSuccess('Review shared successfully!');
      setComment('');
      setReviewBookingId(null);
      fetchProfileAndBookings();
    } catch (err) {
      setErrorMsg('Failed to post review feedback.');
    }
  };

  return (
    <div className="container py-5">
      {loading ? (
        <div className="text-center py-5">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Loading Profile...</span>
          </div>
        </div>
      ) : errorMsg ? (
        <div className="alert alert-danger">{errorMsg}</div>
      ) : (
        <div className="row g-4">
          {/* Profile Sidebar */}
          <div className="col-lg-4">
            <div className="card glass-card p-4 border-0 shadow-sm text-center">
              <div className="fs-1 text-primary mb-3">👤</div>
              <h4 className="fw-bold">{profile?.fullName}</h4>
              <p className="text-muted">{profile?.email}</p>
              <div className="bg-light p-3 rounded-3 mb-4">
                <span className="text-muted font-bold text-uppercase d-block mb-1 small">Loyalty points status</span>
                <span className="fs-3 fw-bold text-success">💎 {profile?.loyaltyPoints || 0} pts</span>
              </div>
              <ul className="list-group list-group-flush text-start rounded-3 border">
                <li className="list-group-item"><strong>Phone:</strong> {profile?.phoneNumber}</li>
                <li className="list-group-item"><strong>Passport/ID:</strong> {profile?.cccdPassport || 'None added'}</li>
                <li className="list-group-item"><strong>Role permissions:</strong> {profile?.roleName}</li>
              </ul>
            </div>
          </div>

          {/* Bookings Timeline */}
          <div className="col-lg-8">
            <div className="card glass-card p-5 border-0 shadow-sm">
              <h3 className="fw-bold mb-4">My Bookings Log</h3>

              {reviewSuccess && <div className="alert alert-success border-0">{reviewSuccess}</div>}

              {bookings.length === 0 ? (
                <div className="alert alert-info">You do not have any room bookings yet.</div>
              ) : (
                <div className="table-responsive">
                  <table className="table align-middle">
                    <thead>
                      <tr>
                        <th>Booking ID</th>
                        <th>Status</th>
                        <th>Rooms RoomNo</th>
                        <th>Total Price</th>
                        <th>Action</th>
                      </tr>
                    </thead>
                    <tbody>
                      {bookings.map((b) => (
                        <tr key={b.bookingId}>
                          <td><strong>#B{b.bookingId}</strong></td>
                          <td>
                            <span className={`badge bg-${b.bookingStatus === 'CHECKED_IN' ? 'primary' : b.bookingStatus === 'CHECKED_OUT' ? 'success' : 'warning'} text-white`}>
                              {b.bookingStatus}
                            </span>
                          </td>
                          <td>
                            {b.bookingDetails.map((det) => (
                              <div key={det.bookingDetailId} className="small">
                                Room {det.room.roomNumber} ({det.startDate} to {det.endDate})
                              </div>
                            ))}
                          </td>
                          <td><strong>${b.totalAmount}</strong></td>
                          <td>
                            {b.bookingStatus === 'CHECKED_OUT' && (
                              <button
                                className="btn btn-outline-primary btn-sm rounded-3 fw-bold"
                                onClick={() => setReviewBookingId(b.bookingId)}
                              >
                                Write review
                              </button>
                            )}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}

              {/* Review Input Section */}
              {reviewBookingId && (
                <div className="card bg-light border p-4 mt-5 rounded-4">
                  <h5 className="fw-bold mb-3">Add Booking Rating</h5>
                  <form onSubmit={handlePostReview}>
                    <div className="mb-3">
                      <label className="form-label fw-bold">Star rating (1-5)</label>
                      <select
                        className="form-select"
                        value={rating}
                        onChange={(e) => setRating(parseInt(e.target.value))}
                      >
                        <option value="5">⭐⭐⭐⭐⭐ 5 Stars</option>
                        <option value="4">⭐⭐⭐⭐ 4 Stars</option>
                        <option value="3">⭐⭐⭐ 3 Stars</option>
                        <option value="2">⭐⭐ 2 Stars</option>
                        <option value="1">⭐ 1 Star</option>
                      </select>
                    </div>
                    <div className="mb-3">
                      <label className="form-label fw-bold">Feedback review text</label>
                      <textarea
                        className="form-control"
                        rows="3"
                        placeholder="Tell us about the hotel amenities, cleaning standards etc."
                        value={comment}
                        onChange={(e) => setComment(e.target.value)}
                        required
                      ></textarea>
                    </div>
                    <div className="gap-2 d-flex justify-content-end">
                      <button
                        type="button"
                        className="btn btn-secondary px-3"
                        onClick={() => setReviewBookingId(null)}
                      >
                        Cancel
                      </button>
                      <button type="submit" className="btn btn-primary px-4 fw-bold">
                        Submit feedback
                      </button>
                    </div>
                  </form>
                </div>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Profile;

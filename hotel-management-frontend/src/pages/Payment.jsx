import React, { useState, useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import API from '../services/api';

const Payment = () => {
  const [searchParams] = useSearchParams();
  const bookingId = searchParams.get('bookingId');
  const navigate = useNavigate();

  const [booking, setBooking] = useState(null);
  const [invoice, setInvoice] = useState(null);
  const [paymentMethod, setPaymentMethod] = useState('BANK_TRANSFER');
  const [transactionId, setTransactionId] = useState('');
  const [errorMsg, setErrorMsg] = useState('');
  const [successMsg, setSuccessMsg] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    if (bookingId) {
      loadBookingDetails();
    }
  }, [bookingId]);

  const loadBookingDetails = async () => {
    try {
      const response = await API.get(`/bookings/${bookingId}`);
      setBooking(response.data.data);
      
      // Auto-trigger invoice generation for the booking
      // Use standard default employee ID of 1 (the Admin/Receptionist default seed)
      try {
        const invRes = await API.post('/invoices', {
          bookingId: parseInt(bookingId),
          employeeId: 1,
          discountAmount: 0,
        });
        setInvoice(invRes.data.data);
      } catch (err) {
        // If invoice is already generated, perform a fallback query
        const allInvs = await API.get('/invoices');
        const existingInv = allInvs.data.data.find(inv => inv.bookingId === parseInt(bookingId));
        if (existingInv) {
          setInvoice(existingInv);
        } else {
          setErrorMsg('Failed to generate hotel stay invoice.');
        }
      }
    } catch (err) {
      setErrorMsg('Cannot load booking verification values.');
    }
  };

  const handlePaymentSubmit = async (e) => {
    e.preventDefault();
    setErrorMsg('');
    setSuccessMsg('');
    setIsSubmitting(true);

    if (!invoice) {
      setErrorMsg('No base invoice found to process.');
      setIsSubmitting(false);
      return;
    }

    try {
      const payload = {
        invoiceId: invoice.invoiceId,
        paymentMethod,
        paymentAmount: invoice.finalAmount,
        transactionId: transactionId || `TXN-${Date.now()}`,
      };

      await API.post('/payments', payload);
      setSuccessMsg('Payment finalized successfully! Redirecting...');
      
      // Auto check-in to make room occupied for testing purposes!
      try {
        if (booking && booking.bookingDetails.length > 0) {
          const detailId = booking.bookingDetails[0].bookingDetailId;
          await API.put(`/bookings/details/${detailId}/checkin`);
        }
      } catch (e) {
        // silently fail if check-in constraints reject it
      }

      setTimeout(() => {
        navigate('/profile');
      }, 1500);
    } catch (err) {
      setErrorMsg(err.response?.data?.message || 'Payment processing failed.');
      setIsSubmitting(false);
    }
  };

  return (
    <div className="container py-5">
      <div className="row justify-content-center">
        <div className="col-lg-6">
          <div className="card glass-card p-5 border-0 shadow">
            <h2 className="fw-bold mb-3">Secure Checkout</h2>
            <p className="text-muted">Review charges and submit payments below.</p>

            {errorMsg && <div className="alert alert-danger border-0 rounded-3">{errorMsg}</div>}
            {successMsg && <div className="alert alert-success border-0 rounded-3">{successMsg}</div>}

            {booking && invoice ? (
              <div>
                <div className="bg-light p-4 rounded-4 mb-4 border">
                  <h6 className="fw-bold mb-3 text-uppercase">Payment Invoice details</h6>
                  <div className="d-flex justify-content-between mb-2">
                    <span className="text-muted">Booking Reference:</span>
                    <strong className="text-dark">#B{bookingId}</strong>
                  </div>
                  <div className="d-flex justify-content-between mb-2">
                    <span className="text-muted">Base Room Rate:</span>
                    <strong className="text-dark">${booking.totalAmount}</strong>
                  </div>
                  <div className="d-flex justify-content-between mb-2">
                    <span className="text-muted">VAT Tax (10%):</span>
                    <strong className="text-dark">${invoice.taxAmount}</strong>
                  </div>
                  <hr />
                  <div className="d-flex justify-content-between mb-2 fs-5">
                    <strong className="text-dark">Final Payable Amount:</strong>
                    <strong className="text-primary">${invoice.finalAmount}</strong>
                  </div>
                </div>

                <form onSubmit={handlePaymentSubmit}>
                  <div className="mb-3">
                    <label className="form-label fw-bold">Select Payment Method</label>
                    <select
                      className="form-select rounded-3"
                      value={paymentMethod}
                      onChange={(e) => setPaymentMethod(e.target.value)}
                    >
                      <option value="BANK_TRANSFER">Bank Transfer (VietQR)</option>
                      <option value="CREDIT_CARD">Credit / Debit Card</option>
                      <option value="CASH">Cash at Desk</option>
                    </select>
                  </div>

                  <div className="mb-4">
                    <label className="form-label fw-bold">Transaction Reference/Notes</label>
                    <input
                      type="text"
                      className="form-control rounded-3"
                      placeholder="Optional Bank reference code"
                      value={transactionId}
                      onChange={(e) => setTransactionId(e.target.value)}
                    />
                  </div>

                  <button
                    type="submit"
                    className="btn btn-primary-gradient w-100 py-3 rounded-3 fw-bold"
                    disabled={isSubmitting}
                  >
                    {isSubmitting ? 'Verifying payment...' : `Authorize Payment of $${invoice.finalAmount}`}
                  </button>
                </form>
              </div>
            ) : (
              <div className="text-center py-4">
                <div className="spinner-border text-primary" role="status">
                  <span className="visually-hidden">Calculating invoice...</span>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Payment;

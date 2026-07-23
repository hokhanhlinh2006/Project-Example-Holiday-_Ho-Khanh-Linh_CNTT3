import React from 'react';
import { Link } from 'react-router-dom';

const Home = () => {
  return (
    <div>
      {/* Hero Banner Section */}
      <header className="header-hero text-center text-md-start">
        <div className="container">
          <div className="row align-items-center">
            <div className="col-lg-6 mb-4 mb-lg-0">
              <h5 className="text-warning fw-bold text-uppercase tracking-wider">Luxury Hotel & Spa</h5>
              <h1 className="display-3 fw-extrabold mb-4 text-white">Experience A Life Luxury In Our Hotel</h1>
              <p className="lead text-white-50 mb-5">
                Every moment spent in our boutique hotel is carefully tailored to present premium levels of comfort, relaxation, and unmatched service.
              </p>
              <div className="d-flex flex-wrap gap-3">
                <Link to="/rooms" className="btn btn-accent-gradient btn-lg px-4 shadow">Explore Rooms</Link>
                <Link to="/booking" className="btn btn-outline-light btn-lg px-4">Instant Booking</Link>
              </div>
            </div>
            <div className="col-lg-6">
              <img
                src="https://images.unsplash.com/photo-1571896349842-33c89424de2d?auto=format&fit=crop&w=800&q=80"
                alt="Luxury Hotel Pool"
                className="img-fluid rounded-4 shadow-lg border border-light border-opacity-10"
              />
            </div>
          </div>
        </div>
      </header>

      {/* Amenities & Services Section */}
      <section className="py-5 bg-white">
        <div className="container py-4">
          <div className="text-center mb-5">
            <h2 className="fw-bold display-5">Premium Facilities & Services</h2>
            <p className="text-muted col-md-6 mx-auto">We provide premium choices for guest refreshments and activity modules to keep you comfortable during your stay.</p>
          </div>
          <div className="row g-4">
            <div className="col-md-4">
              <div className="card glass-card h-100 p-4 text-center border-0">
                <div className="fs-1 text-primary mb-3">💆‍♂️</div>
                <h5 className="fw-bold">Relaxing Spa & Wellness</h5>
                <p className="text-muted mb-0">Rejuvenate your body and soul with top-tier therapeutic massage packages and deep skin treatments.</p>
              </div>
            </div>
            <div className="col-md-4">
              <div className="card glass-card h-100 p-4 text-center border-0">
                <div className="fs-1 text-primary mb-3">🥪</div>
                <h5 className="fw-bold">Gourmet Dining & Minibar</h5>
                <p className="text-muted mb-0">Enjoy locally sourced organic foods, customized cocktails, and robust minibar items stocked daily.</p>
              </div>
            </div>
            <div className="col-md-4">
              <div className="card glass-card h-100 p-4 text-center border-0">
                <div className="fs-1 text-primary mb-3">🏊‍♂️</div>
                <h5 className="fw-bold">Sky Pool & Recreation</h5>
                <p className="text-muted mb-0">Swim under the stars at our high-altitude pool with temperature regulation and a dynamic pool-side bar.</p>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Customer Attraction banner */}
      <section className="py-5 bg-light">
        <div className="container py-4 text-center">
          <div className="glass-card p-5 border-0 text-white rounded-5 shadow" style={{ background: 'linear-gradient(135deg, #1e3c72 0%, #2a5298 100%)' }}>
            <h2 className="fw-extrabold display-5 mb-3 text-white">Save Up To 15% For Your First Stay</h2>
            <p className="lead text-white-50 mb-4">Register an account online today to earn loyalty points and redeem rewards instantly at check-in.</p>
            <Link to="/register" className="btn btn-warning btn-lg px-5 fw-bold text-dark rounded-pill">Sign Up Now & Save</Link>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Home;

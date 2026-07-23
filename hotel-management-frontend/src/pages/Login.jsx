import React, { useState, useContext } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errorMsg, setErrorMsg] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  const { login } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrorMsg('');
    setIsSubmitting(true);

    const result = await login(email, password);
    setIsSubmitting(false);

    if (result.success) {
      navigate('/');
    } else {
      setErrorMsg(result.message);
    }
  };

  return (
    <div className="auth-container">
      <div className="container">
        <div className="row justify-content-center">
          <div className="col-md-5">
            <div className="card glass-card p-5 border-0 shadow">
              <div className="text-center mb-4">
                <h2 className="fw-bold">Sign In</h2>
                <p className="text-muted">Welcome back! Input your credentials below.</p>
              </div>

              {errorMsg && (
                <div className="alert alert-danger border-0 rounded-4" role="alert">
                  ⚠️ {errorMsg}
                </div>
              )}

              <form onSubmit={handleSubmit}>
                <div className="mb-3">
                  <label className="form-label fw-semibold">Email address</label>
                  <input
                    type="email"
                    className="form-control form-control-lg rounded-3"
                    placeholder="name@example.com"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                  />
                </div>
                <div className="mb-4">
                  <label className="form-label fw-semibold">Password</label>
                  <input
                    type="password"
                    className="form-control form-control-lg rounded-3"
                    placeholder="••••••••"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                  />
                </div>
                <button
                  type="submit"
                  className="btn btn-primary-gradient w-100 py-3 rounded-3"
                  disabled={isSubmitting}
                >
                  {isSubmitting ? 'Verifying...' : 'Sign In'}
                </button>
              </form>

              <div className="text-center mt-4">
                <span className="text-muted">Don't have an account? </span>
                <Link to="/register" className="fw-semibold text-primary decoration-none">Sign Up</Link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;

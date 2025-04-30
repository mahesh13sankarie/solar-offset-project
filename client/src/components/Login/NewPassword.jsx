import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from "/src/components/LandingPage/Navbar.jsx";
import Footer from "/src/components/LandingPage/Footer.jsx";

const NewPassword = () => {
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    if (password !== confirmPassword) {
      alert('Passwords do not match!');
      return;
    }
    // Send the new password to your backend here
    console.log('New password submitted:', password);

    // After successful password reset:
    alert('Password reset successful! Please login.');
    navigate('/login');
  };

  return (
  <>
    <Navbar />
    <div className="container d-flex justify-content-center align-items-center vh-100">
      <div className="card p-4 shadow-lg" style={{ maxWidth: '500px', width: '100%' }}>
        <h3 className="text-center mb-4">Set New Password</h3>
        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label className="form-label">New Password</label>
            <input
              type="password"
              className="form-control"
              placeholder="Enter new password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          <div className="mb-3">
            <label className="form-label">Confirm New Password</label>
            <input
              type="password"
              className="form-control"
              placeholder="Confirm new password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
            />
          </div>
          <button type="submit" className="btn btn-primary w-100">Reset Password</button>
        </form>
      </div>
    </div>
    <Footer />
  </>
  );
};

export default NewPassword;

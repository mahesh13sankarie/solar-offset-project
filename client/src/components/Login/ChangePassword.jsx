import React from 'react';
import Navbar from "/src/components/LandingPage/Navbar.jsx";
import Footer from "/src/components/LandingPage/Footer.jsx";

const ChangePassword = () => {
  return (
  <>
    <Navbar />
    <div className="container d-flex justify-content-center align-items-center vh-100">
      <div className="card p-4 shadow-lg" style={{ maxWidth: '500px', width: '100%' }}>
        <h3 className="text-center mb-4">Reset Your Password</h3>
        <form>
          <div className="mb-3">
            <label className="form-label">Email</label>
            <input type="email" className="form-control" placeholder="Enter your registered email" required />
          </div>
          <button type="submit" className="btn btn-primary w-100">Send Reset Link</button>
        </form>
      </div>
    </div>
    <Footer />
  </>
  );
};

export default ChangePassword;
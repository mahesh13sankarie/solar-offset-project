import React from "react";
import { Link } from "react-router-dom";
import "../../css/services.css"; // Keep if you have custom styles
import Navbar from "./Navbar.jsx";

const Services = () => {
  return (
    <>
      <Navbar />

      <section id="services" className="services section py-5 bg-light">
        <div className="container section-title" data-aos="fade-up">
          {/* Section Title */}
          <div className="text-center mb-5">
            <h2 className="fw-bold mb-2">How to Donate</h2>
            <p className="text-muted small">
              Follow these simple steps to contribute to solar panel projects around the world.
            </p>
          </div>

          {/* Services Row */}
          <div className="row gy-4">
            {/* Choose Country */}
            <div className="col-xl-3 col-md-6" data-aos="fade-up" data-aos-delay="100">
              <div className="service-item text-center p-4 bg-white shadow-sm rounded h-100">
                <div className="icon mb-3">
                  <i className="bi bi-globe-asia-australia fs-1 text-primary"></i>
                </div>
                <h5 className="fw-bold">
                  <Link to="/countries" className="text-decoration-none text-dark">Choose a Country</Link>
                </h5>
                <p className="small text-muted mt-2">
                  Select your preferred country to support. Compare different opportunities before donating.
                </p>
              </div>
            </div>

            {/* Create Account */}
            <div className="col-xl-3 col-md-6" data-aos="fade-up" data-aos-delay="200">
              <div className="service-item text-center p-4 bg-white shadow-sm rounded h-100">
                <div className="icon mb-3">
                  <i className="bi bi-person-square fs-1 text-success"></i>
                </div>
                <h5 className="fw-bold">
                  <Link to="/login" className="text-decoration-none text-dark">Create an Account</Link>
                </h5>
                <p className="small text-muted mt-2">
                  Quickly set up your profile before making a contribution.
                </p>
              </div>
            </div>

            {/* Make Donation */}
            <div className="col-xl-3 col-md-6" data-aos="fade-up" data-aos-delay="300">
              <div className="service-item text-center p-4 bg-white shadow-sm rounded h-100">
                <div className="icon mb-3">
                  <i className="bi bi-currency-exchange fs-1 text-warning"></i>
                </div>
                <h5 className="fw-bold">
                  <Link to="/donate" className="text-decoration-none text-dark">Make a Donation</Link>
                </h5>
                <p className="small text-muted mt-2">
                  Support clean energy by completing your donation securely.
                </p>
              </div>
            </div>

            {/* See Impact */}
            <div className="col-xl-3 col-md-6" data-aos="fade-up" data-aos-delay="400">
              <div className="service-item text-center p-4 bg-white shadow-sm rounded h-100">
                <div className="icon mb-3">
                  <i className="bi bi-brightness-alt-high-fill fs-1 text-info"></i>
                </div>
                <h5 className="fw-bold">
                  <Link to="/impact" className="text-decoration-none text-dark">See Your Impact</Link>
                </h5>
                <p className="small text-muted mt-2">
                  Track how much carbon you’ve helped offset.
                </p>
              </div>
            </div>
          </div>

          {/* Donate Now Button */}
          <div className="text-center mt-5">
            <Link to="/solarComparison" className="btn btn-primary px-5 py-2">
              Donate Now
            </Link>
          </div>
        </div>
      </section>
    </>
  );
};

export default Services;

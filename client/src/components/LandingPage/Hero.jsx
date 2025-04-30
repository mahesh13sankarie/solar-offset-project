import React from "react";
import { Link } from "react-router-dom";

const Hero = () => {
  return (
    <section id="hero" className="hero section text-white d-flex align-items-center" style={{ backgroundColor: "#1e2a38", padding: "60px 0" }}>
      <div className="container" data-aos="zoom-out">
        <div className="row justify-content-center text-center">
          <div className="col-lg-8">
            <h1 className="fw-bold mb-3" style={{ fontSize: "2.5rem" }}>
              Power the Planet, Offset Carbon
            </h1>
            <p className="lead mb-4 text-light">
              Your donation funds solar panels to reduce global carbon emissions.
            </p>
            <div className="d-flex justify-content-center">
              <Link to="/donate" className="btn btn-primary px-4 py-2">
                Get Started
              </Link>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default Hero;

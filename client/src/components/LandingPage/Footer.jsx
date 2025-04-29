import React from "react";
import { Link } from "react-router-dom";

const Footer = () => {
  return (
    <footer className="footer py-4" style={{ backgroundColor: "#f1f3f6" }}>
      <div className="container">
        <div className="row gy-4 justify-content-between align-items-start">

          {/* Left: Newsletter */}
          <div className="col-lg-5 text-center text-lg-start">
            <h6 className="fw-bold mb-2">Subscribe to Our Newsletter</h6>
            <p className="small text-muted mb-3">Stay updated on our latest projects and impact</p>
            <form className="d-flex flex-column flex-sm-row gap-2">
              <input
                type="email"
                name="email"
                placeholder="Enter your email"
                className="form-control"
              />
              <input
                type="submit"
                value="Subscribe"
                className="btn btn-primary"
              />
            </form>
          </div>

          {/* Right: Company Info */}
          <div className="col-lg-6 text-center text-lg-end">
            <Link to="/" className="h5 text-primary d-inline-block mb-2">
              Solar Offset
            </Link>
            <p className="small text-muted mb-2">
              Empowering communities through sustainable solar energy solutions.
            </p>
            <div className="social-links d-flex gap-3 justify-content-center justify-content-lg-end mb-3">
              <a href="#"><i className="bi bi-twitter-x fs-5"></i></a>
              <a href="#"><i className="bi bi-facebook fs-5"></i></a>
              <a href="#"><i className="bi bi-instagram fs-5"></i></a>
              <a href="#"><i className="bi bi-linkedin fs-5"></i></a>
            </div>
            <p className="small text-muted mb-0">
              <strong>Phone:</strong> <a href="tel:01142221800" className="text-dark text-decoration-none">0114 222 1800</a> |
              <strong> Email:</strong> <a href="mailto:info@solaroffset.com" className="text-dark text-decoration-none">info@solaroffset.com</a>
            </p>
          </div>

        </div>

        {/* Divider */}
        <hr style={{ borderTop: "1px solid #d3d3d3", opacity: 0.7 }} />

        {/* Bottom Copyright */}
        <div className="row">
          <div className="col-12 text-center">
            <small className="text-muted">
              &copy; {new Date().getFullYear()} <strong>Solar Offset</strong>. All Rights Reserved.
            </small>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;

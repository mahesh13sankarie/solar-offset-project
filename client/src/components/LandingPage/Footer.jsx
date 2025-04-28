import React from "react";
import { Link } from "react-router-dom";

const Footer = () => {
    return (
        <footer id="footer" className="footer section light-background">
            <div className="footer-newsletter">
                <div className="container">
                    <div className="row justify-content-center">
                        <div className="col-lg-12 text-center">
                            <h4>Our Newsletter</h4>
                            <p>Subscribe to stay updated on our latest projects and impact</p>
                            <form>
                                <input type="email" name="email" placeholder="Enter your email" />
                                <input type="submit" value="Subscribe" />
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <div className="footer-content">
                <div className="container">
                    <div className="row gy-4">
                        <div className="col-lg-5 col-md-12 footer-info">
                            <Link to="/" className="logo d-flex align-items-center">
                                <span>Solar Offset</span>
                            </Link>
                            <p>Empowering global communities through sustainable solar energy solutions. Join us in creating a brighter, cleaner future for all.</p>
                            <div className="social-links d-flex mt-3">
                                <a href="#" className="twitter"><i className="bi bi-twitter-x"></i></a>
                                <a href="#" className="facebook"><i className="bi bi-facebook"></i></a>
                                <a href="#" className="instagram"><i className="bi bi-instagram"></i></a>
                                <a href="#" className="linkedin"><i className="bi bi-linkedin"></i></a>
                            </div>
                        </div>
                        <div className="col-lg-6 d-flex justify-content-end">
                          <div className="footer-contact text-end">
                            <h4 className="mb-1">Contact Us</h4>
                            <address className="mb-0">
                              Diamond Building<br />
                              32 Leavygreave Road<br />
                              Sheffield S3 7RD<br />
                              United Kingdom<br /><br />
                              <strong>Phone:</strong> 0114 222 1800<br />
                              <strong>Email:</strong> info@solaroffset.com
                            </address>
                          </div>
                      </div>

                    </div>
                </div>
            </div>

            <div className="footer-legal">
                <div className="container">
                    <div className="copyright">
                        &copy; {new Date().getFullYear()} <strong><span>Solar Offset</span></strong>. All Rights Reserved
                    </div>
                </div>
            </div>
        </footer>
    );
};

export default Footer;
import React from "react";
import { Link } from "react-router-dom";
import "../../css/services.css"


const Services = () => {
    return (
        <section id="services" className="services section light-background">
            <div className="container section-title" data-aos="fade-up">
                <h2>How to Donate</h2>
                <p>These are key steps you should follow to donate solar panels to countries you need.</p>
            </div>

            <div className="container">
                <div className="row gy-4">
                    <div className="col-xl-3 col-md-6 d-flex" data-aos="fade-up" data-aos-delay="100">
                        <div className="service-item position-relative">
                            <div className="icon">
                                <i className="bi bi-person-square icon"></i>
                            </div>
                            <h4><Link to="/login" className="stretched-link">Create an Account</Link></h4>
                            <p>Before making a donation, create your account first</p>
                        </div>
                    </div>

                    <div className="col-xl-3 col-md-6 d-flex" data-aos="fade-up" data-aos-delay="200">
                        <div className="service-item position-relative">
                            <div className="icon">
                                <i className="bi bi-globe-asia-australia icon"></i>
                            </div>
                            <h4><Link to="/countries" className="stretched-link">Choose a Country</Link></h4>
                            <p>Choose your preferred country to donate. You can also compare options before making your decision.</p>
                        </div>
                    </div>

                    <div className="col-xl-3 col-md-6 d-flex" data-aos="fade-up" data-aos-delay="300">
                        <div className="service-item position-relative">
                            <div className="icon">
                                <i className="bi bi-currency-exchange icon"></i>
                            </div>
                            <h4><Link to="/donate" className="stretched-link">Make a Donation</Link></h4>
                            <p>Complete your payment and make a meaningful contribution to clean energy solutions.</p>
                        </div>
                    </div>

                    <div className="col-xl-3 col-md-6 d-flex" data-aos="fade-up" data-aos-delay="400">
                        <div className="service-item position-relative">
                            <div className="icon">
                                <i className="bi bi-brightness-alt-high-fill icon"></i>
                            </div>
                            <h4><Link to="/impact" className="stretched-link">See Your Impact</Link></h4>
                            <p>Track how much carbon you have helped offset</p>
                        </div>
                    </div>
                </div>

                <br />
                <div className="container section-title">
                    <Link to="/donate" className="btn-get-started">Donate Now</Link>
                </div>
            </div>
        </section>
    );
};

export default Services;
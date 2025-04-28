import React from "react";
import { Link } from "react-router-dom";

const Hero = () => {
    return (
        <section id="hero" className="hero section dark-background">
            <div className="container">
                <div className="row gy-4">
                    <div className="col-lg-6 order-2 order-lg-1 d-flex flex-column justify-content-center" data-aos="zoom-out">
                        <h1>Power the Planet, Offset Carbon</h1>
                        <p>Your donation funds solar panels to reduce carbon emissions</p>
                        <div className="d-flex">
                            <a href="#donate"  className="btn-get-started">Get Started</a>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    );
};

export default Hero;
import React from "react";

const Contact = () => {
    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission here
    };

    return (
        <section id="contact" className="contact section light-background">
            <div className="container section-title" data-aos="fade-up">
                <h2>Contact</h2>
            </div>

            <div className="container">
                <div className="row gy-4">
                    <div className="col-lg-6" data-aos="fade-up" data-aos-delay="100">
                        <div className="contact-info">
                            <div className="info-item d-flex align-items-center">
                                <i className="bi bi-envelope flex-shrink-0"></i>
                                <div>
                                    <h4>Email:</h4>
                                    <p>info@solaroffset.com</p>
                                </div>
                            </div>

                            <div className="info-item d-flex align-items-center">
                                <i className="bi bi-phone flex-shrink-0"></i>
                                <div>
                                    <h4>Call:</h4>
                                    <p>0114 222 1800</p>
                                </div>
                            </div>

                            <div className="info-item d-flex align-items-center">
                                <i className="bi bi-clock flex-shrink-0"></i>
                                <div>
                                    <h4>Open Hours:</h4>
                                    <p>Mon-Fri: 9:00AM - 5:00PM</p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div className="col-lg-6" data-aos="fade-up" data-aos-delay="200">
                        <form className="contact-form" onSubmit={handleSubmit}>
                            <div className="row gy-4">
                                <div className="col-md-6">
                                    <input type="text" className="form-control" placeholder="Your Name" required />
                                </div>

                                <div className="col-md-6">
                                    <input type="email" className="form-control" placeholder="Your Email" required />
                                </div>

                                <div className="col-md-12">
                                    <input type="text" className="form-control" placeholder="Subject" required />
                                </div>

                                <div className="col-md-12">
                                    <textarea className="form-control" rows="6" placeholder="Message" required></textarea>
                                </div>

                                <div className="col-md-12 text-center">
                                    <button type="submit" className="btn-get-started">Send Message</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    );
};

export default Contact;
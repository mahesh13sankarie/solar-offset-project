import React from "react";

const Contact = () => {
  const handleSubmit = (e) => {
    e.preventDefault();
    // Handle form submission
  };

  return (
    <section id="contact" className="contact section py-5 bg-light">
      <div className="container section-title" data-aos="fade-up">
        {/* Section title */}
        <div className="text-center mb-5">
          <h2 className="fw-bold mb-2">Contact Us</h2>
          <p className="text-muted small">We’d love to hear from you!</p>
        </div>

        {/* Contact content */}
        <div className="row gy-4">
          {/* Left - Contact Info */}
          <div className="col-lg-5 d-flex flex-column gap-3" data-aos="fade-up" data-aos-delay="100">
            {/* Info Card */}
            <div className="d-flex align-items-center bg-white shadow-sm p-3 rounded">
                          <i className="bi bi-envelope flex-shrink-0 fs-4 text-primary me-3"></i>
                          <div>
                            <h6 className="fw-bold mb-1">Email:</h6>
                            <p className="small mb-0">info@solaroffset.com</p>
                          </div>
            </div>

            <div className="d-flex align-items-center bg-white shadow-sm p-3 rounded">
                          <i className="bi bi-phone flex-shrink-0 fs-4 text-success me-3"></i>
                          <div>
                            <h6 className="fw-bold mb-1">Call:</h6>
                            <p className="small mb-0">0114 222 1800</p>
                          </div>
            </div>

            <div className="d-flex align-items-center bg-white shadow-sm p-3 rounded">
                          <i className="bi bi-clock flex-shrink-0 fs-4 text-warning me-3"></i>
                          <div>
                            <h6 className="fw-bold mb-1">Open Hours:</h6>
                            <p className="small mb-0">Mon-Fri: 9:00AM - 5:00PM</p>
                          </div>
            </div>
          </div>

          {/* Right - Contact Form */}
          <div className="col-lg-7" data-aos="fade-up" data-aos-delay="200">
            <div className="p-4 bg-white shadow rounded">
              <form onSubmit={handleSubmit}>
                <div className="row gy-3">
                  <div className="col-md-6">
                    <input type="text" className="form-control" placeholder="Your Name" required />
                  </div>
                  <div className="col-md-6">
                    <input type="email" className="form-control" placeholder="Your Email" required />
                  </div>
                  <div className="col-12">
                    <input type="text" className="form-control" placeholder="Subject" required />
                  </div>
                  <div className="col-12">
                    <textarea className="form-control" rows="5" placeholder="Message" required></textarea>
                  </div>
                  <div className="col-12 text-center">
                    <button type="submit" className="btn btn-primary px-4 py-2 mt-2">
                      Send Message
                    </button>
                  </div>
                </div>
              </form>
            </div>
          </div>

        </div>
      </div>
    </section>
  );
};

export default Contact;

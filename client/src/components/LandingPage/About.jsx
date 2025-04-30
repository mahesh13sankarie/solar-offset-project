import React from "react";

const About = () => {
  return (
    <section id="about" className="about section py-5 bg-light">
      <div className="container section-title" data-aos="fade-up">
        <div className="text-center mb-5">
          <h2 className="fw-bold">About Us</h2>
          <p className="text-muted small">
            Building a sustainable future through solar energy
          </p>
        </div>

        <div className="row gy-4">
          {/* Left Column */}
          <div className="col-lg-6 d-flex flex-column gap-4" data-aos="fade-up" data-aos-delay="100">
            {/* Mission */}
            <div className="p-3 bg-white shadow-sm rounded">
              <h5 className="fw-bold"><i className="bi bi-bullseye text-primary me-2"></i>Our Mission</h5>
              <p className="small text-muted mt-2">
                At Solar Offset, we believe everyone should have the opportunity to contribute to a more sustainable future.
                Our mission is to enable households to offset their carbon footprint by funding solar energy projects
                where solar power can create the greatest impact.
              </p>
            </div>

            {/* What We Do */}
            <div className="p-3 bg-white shadow-sm rounded">
              <h5 className="fw-bold"><i className="bi bi-gear-wide-connected text-success me-2"></i>What We Do</h5>
              <p className="small text-muted mt-2">
                We connect households with solar projects in regions where clean electricity is limited.
                Supporters help reduce global carbon emissions while empowering local communities with sustainable energy.
              </p>
            </div>
          </div>

          {/* Right Column */}
          <div className="col-lg-6 d-flex flex-column gap-4" data-aos="fade-up" data-aos-delay="200">
            {/* Why It Matters */}
            <div className="p-3 bg-white shadow-sm rounded">
              <h5 className="fw-bold"><i className="bi bi-lightning-charge-fill text-warning me-2"></i>Why It Matters</h5>
              <p className="small text-muted mt-2">
                Not every household can install solar panels due to space or property limitations.
                Solar Offset offers a solution — allowing anyone to make a tangible impact by funding solar energy where it's needed most.
              </p>
            </div>

            {/* Our Promise */}
            <div className="p-3 bg-white shadow-sm rounded">
              <h5 className="fw-bold"><i className="bi bi-shield-check text-info me-2"></i>Our Promise</h5>
              <p className="small text-muted mt-2">
                Transparency and impact are at the heart of everything we do.
                Every donation directly supports solar panel installations, with clear reporting on carbon savings and benefits to communities worldwide.
              </p>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default About;

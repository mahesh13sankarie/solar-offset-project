import React from "react";

const About = () => {
    return (
        <section id="about" className="about section">
            <div className="container section-title" data-aos="fade-up">
                <h2>About Us</h2>
            </div>

            <div className="container">
                <div className="row gy-4">
                    <div className="col-lg-6 content" data-aos="fade-up" data-aos-delay="100">
                        <ul>
                            <li>
                                <i className="bi"></i>
                                <span>
                                    <h6>Our Mission</h6>
                                    At Solar Offset, we believe everyone should have the opportunity
                                    to contribute to a more sustainable future. Our mission is to enable households to offset their carbon footprint by funding
                                    solar energy projects in regions where solar power has the greatest potential impact.
                                </span>
                            </li>
                            <br />
                            <li>
                                <i className="bi"></i>
                                <span>
                                    <h6>What We Do</h6>
                                    We connect households with solar energy projects in countries where access to clean
                                    electricity is limited. By supporting these projects, donors help reduce global carbon emissions while empowering local
                                    communities with sustainable energy.
                                </span>
                            </li>
                        </ul>
                    </div>

                    <div className="col-lg-6" data-aos="fade-up" data-aos-delay="200">
                        <ul>
                            <li>
                                <i className="bi"></i>
                                <span>
                                    <h6>Why It Matters</h6>
                                    Not every household has the option to install solar panels. Whether due to space constraints or property
                                    limitations, many are unable to generate their own renewable energy. Solar Offset offers a solution — allowing anyone to make a tangible
                                    impact by funding solar energy where it's needed most.
                                </span>
                            </li>
                            <br />
                            <li>
                                <i className="bi"></i>
                                <span>
                                    <h6>Our Promise</h6>
                                    Transparency and impact are at the heart of everything we do. Every donation directly supports solar panel installations,
                                    with clear reporting on the carbon savings and benefits provided to communities around the world.
                                </span>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </section>
    );
};

export default About;
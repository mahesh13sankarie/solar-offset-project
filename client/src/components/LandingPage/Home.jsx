import React from 'react';
import Navbar from "./Navbar"
import Hero from "./Hero"
import About from "./About"
import Services from "./Services"
import Contact from "./Contact"
import Footer from "./Footer"


const Home = () => {
    return (
        <div>
            <Navbar />
                <section id="hero">
                    <Hero />
                </section>
                <section id="about">
                    <About />
                </section>
                <section id="donate">
                    <Services />
                </section>
                <section id="contact">
                    <Contact />
                </section>
                <Footer />
        </div>
    );
};

export default Home;
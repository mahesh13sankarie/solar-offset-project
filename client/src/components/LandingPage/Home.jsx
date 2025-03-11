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
                    <Hero />
                    <About />
                    <Services />
                    <Contact />
            
                <Footer />
     
        </div>
    );
};

export default Home;
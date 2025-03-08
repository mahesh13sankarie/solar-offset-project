import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';

// Styles
import './css/main.css';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import '../node_modules/bootstrap-icons/font/bootstrap-icons.css';

// Components
import Navbar from './components/Navbar';
import Hero from './components/Hero';
import About from './components/About';
import Services from './components/Services';
import Contact from './components/Contact';
import Footer from './components/Footer';

const App = () => {
    return (
        <Router>
            <div className="index-page">
                <Navbar />
                <main className="main">
                    <Hero />
                    <About />
                    <Services />
                    <Contact />
                </main>
                <Footer />
            </div>
        </Router>
    );
};

export default App;
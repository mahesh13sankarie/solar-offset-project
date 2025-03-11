import React from "react";


const Navbar = () => {
    return (
        <header id="header" className="header d-flex align-items-center fixed-top"
        style={{
            background:"#37517E",
            color:"white",
        }}
        >
            <div className="container-fluid container-xl position-relative d-flex align-items-center">
                {/* Logo */}
                <a href="/client/public" className="logo d-flex align-items-center me-auto" style={{textDecoration:"none",}}>
                    <h1 className="sitename">Solar Offset</h1>
                </a>

                {/* Navigation Menu */}
                <nav id="navmenu" className="navmenu">
                    <ul>
                        <li><a href="/client/public" className="active" style={{textDecoration:"none",}}>Home</a></li>
                        <li><a href="/Users/shreyasdesai/Documents/Uni/Team Software Project/project/client/src/components/LandingPage/About" style={{textDecoration:"none",}} >About</a></li>
                        <li><a href="/donate" style={{textDecoration:"none",}}>Donate</a></li>
                        <li><a href="/Users/shreyasdesai/Documents/Uni/Team Software Project/project/client/src/components/LandingPage/Contact" style={{textDecoration:"none",}}>Contact</a></li>
                    </ul>
                    <i className="mobile-nav-toggle d-xl-none bi bi-list"></i>
                </nav>

                {/* Login/Register Button */}
                <a href="/login" className="btn-getstarted" style={{textDecoration:"none",}} >Login/Register</a>

                {/* Profile Section */}
                {/* <div className="profile" id="profile">
                    <img id="profileImg" src="" alt="User Profile" />
                </div> */}
            </div>
        </header>
    );
};

export default Navbar;
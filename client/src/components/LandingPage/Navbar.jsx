import React, { useState, useEffect } from "react";
import { logout } from "../HelperComponents/HelperFunction.jsx";

const Navbar = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [isStaff, setIsStaff] = useState(false); // Assuming you have a way to determine if the user is staff

    // Check if there's a token in localStorage to determine if the user is logged in
    useEffect(() => {
        const token = localStorage.getItem("token");
        if (token) {
            setIsLoggedIn(true);
        }
        const userRole = localStorage.getItem("accountType"); // This is just an example
        if (userRole == 2) {
            setIsStaff(true);
        }
    }, [isStaff]);

    const handleLogout = () => {
        // Remove token from localStorage
        localStorage.removeItem("token");
        localStorage.removeItem("accountType");
        localStorage.removeItem("userId");
        localStorage.removeItem("fullName");

        setIsLoggedIn(false); // Update state to reflect that user is logged out
        console.log("Logged out successfully");
    };

    return (
        <header
            id="header"
            className="header d-flex align-items-center fixed-top"
            style={{
                background: "#37517E",
                color: "white",
            }}
        >
            <div className="container-fluid container-xl position-relative d-flex align-items-center">
                {/* Logo */}
                <a
                    href="/"
                    className="logo d-flex align-items-center me-auto"
                    style={{ textDecoration: "none" }}
                >
                    <h1 className="sitename">Solar Offset</h1>
                </a>

                {/* Navigation Menu */}
                <nav id="navmenu" className="navmenu">
                    <ul>
                        <li>
                            <a href="/" className="active" style={{ textDecoration: "none" }}>
                                Home
                            </a>
                        </li>
                        <li>
                            <a href="/#donate" style={{ textDecoration: "none" }}>
                                Donate
                            </a>
                        </li>

                        {isLoggedIn ? (
                            <li>
                                <li>
                                    <a
                                        href="/transaction-history"
                                        style={{ textDecoration: "none" }}
                                    >
                                        Transaction History
                                    </a>
                                </li>
                            </li>
                        ) : (
                            <li>
                                <a href="/#about" style={{ textDecoration: "none" }}>
                                    About
                                </a>
                            </li>
                        )}
                        {isStaff ? (
                            <li>
                                <a href="/staff/dashboard" style={{ textDecoration: "none" }}>
                                    Staff Dashboard
                                </a>
                            </li>
                        ) : (
                            <li>
                                <a href="/#contact" style={{ textDecoration: "none" }}>
                                    Contact
                                </a>
                            </li>
                        )}
                    </ul>
                    <i className="mobile-nav-toggle d-xl-none bi bi-list"></i>
                </nav>

                {/* Login/Register Button */}
                <div>
                    {isLoggedIn ? (
                        <a
                            href="/"
                            className="btn-getstarted"
                            style={{ textDecoration: "none" }}
                            onClick={handleLogout}
                        >
                            Logout
                        </a>
                    ) : (
                        <a
                            href="/login"
                            className="btn-getstarted"
                            style={{ textDecoration: "none" }}
                        >
                            Login/Register
                        </a>
                    )}
                </div>
            </div>
        </header>
    );
};

export default Navbar;

import React from "react";

import './SideNav.css'; // Optional: for custom styling
import {Link, useLocation} from 'react-router-dom';

const SideNav = (props) => {

    const location = useLocation();
    const currentPath = location.pathname;
return (
    <div className="d-flex flex-column flex-shrink-0 p-3 text-white bg-dark text-center" style={{ width: '250px', height: '100vh' }}>
        <i className="bi bi-person-circle" style={{ fontSize: "2rem" }}></i>
        <p className="m-2">Admin</p>
        <hr />
        <ul className="nav nav-pills flex-column mb-auto">
            <li>
                <Link
                    to="/dashboard"
                    className={`nav-link text-white ${currentPath === '/dashboard' ? 'active bg-primary' : ''}`}
                >
                    Users
                </Link>
            </li>
            <li>
                <Link
                    to="/logout"
                    className={`nav-link text-white ${currentPath === '/logout' ? 'active bg-primary' : ''}`}
                    onClick={() => {
                        localStorage.removeItem('authToken'); // Remove JWT token
                        localStorage.removeItem("token");
                        localStorage.removeItem("userId");

                        window.location.href = '/admin'; // Force redirect to login
                    }}
                >
                    Logout
                </Link>
            </li>
        </ul>
        <hr />
    </div>
);

};
export default SideNav;
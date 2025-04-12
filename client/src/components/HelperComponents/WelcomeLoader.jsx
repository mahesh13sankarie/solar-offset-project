import React from "react";
import "bootstrap-icons/font/bootstrap-icons.css";
import "bootstrap/dist/css/bootstrap.min.css";

export default function WelcomeLoader() {
    return (
        <div className="d-flex justify-content-center align-items-center vh-100 bg-white text-dark-emphasis flex-column">
            <div className="position-relative mb-4">
                <div className="rounded-circle bg-warning" style={{ width: "100px", height: "100px", animation: "pulse 2s infinite" }}></div>
                <div className="rounded-circle bg-warning position-absolute top-50 start-50 translate-middle" style={{ width: "80px", height: "80px", opacity: 0.8 }}></div>
            </div>

            <h2 className="mb-3">Solar Offset</h2>

            <div className="d-flex align-items-center  text-dark">
                <i className="bi me-2 fs-3 spinner-border text-dark"></i>
                <span className="fs-5">Loading</span>
            </div>
        </div>
    );
}
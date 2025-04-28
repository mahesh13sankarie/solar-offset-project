import React, { useEffect, useState } from "react";
import { Link, useParams, useNavigate } from "react-router-dom";
import Navbar from "./Navbar.jsx";
import axios from "axios";
import { useAuth } from "../../context/AuthContext";

const InstallationCost = () => {
    const [country, setCountry] = useState(null);
    const [error, setError] = useState(null);
    const [showLoginModal, setShowLoginModal] = useState(false);
    const { countryCode } = useParams();
    const navigate = useNavigate();
    const { isAuthenticated } = useAuth();

    useEffect(() => {
        const fetchData = async () => {
            const token = localStorage.getItem("token");
            console.log("Fetched token:", token);

            if (!token) {
                console.error("No token found, please login first");
                setError("Authentication required. Please login.");
                return;
            }

            try {
                const response = await axios.get(
                    `http://localhost:8000/api/v1/countries/${countryCode}`,
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                            "Content-Type": "application/json",
                        },
                    }
                );
                console.log(response.data);
                setCountry(response.data);
            } catch (error) {
                setError(error.response?.data?.message || "Failed to fetch data");
                console.error("Fetch country error:", error);
            }
        };

        fetchData();
    }, [countryCode]);

    // Handle donate button click
    const handleDonateClick = (panelId) => {
        if (isAuthenticated) {
            // If logged in, proceed to payment page
            navigate(`/Payment/${countryCode}/${panelId}`);
        } else {
            // If not logged in, show login modal
            setShowLoginModal(true);

            // After 2 seconds, close modal and redirect to home
            setTimeout(() => {
                setShowLoginModal(false);
                navigate("/");
            }, 2000);
        }
    };

    return (
        <div>
            <Navbar />

            {/* Login Modal */}
            {showLoginModal && (
                <div
                    className="modal d-block"
                    tabIndex="-1"
                    role="dialog"
                    style={{ backgroundColor: "rgba(0,0,0,0.5)" }}
                >
                    <div className="modal-dialog modal-dialog-centered" role="document">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">Login Required</h5>
                                <button
                                    type="button"
                                    className="btn-close"
                                    onClick={() => setShowLoginModal(false)}
                                    aria-label="Close"
                                ></button>
                            </div>
                            <div className="modal-body">
                                <p>You must be logged in to donate. Redirecting to home page...</p>
                            </div>
                        </div>
                    </div>
                </div>
            )}

            <section className="container mt-4">
                {/* Back Button */}
                <Link to="/SolarComparison">
                    <button className="btn btn-link mb-3">
                        <i className="bi bi-arrow-left-circle"></i> Back
                    </button>
                </Link>

                {/* Error Message */}
                {error && <div className="alert alert-danger">{error}</div>}

                {/* Country Data */}
                {country && (
                    <>
                        {/* Country Overview */}
                        <div className="card shadow-sm p-4 mb-5">
                            <h2 className="mb-3">{country.country}</h2>
                            <p className="text-muted">{country.description}</p>
                            <div className="row mt-3">
                                <div className="col-md-4">
                                    <strong>Carbon Emissions:</strong>{" "}
                                    {country.carbonEmissions.toLocaleString()} tCO₂
                                </div>
                                <div className="col-md-4">
                                    <strong>Electricity Availability:</strong>{" "}
                                    {country.electricityAvailability.toLocaleString()} MWh
                                </div>
                                <div className="col-md-4">
                                    <strong>Solar Potential:</strong> {country.solarPowerPotential}%
                                </div>
                                <div className="col-md-4 mt-3">
                                    <strong>Renewable Energy:</strong> {country.renewablePercentage}
                                    %
                                </div>
                                <div className="col-md-4 mt-3">
                                    <strong>Population:</strong> {country.population} million
                                </div>
                            </div>
                        </div>

                        {/* Panel Cards */}
                        <h4 className="mb-4">Available Solar Panels</h4>
                        <div className="row">
                            {country.solarPanels?.map((panel, i) => (
                                <div className="col-md-6 col-lg-4 mb-4" key={i}>
                                    <div className="card h-100 shadow-sm d-flex flex-column">
                                        <div className="card-body d-flex flex-column">
                                            <h5 className="card-title">{panel.panelName}</h5>
                                            <p
                                                className="text-muted"
                                                style={{ fontSize: "0.9rem" }}
                                            >
                                                {panel.description}
                                            </p>
                                            <ul className="list-group list-group-flush mt-3 mb-4">
                                                <li className="list-group-item">
                                                    <strong>Installation:</strong> £
                                                    {panel.installationCost ?? "-"}
                                                </li>
                                                <li className="list-group-item">
                                                    <strong>Production:</strong>{" "}
                                                    {panel.productionPerPanel ?? "-"} W
                                                </li>
                                                <li className="list-group-item">
                                                    <strong>Efficiency:</strong>{" "}
                                                    {panel.efficiency ?? "-"}%
                                                </li>
                                                <li className="list-group-item">
                                                    <strong>Warranty:</strong>{" "}
                                                    {panel.warranty ?? "-"} years
                                                </li>
                                            </ul>
                                            <div className="mt-auto">
                                                {/* Changed from Link to button with onClick handler */}
                                                <button
                                                    onClick={() => handleDonateClick(panel.id || i)}
                                                    className="btn btn-success w-100"
                                                >
                                                    <i className="bi bi-heart-fill"></i> Donate
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </>
                )}
            </section>
        </div>
    );
};

export default InstallationCost;

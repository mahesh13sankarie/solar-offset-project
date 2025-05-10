import React, { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { api } from "../../api";
import { useAuth } from "../../context/AuthContext";
import Navbar from "./Navbar.jsx";

const InstallationCost = () => {
    const [country, setCountry] = useState(null);
    const [error, setError] = useState(null);
    const [showLoginModal, setShowLoginModal] = useState(false);
    const { countryCode } = useParams();
    const navigate = useNavigate();
    const { isAuthenticated } = useAuth();

    const [sortKey, setSortKey] = useState("");

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await api.countries.getByCode(countryCode);
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
                navigate("/login");
            }, 2000);
        }
    };

    const sortPanels = (panels, key) => {
        if (!key) return panels;
        return [...panels].sort((a, b) => {
            const aVal =
                typeof a[key] === "string" ? parseFloat(a[key].replace("%", "")) : a[key] ?? 0;
            const bVal =
                typeof b[key] === "string" ? parseFloat(b[key].replace("%", "")) : b[key] ?? 0;
            return aVal - bVal;
        });
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
                <Link
                    to="/SolarComparison"
                    className="text-decoration-none d-inline-flex align-items-center gap-2 mb-4 mt-3"
                    style={{ color: "#6c757d", fontWeight: "500", fontSize: "1rem" }}
                >
                    <i className="bi bi-arrow-left-circle" style={{ fontSize: "1.2rem" }}></i>
                    <span>Back</span>
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

                        {/* Dropdown Filter */}
                        {country.solarPanels?.length > 0 && (
                            <div className="mb-4 d-flex align-items-center gap-2">
                                <label htmlFor="sortSelect" className="form-label mb-0 fw-bold">
                                    Sort Panels:
                                </label>
                                <select
                                    id="sortSelect"
                                    className="form-select w-auto"
                                    value={sortKey}
                                    onChange={(e) => setSortKey(e.target.value)}
                                >
                                    <option value="installationCost">Installation Cost</option>
                                    <option value="efficiency">Efficiency</option>
                                    <option value="productionPerPanel">Production</option>
                                </select>
                            </div>
                        )}

                        {/* Solar Panels Table */}
                        {country.solarPanels?.length > 0 && (
                            <div className="card shadow-sm p-4 mb-5">
                                <h4 className="mb-4">Compare Solar Panels</h4>
                                <div className="table-responsive">
                                    <table className="table table-hover align-middle">
                                        <thead className="table-light">
                                            <tr>
                                                <th>Name</th>
                                                <th>Installation Cost (£)</th>
                                                <th>Production (W)</th>
                                                <th>Efficiency (%)</th>
                                                <th>Warranty</th>
                                                <th>Donate</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {sortPanels(country.solarPanels, sortKey).map(
                                                (panel, index) => (
                                                    <tr key={index}>
                                                        <td>{panel.panelName}</td>
                                                        <td>£{panel.installationCost ?? "-"}</td>
                                                        <td>{panel.productionPerPanel ?? "-"}</td>
                                                        <td>{panel.efficiency ?? "-"}</td>
                                                        <td>{panel.warranty ?? "-"}</td>
                                                        <td>
                                                            <button
                                                                className="btn btn-outline-success btn-sm"
                                                                onClick={() =>
                                                                    handleDonateClick(
                                                                        panel.id || index,
                                                                    )
                                                                }
                                                            >
                                                                <i className="bi bi-heart-fill"></i>{" "}
                                                                Donate
                                                            </button>
                                                        </td>
                                                    </tr>
                                                ),
                                            )}
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        )}
                    </>
                )}
            </section>
        </div>
    );
};

export default InstallationCost;

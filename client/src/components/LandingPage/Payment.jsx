import { PDFDownloadLink } from "@react-pdf/renderer";
import React, { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { api } from "../../api";
import { useAuth } from "../../context/AuthContext";
import { PaymentInvoice } from "../Staff/Invoice/Invoice.jsx";
import Navbar from "./Navbar.jsx";

const Payment = () => {
    const [country, setCountry] = useState(null);
    const [selectedPanel, setSelectedPanel] = useState(null);
    const [selectedCountryData, setSelectedCountryData] = useState(null);
    const [quantity, setQuantity] = useState(1);
    const [currentStep, setCurrentStep] = useState(1);
    const [formData, setFormData] = useState({
        name: "",
        email: "",
        cardNumber: "",
        expiry: "",
        cvv: "",
    });
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [isComplete, setIsComplete] = useState(false);

    const { countryCode, panelId } = useParams();
    const { userId } = useAuth();

    useEffect(() => {
        console.log("Panel ID:", panelId, "Country Code:", countryCode);
        const fetchPanelData = async () => {
            const token = localStorage.getItem("token"); // <-- get the token from localStorage
            console.log("Fetched token for panel:", token);

            if (!token) {
                console.error("No token found, please login first");
                return;
            }

            try {
                const response = await api.panels.getById(panelId);
                console.log("Panel data:", response.data);

                const panelData = response.data;
                setSelectedPanel(panelData);

                setCountry({
                    country: panelData.countryCode,
                    countryCode: panelData.countryCode,
                });

                const responseCountry = await api.countries.getByCode(countryCode);

                console.log("Country data:", responseCountry.data);
                setSelectedCountryData(responseCountry.data)

            } catch (error) {
                console.error("Error fetching panel data:", error);
            }
        };

        fetchPanelData();
    }, [panelId]);

    const handleQuantityChange = (amount) => {
        const newQuantity = quantity + amount;
        if (newQuantity >= 1) {
            setQuantity(newQuantity);
        }
    };

    const calculateTotal = () => {
        if (!selectedPanel) return 0;
        return selectedPanel.installationCost * quantity;
    };

    const handleInputChange = (e) => {
        const { id, value } = e.target;
        let formattedValue = value;

        // Format card number with spaces
        if (id === "cardNumber") {
            formattedValue = value
                .replace(/\s/g, "")
                .replace(/(\d{4})/g, "$1 ")
                .trim();
        }

        // Format expiry date as MM/YY
        if (id === "expiry") {
            formattedValue = value
                .replace(/\D/g, "")
                .replace(/^(\d{2})/, "$1/")
                .substr(0, 5);
        }

        setFormData({
            ...formData,
            [id]: formattedValue,
        });
    };

    const handleNextStep = () => {
        setCurrentStep((prevStep) => prevStep + 1);
    };

    const handlePrevStep = () => {
        setCurrentStep((prevStep) => prevStep - 1);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);

        try {
            // Extract card number without spaces
            const cardNumber = formData.cardNumber.replace(/\s/g, "");
            console.log(userId);
            // Create payment request data
            const paymentData = {
                userId: userId,
                countryPanelId: selectedPanel.id, // Assuming the panel has an id field
                quantity: quantity,
                paymentType: "STRIPE",
                paymentMethodId: `pm_card_visa`, // Just for demonstration
            };
            console.log(paymentData);

            // Send payment request to API
            const response = await api.payments.create(paymentData);

            // Payment successful
            setIsComplete(true);
            setCurrentStep(3);
        } catch (error) {
            console.error("Payment failed:", error);
        } finally {
            setIsSubmitting(false);
        }
    };

    const calculateCarbonSavings = (panel, quantity) =>{

        if (selectedCountryData && selectedCountryData.carbonEmissions) {
            const carbonEmission = Math.abs(selectedCountryData.carbonEmissions);
            const electricity = selectedCountryData.electricityAvailability;
            const panelEnergy = ((panel * quantity * 5)/1000); // 470 Watt * quantity of panel * average sunlight in hours and divided to kWatt
            const carbonIntensity = (carbonEmission * 1000) / (electricity * 1000) ;
            const carbonSavings = (panelEnergy * carbonIntensity);
            const carbonSavingsinPercentage = (carbonSavings / (carbonEmission * 1000)) * 100;

            console.log('calculation', carbonSavingsinPercentage)
            return parseFloat(carbonSavingsinPercentage.toFixed(5));;

        } else {
            console.error("Carbon emissions data is not available yet.");
            return 0; // Return a default value if data is not available
        }


    }

    // Progress step component
    const ProgressSteps = () => {
        return (
            <div className="mb-4">
                <div className="d-flex justify-content-between position-relative">
                    {/* Progress bar connecting steps */}
                    <div
                        className="position-absolute"
                        style={{
                            height: "3px",
                            background: "#e0e0e0",
                            width: "100%",
                            top: "15px",
                            zIndex: 1,
                        }}
                    ></div>

                    {/* Filled progress */}
                    <div
                        className="position-absolute"
                        style={{
                            height: "3px",
                            background: "#28a745",
                            width: `${(currentStep - 1) * 50}%`,
                            top: "15px",
                            zIndex: 1,
                            transition: "width 0.3s ease",
                        }}
                    ></div>

                    {/* Step circles */}
                    <div
                        className="d-flex flex-column align-items-center position-relative"
                        style={{ zIndex: 2 }}
                    >
                        <div
                            className={`rounded-circle d-flex align-items-center justify-content-center ${
                                currentStep >= 1 ? "bg-success text-white" : "bg-light"
                            }`}
                            style={{ width: "30px", height: "30px", border: "1px solid #dee2e6" }}
                        >
                            {currentStep > 1 ? <i className="bi bi-check"></i> : "1"}
                        </div>
                        <span className="mt-2 small">Quantity</span>
                    </div>

                    <div
                        className="d-flex flex-column align-items-center position-relative"
                        style={{ zIndex: 2 }}
                    >
                        <div
                            className={`rounded-circle d-flex align-items-center justify-content-center ${
                                currentStep >= 2 ? "bg-success text-white" : "bg-light"
                            }`}
                            style={{ width: "30px", height: "30px", border: "1px solid #dee2e6" }}
                        >
                            {currentStep > 2 ? <i className="bi bi-check"></i> : "2"}
                        </div>
                        <span className="mt-2 small">Payment</span>
                    </div>

                    <div
                        className="d-flex flex-column align-items-center position-relative"
                        style={{ zIndex: 2 }}
                    >
                        <div
                            className={`rounded-circle d-flex align-items-center justify-content-center ${
                                currentStep >= 3 ? "bg-success text-white" : "bg-light"
                            }`}
                            style={{ width: "30px", height: "30px", border: "1px solid #dee2e6" }}
                        >
                            3
                        </div>
                        <span className="mt-2 small">Confirmation</span>
                    </div>
                </div>
            </div>
        );
    };

    return (
        <div>
            <Navbar />
            <section className="container mt-4">
                {/* Back Button */}
                <Link
                    to={`/InstallationCost/${countryCode}`}
                    className="text-decoration-none d-inline-flex align-items-center gap-2 mb-4 mt-3"
                    style={{ color: "#6c757d", fontWeight: "500", fontSize: "1rem" }}
                >
                    <i className="bi bi-arrow-left-circle" style={{ fontSize: "1.2rem" }}></i>
                    <span>Back</span>
                </Link>

                {/* Main Content */}
                {country && (
                    <>
                        <h2 className="mb-4">Donate to Solar Project in {country.country}</h2>

                        {/* Progress Steps */}
                        {selectedPanel && <ProgressSteps />}

                        <div className="row">
                            {/* Left Side - Panel Information */}
                            {!selectedPanel ? (
                                <div className="col-md-12">
                                    <div className="alert alert-info">
                                        No panel selected. Please go back and select a panel.
                                    </div>
                                </div>
                            ) : (
                                <>
                                    <div className="col-md-5">
                                        <div className="card shadow-sm mb-4">
                                            <div className="card-body">
                                                <h4 className="card-title">
                                                    {selectedPanel.panelName}
                                                </h4>
                                                <p className="text-muted">
                                                    {selectedPanel.description}
                                                </p>
                                                <ul className="list-group list-group-flush mt-3 mb-3">
                                                    <li className="list-group-item">
                                                        <strong>Cost per Panel:</strong> £
                                                        {selectedPanel.installationCost}
                                                    </li>
                                                    {selectedPanel.efficiency && (
                                                        <li className="list-group-item">
                                                            <strong>Efficiency:</strong>{" "}
                                                            {selectedPanel.efficiency}%
                                                        </li>
                                                    )}
                                                    {selectedPanel.warranty && (
                                                        <li className="list-group-item">
                                                            <strong>Warranty:</strong>{" "}
                                                            {selectedPanel.warranty} years
                                                        </li>
                                                    )}
                                                    {selectedPanel.productionPerPanel && (
                                                        <li className="list-group-item">
                                                            <strong>Production:</strong>{" "}
                                                            {selectedPanel.productionPerPanel} W
                                                        </li>
                                                    )}
                                                </ul>

                                                {/*total savings*/}
                                                <div className="mt-4 p-3 bg-success-subtle rounded">
                                                    <p> Amazing! You are saving around <b>{calculateCarbonSavings(selectedPanel.productionPerPanel,quantity)}%  </b>
                                                        of carbon emissions per day by donating {quantity} solar panel. Thank you!</p>
                                                </div>

                                                {/* Order Summary */}
                                                <div className="mt-4 p-3 bg-light rounded">
                                                    <h5 className="mb-3">Order Summary</h5>
                                                    <div className="d-flex justify-content-between mb-2">
                                                        <span>Panel Cost:</span>
                                                        <span>
                                                            £{selectedPanel.installationCost}
                                                        </span>
                                                    </div>
                                                    <div className="d-flex justify-content-between mb-2">
                                                        <span>Quantity:</span>
                                                        <span>{quantity}</span>
                                                    </div>
                                                    <hr />
                                                    <div className="d-flex justify-content-between fw-bold">
                                                        <span>Total:</span>
                                                        <span>
                                                            £{calculateTotal().toLocaleString()}
                                                        </span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    {/* Right Side - Multi-step Process */}
                                    <div className="col-md-7">
                                        {/* Step 1: Quantity Selection */}
                                        {currentStep === 1 && (
                                            <div className="card shadow-sm mb-4">
                                                <div className="card-body">
                                                    <h4 className="mb-3">Select Quantity</h4>
                                                    <div className="d-flex align-items-center mb-4">
                                                        <button
                                                            className="btn btn-outline-secondary"
                                                            onClick={() => handleQuantityChange(-1)}
                                                            disabled={quantity <= 1}
                                                        >
                                                            <i className="bi bi-dash"></i>
                                                        </button>
                                                        <span className="mx-4 fs-5">
                                                            {quantity}
                                                        </span>
                                                        <button
                                                            className="btn btn-outline-secondary"
                                                            onClick={() => handleQuantityChange(1)}
                                                        >
                                                            <i className="bi bi-plus"></i>
                                                        </button>
                                                        <div className="ms-auto">
                                                            <h5>
                                                                Total: £
                                                                {calculateTotal().toLocaleString()}
                                                            </h5>
                                                        </div>
                                                    </div>

                                                    <div className="d-flex justify-content-end">
                                                        <button
                                                            className="btn btn-primary px-4"
                                                            onClick={handleNextStep}
                                                        >
                                                            Next{" "}
                                                            <i className="bi bi-arrow-right"></i>
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                        )}

                                        {/* Step 2: Payment Form */}
                                        {currentStep === 2 && (
                                            <div className="card shadow-sm mb-4">
                                                <div className="card-body">
                                                    <h4 className="mb-3">Payment Details</h4>
                                                    <form onSubmit={handleSubmit}>
                                                        <div className="mb-3">
                                                            <label
                                                                htmlFor="name"
                                                                className="form-label"
                                                            >
                                                                Full Name
                                                            </label>
                                                            <input
                                                                type="text"
                                                                className="form-control"
                                                                id="name"
                                                                value={formData.name}
                                                                onChange={handleInputChange}
                                                                required
                                                            />
                                                        </div>
                                                        <div className="mb-3">
                                                            <label
                                                                htmlFor="email"
                                                                className="form-label"
                                                            >
                                                                Email
                                                            </label>
                                                            <input
                                                                type="email"
                                                                className="form-control"
                                                                id="email"
                                                                value={formData.email}
                                                                onChange={handleInputChange}
                                                                required
                                                            />
                                                        </div>
                                                        <div className="mb-3">
                                                            <label
                                                                htmlFor="cardNumber"
                                                                className="form-label"
                                                            >
                                                                Card Number
                                                            </label>
                                                            <div className="input-group">
                                                                <input
                                                                    type="text"
                                                                    className="form-control"
                                                                    id="cardNumber"
                                                                    placeholder="1234 5678 9012 3456"
                                                                    value={formData.cardNumber}
                                                                    onChange={handleInputChange}
                                                                    maxLength="19"
                                                                    required
                                                                />
                                                                <span className="input-group-text">
                                                                    <i className="bi bi-credit-card"></i>
                                                                </span>
                                                            </div>
                                                        </div>
                                                        <div className="row">
                                                            <div className="col-md-6 mb-3">
                                                                <label
                                                                    htmlFor="expiry"
                                                                    className="form-label"
                                                                >
                                                                    Expiry Date (MM/YY)
                                                                </label>
                                                                <input
                                                                    type="text"
                                                                    className="form-control"
                                                                    id="expiry"
                                                                    placeholder="MM/YY"
                                                                    value={formData.expiry}
                                                                    onChange={handleInputChange}
                                                                    maxLength="5"
                                                                    required
                                                                />
                                                            </div>
                                                            <div className="col-md-6 mb-3">
                                                                <label
                                                                    htmlFor="cvv"
                                                                    className="form-label"
                                                                >
                                                                    CVV
                                                                </label>
                                                                <input
                                                                    type="text"
                                                                    className="form-control"
                                                                    id="cvv"
                                                                    placeholder="123"
                                                                    value={formData.cvv}
                                                                    onChange={handleInputChange}
                                                                    maxLength="3"
                                                                    required
                                                                />
                                                            </div>
                                                        </div>
                                                        <div className="d-flex justify-content-between">
                                                            <button
                                                                type="button"
                                                                className="btn btn-outline-secondary"
                                                                onClick={handlePrevStep}
                                                            >
                                                                <i className="bi bi-arrow-left"></i>{" "}
                                                                Back
                                                            </button>
                                                            <button
                                                                type="submit"
                                                                className="btn btn-success px-4"
                                                                disabled={isSubmitting}
                                                            >
                                                                {isSubmitting ? (
                                                                    <>
                                                                        <span
                                                                            className="spinner-border spinner-border-sm me-2"
                                                                            role="status"
                                                                            aria-hidden="true"
                                                                        ></span>
                                                                        Processing...
                                                                    </>
                                                                ) : (
                                                                    <>Complete Donation</>
                                                                )}
                                                            </button>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                        )}

                                        {/* Step 3: Confirmation */}
                                        {currentStep === 3 && (
                                            <div className="card shadow-sm mb-4">
                                                <div className="card-body text-center py-5">
                                                    <div className="mb-4">
                                                        <i
                                                            className="bi bi-check-circle text-success"
                                                            style={{ fontSize: "4rem" }}
                                                        ></i>
                                                    </div>
                                                    <h3 className="mb-3">
                                                        Thank You for Your Donation!
                                                    </h3>
                                                    <p className="text-muted mb-4">
                                                        You have donated £
                                                        {calculateTotal().toLocaleString()} for{" "}
                                                        {quantity} {selectedPanel.panelName} panels.
                                                        A confirmation has been sent to your email.
                                                    </p>
                                                    <div className="mt-3">
                                                        <Link
                                                            to="/SolarComparison"
                                                            className="btn btn-outline-primary me-2"
                                                        >
                                                            View More Countries
                                                        </Link>
                                                        <Link
                                                            to="/"
                                                            className="btn btn-outline-secondary"
                                                        >
                                                            Return to Home
                                                        </Link>
                                                    </div>
                                                    <div className="mt-2">
                                                        <PDFDownloadLink
                                                            document={
                                                               <PaymentInvoice
                                                                    userId={userId}
                                                                    countryPanelId={selectedPanel?.id}
                                                                    amount={calculateTotal()}
                                                                    total={calculateTotal()}
                                                                    quantity={quantity}
                                                                    panelName={selectedPanel?.panelName}
                                                                    paymentType="STRIPE"
                                                                    paymentMethodId={`pm_card_${formData.cardNumber
                                                                        .replace(/\s/g, "")
                                                                        .slice(-4)}`}
                                                                />
                                                            }
                                                            fileName={`invoice_user_${userId}_panel_${selectedPanel?.id}.pdf`}
                                                            className="btn btn-outline-success me-2 px-3 py-2"
                                                        >
                                                            {({ loading }) =>
                                                                loading ? (
                                                                    "Preparing Invoice..."
                                                                ) : (
                                                                    <>
                                                                        Download Invoice{" "}
                                                                        <i className="bi bi-printer px-2"></i>
                                                                    </>
                                                                )
                                                            }
                                                        </PDFDownloadLink>
                                                    </div>
                                                </div>
                                            </div>
                                        )}
                                    </div>
                                </>
                            )}
                        </div>
                    </>
                )}
            </section>
        </div>
    );
};

export default Payment;

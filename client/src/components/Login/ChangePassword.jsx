import React, { useState } from "react";
import Navbar from "/src/components/LandingPage/Navbar.jsx";
import Footer from "/src/components/LandingPage/Footer.jsx";
import authEndpoints from "/src/api/auth.js";

const ChangePassword = () => {
    const [email, setEmail] = useState("");
    const [status, setStatus] = useState({
        message: "",
        isError: false,
        isLoading: false,
    });

    const handleEmailChange = (e) => {
        setEmail(e.target.value);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!email) {
            setStatus({
                message: "Please enter your email",
                isError: true,
                isLoading: false,
            });
            return;
        }

        try {
            setStatus({
                message: "Sending reset link...",
                isError: false,
                isLoading: true,
            });

            const response = await authEndpoints.resetPassword(email);
            const responseBody = response.data;

            if (responseBody.success) {
                // Success case - response has format from ApiResponseGenerator.success
                setStatus({
                    message: responseBody.response || "Reset link sent! Please check your email.",
                    isError: false,
                    isLoading: false,
                });
                setEmail("");
            } else {
                // Error case - response has format from ApiResponseGenerator.fail
                setStatus({
                    message:
                        responseBody.error?.message ||
                        "Failed to send reset link. Please try again.",
                    isError: true,
                    isLoading: false,
                });
            }
        } catch (error) {
            setStatus({
                message:
                    error.response?.data?.error?.message ||
                    "An error occurred. Please try again later.",
                isError: true,
                isLoading: false,
            });
        }
    };

    return (
        <>
            <Navbar />
            <div className="container d-flex justify-content-center align-items-center vh-100">
                <div className="card p-4 shadow-lg" style={{ maxWidth: "500px", width: "100%" }}>
                    <h3 className="text-center mb-4">Reset Your Password</h3>

                    {status.message && (
                        <div
                            className={`alert ${status.isError ? "alert-danger" : "alert-success"}`}
                            role="alert"
                        >
                            {status.message}
                        </div>
                    )}

                    <form onSubmit={handleSubmit}>
                        <div className="mb-3">
                            <label className="form-label">Email</label>
                            <input
                                type="email"
                                className="form-control"
                                placeholder="Enter your registered email"
                                value={email}
                                onChange={handleEmailChange}
                                required
                            />
                        </div>
                        <button
                            type="submit"
                            className="btn btn-primary w-100"
                            disabled={status.isLoading}
                        >
                            {status.isLoading ? "Sending..." : "Send Reset Link"}
                        </button>
                    </form>
                </div>
            </div>
            <Footer />
        </>
    );
};

export default ChangePassword;

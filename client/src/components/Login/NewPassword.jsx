import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import Navbar from "/src/components/LandingPage/Navbar.jsx";
import Footer from "/src/components/LandingPage/Footer.jsx";
import authEndpoints from "/src/api/auth.js";

const NewPassword = () => {
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [email, setEmail] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState("");
    const navigate = useNavigate();
    const location = useLocation();

    useEffect(() => {
        // Extract email from URL query parameter
        const queryParams = new URLSearchParams(location.search);
        const emailFromUrl = queryParams.get("email") || location.search.substring(1);
        if (!emailFromUrl) {
            setError("Email not found in URL. Unable to reset password.");
        } else {
            setEmail(emailFromUrl);
        }
    }, [location]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");

        if (!email) {
            setError("Email is missing. Please try the reset link again.");
            return;
        }

        if (password !== confirmPassword) {
            setError("Passwords do not match!");
            return;
        }

        if (password.length < 6) {
            setError("Password must be at least 6 characters long");
            return;
        }

        setIsLoading(true);

        try {
            // Call the API to update the password
            const response = await authEndpoints.updatePassword({
                email: email,
                password: password,
            });

            // Check if the response indicates success
            if (response.data && response.data.success) {
                alert("Password reset successful! Please login.");
                navigate("/login");
            } else {
                setError(
                    response.data?.error?.message || "Failed to update password. Please try again.",
                );
            }
        } catch (error) {
            console.error("Error updating password:", error);
            setError(
                error.response?.data?.error?.message || "An error occurred. Please try again.",
            );
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <>
            <Navbar />
            <div className="container d-flex justify-content-center align-items-center vh-100">
                <div className="card p-4 shadow-lg" style={{ maxWidth: "500px", width: "100%" }}>
                    <h3 className="text-center mb-4">Set New Password</h3>

                    {error && (
                        <div className="alert alert-danger" role="alert">
                            {error}
                        </div>
                    )}

                    <form onSubmit={handleSubmit}>
                        <div className="mb-3">
                            <label className="form-label">Email</label>
                            <input type="email" className="form-control" value={email} disabled />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">New Password</label>
                            <input
                                type="password"
                                className="form-control"
                                placeholder="Enter new password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Confirm New Password</label>
                            <input
                                type="password"
                                className="form-control"
                                placeholder="Confirm new password"
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                                required
                            />
                        </div>
                        <button
                            type="submit"
                            className="btn btn-primary w-100"
                            disabled={isLoading}
                        >
                            {isLoading ? "Updating..." : "Reset Password"}
                        </button>
                    </form>
                </div>
            </div>
            <Footer />
        </>
    );
};

export default NewPassword;

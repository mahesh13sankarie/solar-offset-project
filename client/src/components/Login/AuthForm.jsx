import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { useAuth } from "../../context/AuthContext"; // Import useAuth hook
import { FaSun, FaSolarPanel } from "react-icons/fa";


const SolarWelcome = ({ message }) => {
    const fullName = localStorage.getItem("fullName") || "Guest";

    return (
        <div className="d-flex flex-column justify-content-center align-items-center vh-100 text-center bg-light">
            <div className="sun-icon mb-4">
                <FaSun className="text-warning display-3 rotating-slow" />
            </div>
            <h2 className="fw-bold">Welcome, {fullName}!</h2>
            <p className="lead text-secondary">
                {message || "Powering a brighter world together 🌞"}
            </p>
            <div className="solar-icon mt-4">
                <FaSolarPanel className="text-primary display-3 floating-panel" />
            </div>
        </div>
    );
};

const AuthForm = () => {
    const [formState, setFormState] = useState("login"); // 'login' or 'register'
    const [formData, setFormData] = useState({
        email: "",
        password: "",
        confirmPassword: "",
        fullName: "",
    });
    const [message, setMessage] = useState("");
    const [submitted, setSubmitted] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();
    const { isAuthenticated, login } = useAuth(); // Use the auth context

    useEffect(() => {
        if (isAuthenticated) {
            setIsLoading(true);
        }
    }, [isAuthenticated, navigate]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage("");
        setSubmitted(true);

        if (formState === "register" && formData.password !== formData.confirmPassword) {
            setMessage("Passwords do not match");
            setSubmitted(false);
            return;
        }

        try {
            if (formState === "login") {
                const res = await axios.post("http://localhost:8000/api/v1/auth/login", {
                    email: formData.email,
                    password: formData.password,
                });
                const userData = res.data.data;
                login({
                    token: userData.token,
                    userId: userData.id,
                });
                localStorage.setItem("fullName", userData.fullName);
                setMessage("Login successful");
                setIsLoading(true); // Show the loading animation
                setTimeout(() => navigate("/SolarComparison"), 2000); // Increased delay to show animation
            } else {
                const res = await axios.post("http://localhost:8000/api/v1/auth/register", {
                    email: formData.email,
                    password: formData.password,
                    fullName: formData.fullName,
                    accountType: 1,
                });

                if (res.status === 200) {
                    setMessage("Registered successfully, please login");
                    setFormState("login");
                }
            }
        } catch (err) {
            setMessage("Error: Unable to connect to server");
        } finally {
            if (formState !== "login") {
                setSubmitted(false);
            }
        }
    };

    const switchForm = (state) => {
        setFormState(state);
        setFormData({ email: "", password: "", confirmPassword: "", fullName: "", country: "" });
        setMessage("");
    };

    if (isLoading) {
        return <SolarWelcome message={message} />;
    }

    return (
        <div className="container d-flex justify-content-center align-items-center vh-100">
            <div className="card p-4 shadow-lg" style={{ maxWidth: "800px", width: "100%" }}>
                <div className="row g-0">
                    <div className="col-md-6 d-flex flex-column justify-content-center p-3 bg-light rounded-start">
                        <h3 className="text-center">Welcome</h3>
                        <p className="text-muted">Login or Register to continue.</p>
                    </div>

                    <div className="col-md-6 p-4">
                        {!submitted ? (
                            <>
                                <h3 className="text-center">
                                    {formState === "login" ? "Login" : "Register"}
                                </h3>
                                <form onSubmit={handleSubmit}>
                                    <div className="mb-3">
                                        <label className="form-label">Email</label>
                                        <input
                                            type="email"
                                            className="form-control"
                                            name="email"
                                            value={formData.email}
                                            onChange={handleChange}
                                            required
                                        />
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Password</label>
                                        <input
                                            type="password"
                                            className="form-control"
                                            name="password"
                                            value={formData.password}
                                            onChange={handleChange}
                                            required
                                        />
                                    </div>

                                    {formState === "register" && (
                                        <>
                                            <div className="mb-3">
                                                <label className="form-label">
                                                    Confirm Password
                                                </label>
                                                <input
                                                    type="password"
                                                    className="form-control"
                                                    name="confirmPassword"
                                                    value={formData.confirmPassword}
                                                    onChange={handleChange}
                                                    required
                                                />
                                            </div>
                                            <div className="mb-3">
                                                <label className="form-label">Full Name</label>
                                                <input
                                                    type="text"
                                                    className="form-control"
                                                    name="fullName"
                                                    value={formData.fullName}
                                                    onChange={handleChange}
                                                    required
                                                />
                                            </div>
                                        </>
                                    )}

                                    {message && (
                                        <p className="text-danger text-center">{message}</p>
                                    )}

                                    <button type="submit" className="btn btn-primary w-100">
                                        Submit
                                    </button>

                                    <div className="text-center mt-3">
                                        {formState === "login" ? (
                                            <>
                                                <a
                                                    href="#"
                                                    onClick={(e) => {
                                                        e.preventDefault();
                                                        switchForm("register");
                                                    }}
                                                >
                                                    Don't have an account? Register
                                                </a>
                                                <br />
                                                <a
                                                    href="/change-password"
                                                    className="text-decoration-none mt-2 d-inline-block"
                                                >
                                                    Forgot Password?
                                                </a>
                                            </>
                                        ) : (
                                            <a
                                                href="#"
                                                onClick={(e) => {
                                                    e.preventDefault();
                                                    switchForm("login");
                                                }}
                                            >
                                                Already have an account? Login
                                            </a>
                                        )}
                                    </div>
                                </form>
                            </>
                        ) : (
                            <h3 className="text-center">Submitted Successfully</h3>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AuthForm;

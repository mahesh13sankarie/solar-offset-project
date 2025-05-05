import { useGoogleLogin } from "@react-oauth/google";
import "bootstrap/dist/css/bootstrap.min.css";
import React, { useEffect, useState } from "react";
import { FaSolarPanel, FaSun } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import { api } from "../../api";
import { useAuth } from "../../context/AuthContext"; // Import useAuth hook
import "./transition.css";
import Footer from "/src/components/LandingPage/Footer.jsx";
import Navbar from "/src/components/LandingPage/Navbar.jsx";

const SolarWelcome = ({ message }) => {
    const fullName = localStorage.getItem("fullName") || "Guest";

    return (
        <div className="fade-in-scale d-flex flex-column justify-content-center align-items-center vh-100 text-center bg-light">
            <FaSun className="text-warning display-3 sun-rotate mb-4" />
            <h2 className="fw-bold">Welcome, {fullName}!</h2>
            <p className="lead text-secondary">
                {message || "Powering a brighter world together 🌞"}
            </p>
            <FaSolarPanel className="text-primary display-3 panel-float mt-4" />
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
                const credentials = {
                    email: formData.email,
                    password: formData.password,
                };
                const res = await api.auth.login(credentials);
                const userData = res.data.data;
                login({
                    token: res.data.token,
                    userId: userData.id,
                });
                localStorage.setItem("token", res.data.token);
                localStorage.setItem("fullName", userData.fullName);
                localStorage.setItem("accountType", userData.accountType); // Store accountType

                setMessage("Login successful");
                setIsLoading(true); // Show the loading animation
                setTimeout(() => navigate("/SolarComparison"), 2000); // Increased delay to show animation
            } else {
                const userData = {
                    email: formData.email,
                    password: formData.password,
                    fullName: formData.fullName,
                    accountType: 1,
                };

                const res = await api.auth.register(userData);

                if (res.status === 200) {
                    setMessage("Registered successfully, please login");
                    setFormState("login");
                }
            }
        } catch (err) {
            if (err.response && err.response.status === 404) {
                setMessage("User does not exist. Please try logging in again.");
            } else {
                setMessage("Error: Unable to connect to server");
            }
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
    const googleLogin = useGoogleLogin({
        onSuccess: async (response) => {
            console.log("Google Login Success:", response);

            try {
                const googleUser = await api.auth.googleUserInfo(response.access_token);
                console.log("Google User Info:", googleUser.data);

                const res = await api.auth.googleLogin({
                    email: googleUser.data.email,
                    name: googleUser.data.name,
                });
                console.log("Backend Response:", res.data);

                const userData = res.data.data;
                login({
                    token: res.data.token,
                    userId: userData.id,
                });
                localStorage.setItem("token", res.data.token);
                localStorage.setItem("fullName", userData.fullName || googleUser.data.name);
                setMessage("Login successful");
                setIsLoading(true);
                setTimeout(() => navigate("/SolarComparison"), 2000);
            } catch (error) {
                console.error("Error during Google login:", error);
                setMessage("Google Login Failed");
            }
        },
        onError: (errorResponse) => {
            console.error("Google Login Failed", errorResponse);
            setMessage("Google Login Failed");
        },
        ux_mode: "popup",
        flow: "implicit",
        scope: "openid email profile",
    });

    if (isLoading) {
        return (
            <div className="fade-in-scale">
                <SolarWelcome message={message} />
            </div>
        );
    }

    return (
        <>
            <Navbar />
            <div
                style={{
                    paddingTop: "120px",
                    paddingBottom: "40px",
                    minHeight: "calc(100vh - 160px)",
                }}
            >
                <div className="container d-flex justify-content-center">
                    <div
                        className="card p-4 shadow-lg w-100"
                        style={{ maxWidth: "800px", minHeight: "450px" }}
                    >
                        <div className="row g-0">
                            <div className="col-md-6 d-flex flex-column justify-content-center p-3 bg-light rounded-start">
                                <h3 className="text-center">Welcome</h3>
                                <p className="text-muted text-center">
                                    Login or Register to continue.
                                </p>
                            </div>

                            <div className="col-md-6 p-4">
                                {!(submitted && isAuthenticated) ? (
                                    <>
                                        <h3 className="text-center">
                                            {formState === "login" ? "Login" : "Register"}
                                        </h3>

                                        {formState === "login" ? (
                                            <>
                                                {/* Email Login Form */}
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
                                                        <label className="form-label">
                                                            Password
                                                        </label>
                                                        <input
                                                            type="password"
                                                            className="form-control"
                                                            name="password"
                                                            value={formData.password}
                                                            onChange={handleChange}
                                                            required
                                                        />
                                                    </div>

                                                    {message && (
                                                        <p className="text-danger text-center">
                                                            {message}
                                                        </p>
                                                    )}

                                                    <button
                                                        type="submit"
                                                        className="btn btn-primary w-100"
                                                    >
                                                        Login
                                                    </button>
                                                </form>

                                                {/* Google Login Button */}
                                                <div className="text-center mt-4">
                                                    <button
                                                        className="btn btn-light d-flex align-items-center justify-content-center mx-auto"
                                                        type="button"
                                                        onClick={() => googleLogin()}
                                                        style={{
                                                            borderRadius: "30px",
                                                            padding: "10px 20px",
                                                            transition: "all 0.3s ease",
                                                            boxShadow: "0 2px 4px rgba(0,0,0,0.1)",
                                                            border: "1px solid #e6e6e6",
                                                            width: "100%",
                                                            maxWidth: "280px",
                                                            fontWeight: "500",
                                                            fontSize: "15px",
                                                            color: "#3c4043",
                                                        }}
                                                        onMouseOver={(e) => {
                                                            e.currentTarget.style.boxShadow =
                                                                "0 4px 8px rgba(0,0,0,0.15)";
                                                            e.currentTarget.style.backgroundColor =
                                                                "#f8f9fa";
                                                        }}
                                                        onMouseOut={(e) => {
                                                            e.currentTarget.style.boxShadow =
                                                                "0 2px 4px rgba(0,0,0,0.1)";
                                                            e.currentTarget.style.backgroundColor =
                                                                "#fff";
                                                        }}
                                                    >
                                                        <svg
                                                            xmlns="http://www.w3.org/2000/svg"
                                                            viewBox="0 0 48 48"
                                                            style={{
                                                                height: 20,
                                                                width: 20,
                                                                marginRight: "12px",
                                                            }}
                                                        >
                                                            <path
                                                                fill="#EA4335"
                                                                d="M24 9.5c3.54 0 6.71 1.22 9.21 3.6l6.85-6.85C35.9 2.38 30.47 0 24 0 14.62 0 6.51 5.38 2.56 13.22l7.98 6.19C12.43 13.72 17.74 9.5 24 9.5z"
                                                            ></path>
                                                            <path
                                                                fill="#4285F4"
                                                                d="M46.98 24.55c0-1.57-.15-3.09-.38-4.55H24v9.02h12.94c-.58 2.96-2.26 5.48-4.78 7.18l7.73 6c4.51-4.18 7.09-10.36 7.09-17.65z"
                                                            ></path>
                                                            <path
                                                                fill="#FBBC05"
                                                                d="M10.53 28.59c-.48-1.45-.76-2.99-.76-4.59s.27-3.14.76-4.59l-7.98-6.19C.92 16.46 0 20.12 0 24c0 3.88.92 7.54 2.56 10.78l7.97-6.19z"
                                                            ></path>
                                                            <path
                                                                fill="#34A853"
                                                                d="M24 48c6.48 0 11.93-2.13 15.89-5.81l-7.73-6c-2.15 1.45-4.92 2.3-8.16 2.3-6.26 0-11.57-4.22-13.47-9.91l-7.98 6.19C6.51 42.62 14.62 48 24 48z"
                                                            ></path>
                                                        </svg>
                                                        Sign in with Google
                                                    </button>
                                                </div>

                                                {/* Divider */}
                                                <div className="d-flex align-items-center mt-4 mb-4">
                                                    <hr className="flex-grow-1" />
                                                    <span className="px-2 text-secondary">OR</span>
                                                    <hr className="flex-grow-1" />
                                                </div>

                                                {/* Secondary Options */}
                                                <div className="text-center">
                                                    <div className="mb-3">
                                                        <a
                                                            href="#"
                                                            onClick={(e) => {
                                                                e.preventDefault();
                                                                switchForm("register");
                                                            }}
                                                            className="text-decoration-none"
                                                        >
                                                            Don't have an account? Register
                                                        </a>
                                                    </div>
                                                    <div>
                                                        <a
                                                            href="/change-password"
                                                            className="text-decoration-none"
                                                        >
                                                            Forgot Password?
                                                        </a>
                                                    </div>
                                                </div>
                                            </>
                                        ) : (
                                            <>
                                                {/* Register Form */}
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
                                                        <label className="form-label">
                                                            Full Name
                                                        </label>
                                                        <input
                                                            type="text"
                                                            className="form-control"
                                                            name="fullName"
                                                            value={formData.fullName}
                                                            onChange={handleChange}
                                                            required
                                                        />
                                                    </div>
                                                    <div className="mb-3">
                                                        <label className="form-label">
                                                            Password
                                                        </label>
                                                        <input
                                                            type="password"
                                                            className="form-control"
                                                            name="password"
                                                            value={formData.password}
                                                            onChange={handleChange}
                                                            required
                                                        />
                                                    </div>
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

                                                    {message && (
                                                        <p className="text-danger text-center">
                                                            {message}
                                                        </p>
                                                    )}

                                                    <button
                                                        type="submit"
                                                        className="btn btn-primary w-100"
                                                    >
                                                        Register
                                                    </button>
                                                </form>

                                                {/* Divider */}
                                                <div className="d-flex align-items-center mt-4 mb-4">
                                                    <hr className="flex-grow-1" />
                                                    <span className="px-2 text-secondary">OR</span>
                                                    <hr className="flex-grow-1" />
                                                </div>

                                                {/* Back to Login */}
                                                <div className="text-center">
                                                    <a
                                                        href="#"
                                                        onClick={(e) => {
                                                            e.preventDefault();
                                                            switchForm("login");
                                                        }}
                                                        className="text-decoration-none"
                                                    >
                                                        Already have an account? Login
                                                    </a>
                                                </div>
                                            </>
                                        )}
                                    </>
                                ) : (
                                    <h3 className="text-center">Submitted Successfully</h3>
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <Footer />
        </>
    );
};

export default AuthForm;

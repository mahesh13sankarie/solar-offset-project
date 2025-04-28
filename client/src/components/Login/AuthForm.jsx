import React, { useEffect, useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { useAuth } from "../../context/AuthContext"; // Import useAuth hook
import { FaSun, FaSolarPanel } from "react-icons/fa";
import "./transition.css";
import { useGoogleLogin, googleLogout} from '@react-oauth/google';
import { jwtDecode } from "jwt-decode";



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
                const res = await axios.post("http://localhost:8000/api/v1/auth/login", {
                    email: formData.email,
                    password: formData.password,
                });
                const userData = res.data.data;
                login({
                    token: res.data.token,
                    userId: userData.id,
                });
                localStorage.setItem("token", res.data.token);
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
        console.log('Google Login Success:', response);

        try {
          const googleUser = await axios.get('https://www.googleapis.com/oauth2/v3/userinfo', {
                  headers: {
                    Authorization: `Bearer ${response.access_token}`,
                  },
          });
          console.log('Google User Info:', googleUser.data);

          const res = await axios.post('http://localhost:8000/api/v1/auth/google-login', {
                  email: googleUser.data.email,
                  name: googleUser.data.name,
          });
          console.log('Backend Response:', res.data);

          const userData = res.data.data;
          login({
            token: res.data.token,
            userId: userData.id,
          });
          localStorage.setItem('token', res.data.token);
          localStorage.setItem('fullName', userData.fullName || googleUser.data.name);
          setMessage('Login successful');
          setIsLoading(true);
          setTimeout(() => navigate('/SolarComparison'), 2000);
        } catch (error) {
          console.error('Error during Google login:', error);
          setMessage('Google Login Failed');
        }
      },
      onError: (errorResponse) => {
        console.error('Google Login Failed', errorResponse);
        setMessage('Google Login Failed');
      },
      ux_mode: 'popup',
      flow: 'implicit',
      scope: 'openid email profile',
    });


if (isLoading) {
    return (
        <div className="fade-in-scale">
            <SolarWelcome message={message} />
        </div>
    );
}

    return (
        <div className="container d-flex justify-content-center align-items-center vh-100">
            <div className="card p-4 shadow-lg" style={{ maxWidth: "800px", width: "100%" }}>
                <div className="row g-0">
                    <div className="col-md-6 d-flex flex-column justify-content-center p-3 bg-light rounded-start">
                        <h3 className="text-center">Welcome</h3>
                        <p className="text-muted text-center">Login or Register to continue.</p>
                    </div>

                    <div className="col-md-6 p-4">
                        {!(submitted && isAuthenticated) ? (
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
                                </form>

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
                                                <br />
                                                 <p className="mt-2">Or</p>
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

                                    {/* Google Login Button */}
                                    <div className="text-center mt-3">
                                      <button
                                        className="gsi-material-button"
                                        type="button"
                                        onClick={() => googleLogin()} // <-- trigger Google OAuth flow!
                                      >
                                        <div className="gsi-material-button-state"></div>
                                        <div className="gsi-material-button-content-wrapper">
                                          <div className="gsi-material-button-icon">
                                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 48 48" style={{ height: 20, width: 20 }}>
                                              <path fill="#EA4335" d="M24 9.5c3.54 0 6.71 1.22 9.21 3.6l6.85-6.85C35.9 2.38 30.47 0 24 0 14.62 0 6.51 5.38 2.56 13.22l7.98 6.19C12.43 13.72 17.74 9.5 24 9.5z"></path>
                                              <path fill="#4285F4" d="M46.98 24.55c0-1.57-.15-3.09-.38-4.55H24v9.02h12.94c-.58 2.96-2.26 5.48-4.78 7.18l7.73 6c4.51-4.18 7.09-10.36 7.09-17.65z"></path>
                                              <path fill="#FBBC05" d="M10.53 28.59c-.48-1.45-.76-2.99-.76-4.59s.27-3.14.76-4.59l-7.98-6.19C.92 16.46 0 20.12 0 24c0 3.88.92 7.54 2.56 10.78l7.97-6.19z"></path>
                                              <path fill="#34A853" d="M24 48c6.48 0 11.93-2.13 15.89-5.81l-7.73-6c-2.15 1.45-4.92 2.3-8.16 2.3-6.26 0-11.57-4.22-13.47-9.91l-7.98 6.19C6.51 42.62 14.62 48 24 48z"></path>
                                              <path fill="none" d="M0 0h48v48H0z"></path>
                                            </svg>
                                            <span className="gsi-material-button-contents">  Sign in with Google</span>
                                          </div>
                                        </div>
                                      </button>
                                    </div>
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

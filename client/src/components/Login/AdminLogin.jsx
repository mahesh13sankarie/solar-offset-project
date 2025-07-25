import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { api } from "../../api";

const AdminLogin = () => {
    const [formData, setFormData] = useState({
        email: "",
        password: "",
    });
    const [message, setMessage] = useState("");
    const navigate = useNavigate();
    const { login } = useAuth();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage("");

        try {
            const credentials = {
                email: formData.email,
                password: formData.password,
            };

            const response = await api.auth.login(credentials);

            const userData = response.data.data;

            // Verify if the user is an admin (accountType === 0)
            if (userData.accountType !== 0) {
                setMessage("Error: Access denied. Admin privileges required.");
                return;
            }

            // If user is admin, proceed with login
            login({
                token: response.data.token,
                userId: userData.id,
            });

            setMessage("Success: Logged in successfully");
            setTimeout(() => {
                navigate("/dashboard");
            }, 1000);
        } catch (error) {
            setMessage(`Error: ${error.message || "Unable to connect to server"}`);
            console.error("Error:", error);
        }

        // try {
        //     const response = await axios.post(REGISTER_URL, {
        //
        //             email: formData.email,
        //             password: formData.password
        //
        //     });
        //     const {token} = response.data;
        //     localStorage.setItem('token',token);
        //     setMessage('Success: Logged in successfully');
        //         setTimeout(() => {
        //             window.location.href = 'http://localhost:5173/dashboard';
        //         }, 1000);
        //
        // }
        // catch (error) {
        //
        //     setMessage(`Error: ${error.response?.data?.message || 'Unable to connect to server'}`);
        //     console.error('Error:', error);
        // }
    };

    return (
        <div className="container d-flex justify-content-center align-items-center vh-100">
            <div className="card p-4 shadow" style={{ maxWidth: "400px", width: "100%" }}>
                <h2 className="text-center mb-4">Admin Login</h2>
                {message && (
                    <div
                        className={`alert ${
                            message.includes("Success") ? "alert-success" : "alert-danger"
                        }`}
                    >
                        {message}
                    </div>
                )}
                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <label htmlFor="email" className="form-label">
                            Email:
                        </label>
                        <input
                            type="email"
                            className="form-control"
                            id="email"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="password" className="form-label">
                            Password:
                        </label>
                        <input
                            type="password"
                            className="form-control"
                            id="password"
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <button type="submit" className="btn btn-primary w-100">
                        Login
                    </button>
                </form>
            </div>
        </div>
    );
};

export default AdminLogin;

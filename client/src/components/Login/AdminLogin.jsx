import React, { useState } from 'react';
import axios from 'axios';


const superAdmin = {
    id: "1",
    email: "admin@example.com",
    password: "admin"
};


// const REGISTER_URL = 'http://localhost:3000/admins';

const AdminLogin = () => {
    const [formData, setFormData] = useState({
        email: '',
        password: '',
    });
    const [message, setMessage] = useState('');

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage('');
        if (superAdmin.email === "admin@example.com" && superAdmin.password === "admin") {
            console.log("✅ Super admin verified and registered (simulated)");
                setMessage('Success: Logged in successfully');
                    setTimeout(() => {
                        window.location.href = 'http://localhost:5173/dashboard';
                    }, 1000);
        } else {
            setMessage(`Error: 'Unable to connect to server'}`);
            console.error("❌ Registration failed: Invalid credentials");
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
            <div className="card p-4 shadow" style={{ maxWidth: '400px', width: '100%' }}>
                <h2 className="text-center mb-4">Admin Login</h2>
                {message && <div className={`alert ${message.includes('Success') ? 'alert-success' : 'alert-danger'}`}>{message}</div>}
                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <label htmlFor="email" className="form-label">Email:</label>
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
                        <label htmlFor="password" className="form-label">Password:</label>
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
                    <button type="submit" className="btn btn-primary w-100">Login</button>
                </form>
            </div>
        </div>
    );
};

export default AdminLogin;
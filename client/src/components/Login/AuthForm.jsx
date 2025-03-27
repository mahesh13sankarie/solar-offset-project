import React, {useEffect, useState} from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import {login} from './HelperFunction.jsx'; // Import helper functions
import {useNavigate} from 'react-router-dom';
import axios from "axios";

const AuthForm = () => {
    const [formState, setFormState] = useState('login'); // 'login' or 'register'
    const [formData, setFormData] = useState({email: '', password: '', confirmPassword: '', fullName: ''});
    const [message, setMessage] = useState('');
    const [submitted, setSubmitted] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('authToken');
        if (token) {
            navigate('/dashboard');
        }
    }, []);

    const handleChange = (e) => {
        const {name, value} = e.target;
        setFormData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage('');
        setSubmitted(true);

        if (formState === 'register' && formData.password !== formData.confirmPassword) {
            setMessage('Passwords do not match');
            setSubmitted(false);
            return;
        }

        try {
            if (formState === 'login') {
                const res = await axios.post('http://localhost:8000/api/v1/auth/login', {
                    email: formData.email,
                    password: formData.password
                });

                localStorage.setItem('authToken', res.data.token);
                setMessage('Login successful');
                setTimeout(() => navigate('/SolarComparison'), 1000);

            } else {
                const res = await axios.post('http://localhost:8000/api/v1/auth/register', {
                    email: formData.email,
                    password: formData.password,
                    fullName: formData.fullName,
                    accountType: 1
                });

                if (res.status === 200) {
                    setMessage("Registered successfully, please login");
                    setFormState("login");
                }
            }
        } catch (err) {
            setMessage('Error: Unable to connect to server');
        } finally {
            setSubmitted(false);
        }
    };

    const switchForm = (state) => {
        setFormState(state);
        setFormData({email: '', password: '', confirmPassword: '', fullName: '', country: ''});
        setMessage('');
    };

    return (
        <div className="container d-flex justify-content-center align-items-center vh-100">
            <div className="card p-4 shadow-lg" style={{maxWidth: '800px', width: '100%'}}>
                <div className="row g-0">
                    <div className="col-md-6 d-flex flex-column justify-content-center p-3 bg-light rounded-start">
                        <h3 className="text-center">Welcome</h3>
                        <p className="text-muted">Login or Register to continue.</p>
                    </div>

                    <div className="col-md-6 p-4">
                        {!submitted ? (
                            <>
                                <h3 className="text-center">{formState === 'login' ? 'Login' : 'Register'}</h3>
                                <form onSubmit={handleSubmit}>
                                    <div className="mb-3">
                                        <label className="form-label">Email</label>
                                        <input type="email" className="form-control" name="email" value={formData.email}
                                               onChange={handleChange} required/>
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Password</label>
                                        <input type="password" className="form-control" name="password"
                                               value={formData.password} onChange={handleChange} required/>
                                    </div>

                                    {formState === 'register' && (
                                        <>
                                            <div className="mb-3">
                                                <label className="form-label">Confirm Password</label>
                                                <input type="password" className="form-control" name="confirmPassword"
                                                       value={formData.confirmPassword} onChange={handleChange}
                                                       required/>
                                            </div>
                                            <div className="mb-3">
                                                <label className="form-label">Full Name</label>
                                                <input type="text" className="form-control" name="fullName"
                                                       value={formData.fullName} onChange={handleChange} required/>
                                            </div>
                                        </>
                                    )}

                                    {message && <p className="text-danger text-center">{message}</p>}

                                    <button type="submit" className="btn btn-primary w-100">Submit</button>

                                    <p className="text-center mt-3">
                                        {formState === 'login' ? (
                                            <a href="#" onClick={() => switchForm('register')}>Don't have an account?
                                                Register</a>
                                        ) : (
                                            <a href="#" onClick={() => switchForm('login')}>Already have an account?
                                                Login</a>
                                        )}
                                    </p>
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
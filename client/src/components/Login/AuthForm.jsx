import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';

const AuthForm = () => {
    const [formState, setFormState] = useState('login'); // 'login', 'register', 'reset'
    const [formData, setFormData] = useState({ email: '', password: '', confirmPassword: '', fullName: '', country: '' });
    const [message, setMessage] = useState('');
    const [submitted, setSubmitted] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({
            ...prevData,
            [name]: value,  
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage('');
        setSubmitted(true);

        // If registering, ensure passwords match
        if (formState === 'register' && formData.password !== formData.confirmPassword) {
            setMessage('Error: Passwords do not match');
            setSubmitted(false);
            return;
        }

        try {
            const endpoint = formState === 'login' ? 'http://localhost:8080/api/login' : 'http://localhost:8080/api/register';
            const response = await fetch(endpoint, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(formData),
            });

            const data = await response.text();
            if (response.ok) {
                setMessage(formState === 'login' ? "Success: Logged in" : "Success: User registered");

                // Clear form after success
                setFormData({ email: '', password: '', confirmPassword: '', fullName: '', country: '' });

                // Redirect to landing page after login
                if (formState === 'login') {
                    setTimeout(() => {
                        window.location.href = '/';
                    }, 1000); // Delay for user feedback
                }
            } else {
                setMessage(`Error: ${data}`);
            }
        } catch (error) {
            setMessage(`Error: ${error.message}`);
        }
    };

    // Clears form when switching between login & register
    const switchForm = (state) => {
        setFormState(state);
        setFormData({ email: '', password: '', confirmPassword: '', fullName: '', country: '' });
        setMessage('');
    };

    return (
        <div className="container d-flex justify-content-center align-items-center vh-100">
            <div className="card p-4 shadow-lg" style={{ maxWidth: '800px', width: '100%' }}>
                <div className="row g-0">
                    {/* Left Side - Thank You Message */}
                    <div className="col-md-6 d-flex flex-column justify-content-center p-3 bg-light rounded-start">
                        <h3 className="text-center">Thank you for Signing Up</h3>
                        <p className="text-muted">
                            Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text since the 1500s.
                        </p>
                    </div>

                    {/* Right Side - Login Form */}
                    <div className="col-md-6 p-4">
                        {!submitted ? (
                            formState === 'reset' ? (
                                <>
                                    <h3 className="text-center">Reset Password</h3>
                                    <form>
                                        <div className="mb-3">
                                            <label className="form-label">Enter your email</label>
                                            <input type="email" className="form-control" required />
                                        </div>
                                        <button className="btn btn-primary w-100">Send Reset Link</button>
                                        <p className="text-center mt-3">
                                            <a href="#" onClick={() => switchForm('login')}>Back to Login</a>
                                        </p>
                                    </form>
                                </>
                            ) : (
                                <>
                                    <h3 className="text-center">{formState === 'login' ? 'Login' : 'Register'}</h3>
                                    <form onSubmit={handleSubmit}>
                                        <div className="mb-3">
                                            <label className="form-label">Email</label>
                                            <input type="email" className="form-control" name="email" value={formData.email} onChange={handleChange} required />
                                        </div>
                                        <div className="mb-3">
                                            <label className="form-label">Password</label>
                                            <input type="password" className="form-control" name="password" value={formData.password} onChange={handleChange} required />
                                        </div>

                                        {formState === 'register' && (
                                            <>
                                                <div className="mb-3">
                                                    <label className="form-label">Confirm Password</label>
                                                    <input type="password" className="form-control" name="confirmPassword" value={formData.confirmPassword} onChange={handleChange} required />
                                                </div>
                                                <div className="mb-3">
                                                    <label className="form-label">Full Name</label>
                                                    <input type="text" className="form-control" name="fullName" value={formData.fullName} onChange={handleChange} required />
                                                </div>
                                                <div className="mb-3">
                                                    <label className="form-label">Country</label>
                                                    <input type="text" className="form-control" name="country" value={formData.country} onChange={handleChange} required />
                                                </div>
                                            </>
                                        )}

                                        {message && <p className="text-danger text-center">{message}</p>}

                                        <button type="submit" className="btn btn-primary w-100">Submit</button>

                                        <p className="text-center mt-3">
                                            {formState === 'login' ? (
                                                <a href="#" onClick={() => switchForm('register')}>Don't have an account? Register</a>
                                            ) : (
                                                <a href="#" onClick={() => switchForm('login')}>Already have an account? Login</a>
                                            )}
                                        </p>

                                        {formState === 'login' && (
                                            <p className="text-center mt-2">
                                                <a href="#" onClick={() => switchForm('reset')}>Forgot Password?</a>
                                            </p>
                                        )}

                                        <p className="text-center mt-2">
                                            <a href="#">Google Login</a>
                                        </p>
                                    </form>
                                </>
                            )
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

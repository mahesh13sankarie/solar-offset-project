import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';

const AuthForm = () => {
  const [formState, setFormState] = useState('login'); // 'login', 'register', 'reset'
    const [formData, setFormData] = useState({ email: '', password: '', fullName: '', country: '' });
    const [message, setMessage] = useState('');

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({
            ...prevData,
            [name]: value,  // ✅ Correctly updates the field dynamically

        }));
        console.log("Updated State:", formData);  // ✅ Debugging
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage('');

        try {
            const response = await fetch('http://localhost:8080/api/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(formData),
            });

            const data = await response.text();
            if (response.ok) {
                setMessage("Success: User registered");
            } else {
                setMessage(`Error: ${data}`);
            }
        } catch (error) {
            setMessage(`Error: ${error.message}`);
        }
    };

    return (
    <div className="container">
      <div className="form-container">
        {formState === 'reset' ? (
          <>
            <h3 className="text-center">Reset Password</h3>
            <form>
              <div className="mb-3">
                <label className="form-label">Enter your email</label>
                <input type="email" className="form-control" required />
              </div>
              <button className="btn btn-primary w-100">Send Reset Link</button>
              <p className="text-center mt-3">
                <a href="#" onClick={() => setFormState('login')}>Back to Login</a>
              </p>
            </form>
          </>
        ) : (
          <>
            <h3 className="text-center">{formState === 'login' ? 'Login' : 'Register'}</h3>
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label className="form-label">Email</label>
                <input type="email" className="form-control" defaultValue={formData.email}
                       required />
              </div>
              <div className="mb-3">
                <label className="form-label">Password</label>
                <input type="password" className="form-control" defaultValue={formData.password}
                       required />
              </div>

              {formState === 'register' && (
                <>
                  <div className="mb-3">
                    <label className="form-label">Full Name</label>
                    <input type="text" className="form-control" defaultValue={formData.fullName}
                           required />
                  </div>
                  <div className="mb-3">
                    <label className="form-label">Country</label>
                    <input type="text" className="form-control" defaultValue={formData.country}
                           required />
                  </div>
                </>
              )}

              <button type="submit" className="btn btn-primary w-100">Submit</button>

              <p className="text-center mt-3">
                {formState === 'login' ? (
                  <a href="#" onClick={() => setFormState('register')}>Don't have an account? Register</a>
                ) : (
                  <a href="#" onClick={() => setFormState('login')}>Already have an account? Login</a>
                )}
              </p>

              {formState === 'login' && (
                <p className="text-center mt-2">
                  <a href="#" onClick={() => setFormState('reset')}>Forgot Password?</a>
                </p>
              )}

              <p className="text-center mt-2">
                <a href="#">Google Login</a>
              </p>
            </form>
          </>
        )}
      </div>
    </div>
  );
};

export default AuthForm;

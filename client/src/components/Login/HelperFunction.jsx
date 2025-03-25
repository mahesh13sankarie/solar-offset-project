import axios from 'axios';

const API_URL = 'http://localhost:8000/api/v1/auth/login';

// Function to log in and store token
export const login = async (email, password) => {
    try {
        const response = await axios.post(API_URL, { email, password });
        if (response.status === 200) {
            const token = response.data.token;
            localStorage.setItem('authToken', token);
            return { success: true, token };
        }
    } catch (error) {
        console.error('Login failed:', error);
        return { success: false, error: error.response?.data?.message || 'Login failed' };
    }
};

// Function to check if user is authenticated
export const isAuthenticated = () => {
    const token = localStorage.getItem('authToken');
    return !!token;
};

// Function to validate token
export const validateToken = async () => {
    const token = localStorage.getItem('authToken');
    if (!token) return false;

    try {
        const response = await axios.get('http://localhost:8000/api/v1/auth/validate', {
            headers: { Authorization: `Bearer ${token}` }
        });
        return response.status === 200;
    } catch (error) {
        console.error('Token validation failed:', error);
        localStorage.removeItem('authToken');
        return false;
    }
};

// Function to log out
export const logout = () => {
    localStorage.removeItem('authToken');
};

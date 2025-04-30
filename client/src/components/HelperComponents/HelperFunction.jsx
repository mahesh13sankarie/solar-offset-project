import axios from "axios";
import { api } from "../../api";

// Function to log in and store token
export const login = async (email, password) => {
    try {
        const credentials = { email, password };
        const response = await api.auth.login(credentials);

        if (response.status === 200) {
            const token = response.data.token;
            localStorage.setItem("authToken", token);
            return { success: true, token };
        }
    } catch (error) {
        console.error("Login failed:", error);
        return { success: false, error: error.response?.data?.message || "Login failed" };
    }
};

// Function to check if user is authenticated
export const isAuthenticated = () => {
    const token = localStorage.getItem("authToken");
    return !!token;
};

// Function to validate token
export const validateToken = async () => {
    const token = localStorage.getItem("authToken");
    if (!token) return false;

    try {
        const response = await api.auth.validate();
        return response.status === 200;
    } catch (error) {
        console.error("Token validation failed:", error);
        localStorage.removeItem("authToken");
        return false;
    }
};

// Function to log out
export const logout = () => {
    localStorage.removeItem("authToken");
    // window.location.href = '/login'; // Redirect to login page
};

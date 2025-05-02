import axios from "axios";
import apiClient from "./config";

// /auth/* endpoints
export const authEndpoints = {
    login: (credentials) => {
        return apiClient.post("/auth/login", credentials);
    },

    register: (userData) => {
        return apiClient.post("/auth/register", userData);
    },

    googleLogin: (googleData) => {
        return apiClient.post("/auth/google-login", googleData);
    },

    resetPassword: (resetEmail) => {
        return apiClient.post("/auth/reset-password", { email: resetEmail });
    },

    validate: () => {
        return apiClient.get("/auth/validate", {
            headers: {
                Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
        });
    },

    // External Google API
    googleUserInfo: (token) => {
        return axios.get("https://www.googleapis.com/oauth2/v3/userinfo", {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        });
    },
};

export default authEndpoints;

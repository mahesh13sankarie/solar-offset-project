import axios from "axios";
import apiClient from "./config";

// /auth/* endpoints
export const authEndpoints = {
    login: (credentials) => {
        return apiClient.post("/auth/login", credentials).then((response) => {
            return {
                ...response,
                data: response.data.response,
            };
        });
    },

    register: (userData) => {
        return apiClient.post("/auth/register", userData).then((response) => {
            return {
                ...response,
                data: response.data.response,
            };
        });
    },

    googleLogin: (googleData) => {
        return apiClient.post("/auth/google-login", googleData).then((response) => {
            return {
                ...response,
                data: response.data.response,
            };
        });
    },

    resetPassword: (resetEmail) => {
        return apiClient.post("/auth/forgot-password", { email: resetEmail });
    },

    updatePassword: (loginData) => {
        return apiClient.put("/auth/update-password", loginData);
    },

    validate: () => {
        return apiClient
            .get("/auth/validate", {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
            })
            .then((response) => {
                return {
                    ...response,
                    data: response.data.response,
                };
            });
    },

    // Contact us form submission
    contactUs: (contactData) => {
        return apiClient.post("/auth/contact-us", contactData).then((response) => {
            return {
                ...response,
                data: response.data.response,
            };
        });
    },

    // Get all enquiries
    getEnquiries: () => {
        return apiClient
            .get("/auth/enquiries", {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                },
            })
            .then((response) => {
                return {
                    ...response,
                    data: response.data.response,
                };
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

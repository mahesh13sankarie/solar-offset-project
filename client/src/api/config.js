import axios from "axios";

// Base axios instance configuration
const apiClient = axios.create({
    baseURL: "http://localhost:8000/api/v1",
    headers: {
        "Content-Type": "application/json",
    },
    timeout: 10000, // 10 seconds
});

export default apiClient;

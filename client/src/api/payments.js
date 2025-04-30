import apiClient from "./config";

// /payments/* endpoints
export const paymentsEndpoints = {
    // Create a new payment
    create: (paymentData) => {
        return apiClient.post("/payments", paymentData, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem("token")}`,
                "Content-Type": "application/json",
            },
        });
    },
};

export default paymentsEndpoints;

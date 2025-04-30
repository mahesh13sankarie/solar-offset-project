import apiClient from "./config";

// /transaction/* endpoints
export const transactionEndpoints = {
    // Get staff transactions
    getStaff: () => {
        return apiClient.get("/transaction/staff", {
            headers: {
                Authorization: `Bearer ${localStorage.getItem("token")}`,
                "Content-Type": "application/json",
            },
        });
    },
};

export default transactionEndpoints;

import apiClient from "./config";

// /transaction/* endpoints
export const transactionEndpoints = {
    // Get staff transactions
    getStaff: () => {
        return apiClient
            .get("/transaction/staff", {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                    "Content-Type": "application/json",
                },
            })
            .then((response) => {
                return {
                    ...response,
                    data: response.data.response,
                };
            });
    },

    // Get user transactions by userId
    getUserTransactions: (userId) => {
        return apiClient
            .get(`/transaction/${userId}`, {
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
};

export default transactionEndpoints;

import apiClient from "./config";

// /dashboard/* endpoints
export const dashboardEndpoints = {
    // Get all users
    getUsers: () => {
        return apiClient.get("/dashboard/users", {
            headers: {
                Authorization: `Bearer ${localStorage.getItem("authToken")}`,
                "Content-Type": "application/json",
            },
        });
    },

    // Update user role
    updateRole: (userData) => {
        return apiClient.put("/dashboard/update-role", userData, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem("authToken")}`,
                "Content-Type": "application/json",
            },
        });
    },

    // Delete user
    deleteUser: (userId) => {
        return apiClient.delete("/dashboard/delete-user", {
            headers: {
                Authorization: `Bearer ${localStorage.getItem("authToken")}`,
                "Content-Type": "application/json",
            },
            data: { userId },
        });
    },
};

export default dashboardEndpoints;

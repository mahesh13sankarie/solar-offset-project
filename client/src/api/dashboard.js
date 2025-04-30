import apiClient from "./config";

// /dashboard/* endpoints
export const dashboardEndpoints = {
    // Get all users
    getUsers: () => {
        return apiClient.get("/dashboard/users");
    },

    // Update user role
    updateRole: (userData) => {
        return apiClient.put("/dashboard/update-role", userData);
    },

    // Delete user
    deleteUser: (userId) => {
        return apiClient.delete("/dashboard/delete-user", {
            data: { userId },
        });
    },
};

export default dashboardEndpoints;

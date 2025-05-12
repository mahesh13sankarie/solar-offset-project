import apiClient from "./config";

// /dashboard/* endpoints
export const dashboardEndpoints = {
    // Get all users
    getUsers: () => {
        return apiClient
            .get("/dashboard/users", {
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

    // Update user role
    updateRole: (userData) => {
        return apiClient
            .put("/dashboard/update-role", userData, {
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

    // Delete user
    deleteUser: (userId) => {
        return apiClient
            .delete("/dashboard/delete-user", {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("token")}`,
                    "Content-Type": "application/json",
                },
                data: { userId },
            })
            .then((response) => {
                return {
                    ...response,
                    data: response.data.response,
                };
            });
    },
};

export default dashboardEndpoints;

import apiClient from "./config";

// /panels/* endpoints
export const panelsEndpoints = {
    // Get panel data by panel ID
    getById: (panelId) => {
        return apiClient
            .get(`/panels/${panelId}`, {
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
};

export default panelsEndpoints;

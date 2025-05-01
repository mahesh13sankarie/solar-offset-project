import apiClient from "./config";

// /countries/* endpoints
export const countriesEndpoints = {
    // Get country data by country code
    getByCode: (countryCode) => {
        return apiClient.get(`/countries/${countryCode}`, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem("token")}`,
                "Content-Type": "application/json",
            },
        });
    },
};

export default countriesEndpoints;

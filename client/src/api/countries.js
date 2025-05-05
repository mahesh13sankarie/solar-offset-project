import apiClient from "./config";

// /countries/* endpoints
export const countriesEndpoints = {
    // Get country data by country code
    getByCode: (countryCode) => {
        return apiClient.get(`/countries/${countryCode}`, {
            headers: {
                "Content-Type": "application/json",
            },
        });
    },
    getAllCountries: () => {
        return apiClient.get("/countries");
    },
};

export default countriesEndpoints;

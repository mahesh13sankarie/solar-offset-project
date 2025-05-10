import apiClient from "./config";

// /countries/* endpoints
export const countriesEndpoints = {
    // Get country data by country code
    getByCode: (countryCode) => {
        return apiClient
            .get(`/countries/${countryCode}`, {
                headers: {
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
    getAllCountries: () => {
        return apiClient.get("/countries").then((response) => {
            return {
                ...response,
                data: response.data.response,
            };
        });
    },
};

export default countriesEndpoints;

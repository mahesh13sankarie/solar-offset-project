import apiClient from "./config";
import authEndpoints from "./auth";
import transactionEndpoints from "./transaction";
import dashboardEndpoints from "./dashboard";
import countriesEndpoints from "./countries";
import panelsEndpoints from "./panels";
import paymentsEndpoints from "./payments";

// Export all API endpoints by URL path
export const api = {
    auth: authEndpoints,
    transaction: transactionEndpoints,
    dashboard: dashboardEndpoints,
    countries: countriesEndpoints,
    panels: panelsEndpoints,
    payments: paymentsEndpoints,
};

// Export the base apiClient for custom requests
export { apiClient };

// Export individual endpoint groups for direct imports
export {
    authEndpoints,
    transactionEndpoints,
    dashboardEndpoints,
    countriesEndpoints,
    panelsEndpoints,
    paymentsEndpoints,
};

// Default export for simpler importing
export default api;

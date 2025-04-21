import React from "react";

// Styles
import "./css/main.css";
import "../node_modules/bootstrap/dist/css/bootstrap.min.css";
import "../node_modules/bootstrap-icons/font/bootstrap-icons.css";
// Components
import Home from "./components/LandingPage/Home";
import { createBrowserRouter, RouterProvider } from "react-router-dom"; // used to find all the routes in our app, and they can be connected to the component.
import AuthForm from "./components/Login/AuthForm";
import SolarComparison from "./components/LandingPage/SolarComparison.jsx";
import Services from "./components/LandingPage/Services.jsx";
import Navbar from "./components/LandingPage/Navbar.jsx";
import InstallationCost from "./components/LandingPage/InstallationCost.jsx";
import ReportGenerate from "./components/Report/ReportGenerate.jsx";
import ChangePassword from "./components/Login/ChangePassword.jsx";

import AdminLogin from "./components/Login/AdminLogin.jsx";
import SideNav from "./components/Dashboard/Sidebar.jsx";
import Dashboard from "./components/Dashboard/Dashboard.jsx";
import Userpage from "./components/Dashboard/UserSection.jsx";
import ReportDashboard from "./components/Report/ReportDashboard.jsx";
import Invoice from "./components/Report/Invoice/Invoice.jsx";
import Payment from "./components/LandingPage/Payment.jsx";

const router = createBrowserRouter([
    { path: "/", element: <Home /> },
    { path: "/login", element: <AuthForm /> },
    { path: "/admin", element: <AdminLogin /> },
    {
        path: "/dashboard",
        element: <Dashboard />,
        children: [
            { path: "/dashboard/users", element: <Userpage /> },
            { path: "/dashboard/reports", element: <ReportGenerate /> },
        ],
    },
    { path: "/login", element: <AuthForm /> },
    { path: "/SolarComparison", element: <SolarComparison /> },
    { path: "/navbar", element: <Navbar /> },
    { path: "/donate", element: <Services /> },
    { path: "/InstallationCost/:countryCode", element: <InstallationCost /> },
    { path: "/Payment/:countryCode/:panelId", element: <Payment /> },
    { path: "/change-password", element: <ChangePassword /> },
    {
        path: "/staff/dashboard",
        element: <ReportDashboard />,
    },
    { path: "/pdf/:panelId", element: <ReportGenerate /> },
]);

const App = () => {
    return <RouterProvider router={router} />;
};

export default App;

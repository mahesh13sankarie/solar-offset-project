import React from 'react';

// Styles
import './css/main.css';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import '../node_modules/bootstrap-icons/font/bootstrap-icons.css';
// Components
import Home from "./components/LandingPage/Home"
import { createBrowserRouter, RouterProvider } from "react-router-dom"; // used to find all the routes in our app, and they can be connected to the component.
import AuthForm from "./components/Login/AuthForm";
import SolarComparison from "./components/LandingPage/SolarComparison.jsx";
import Services from "./components/LandingPage/Services.jsx";
import Navbar from "./components/LandingPage/Navbar.jsx";
import InstallationCost from "./components/LandingPage/InstallationCost.jsx"



const router = createBrowserRouter([
  {path:"/",element:<Home />},
  {path:"/login",element:<AuthForm />},
    {path:"/SolarComparison", element:<SolarComparison/>},
    {path:"/navbar",element:<Navbar />},
    {path:"/donate",element:<Services />},
    {path:"/InstallationCost/:countryCode", element:<InstallationCost/>}
])


const App = () => {
    return (
        <RouterProvider router={router} />
    );
};

export default App;
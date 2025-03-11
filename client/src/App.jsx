import React from 'react';

// Styles
import './css/main.css';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import '../node_modules/bootstrap-icons/font/bootstrap-icons.css';
// Components
import Home from "./components/LandingPage/Home"
import { createBrowserRouter, RouterProvider } from "react-router-dom"; // used to find all the routes in our app, and they can be connected to the component.
import AuthForm from "./components/Login/AuthForm";


const router = createBrowserRouter([
  {path:"/",element:<Home />},
  {path:"/login",element:<AuthForm />}
])


const App = () => {
    return (
        <RouterProvider router={router} />
    );
};

export default App;
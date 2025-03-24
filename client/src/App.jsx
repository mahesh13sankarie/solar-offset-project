import React from 'react';

// Styles
import './css/main.css';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import '../node_modules/bootstrap-icons/font/bootstrap-icons.css';
// Components
import Home from "./components/LandingPage/Home"
import { createBrowserRouter, RouterProvider } from "react-router-dom"; // used to find all the routes in our app, and they can be connected to the component.
import AuthForm from "./components/Login/AuthForm";
import AdminLogin from './components/Login/AdminLogin.jsx';
import SideNav from "./components/Dashboard/Sidebar.jsx";
import Dashboard from "./components/Dashboard/Dashboard.jsx";
import Userpage from "./components/Dashboard/UserSection.jsx";

const router = createBrowserRouter([
  {path:"/",element:<Home />},
  {path:"/login",element:<AuthForm />},
  {path:"/admin",element:<AdminLogin />},
    {
        path:"/dashboard",
        element:<Dashboard />,
        children:[
            {path:"/dashboard/users",element:<Userpage />}
        ]
    }
])


const App = () => {
    return (
        <RouterProvider router={router} />
    );
};

export default App;
import React from "react";
import {Outlet} from "react-router-dom";
import SideNav from "./Sidebar.jsx";

const Dashboard = () => {
    return (
        <div className="d-flex">
            <SideNav />
            <div className="p-4" style={{flexGrow:1}}>
                <Outlet />
            </div>
        </div>
    )
}

export default Dashboard;
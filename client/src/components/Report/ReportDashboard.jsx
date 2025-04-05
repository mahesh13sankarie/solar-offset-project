import React,{ useEffect} from "react";
import {isAuthenticated} from "../Login/HelperFunction.jsx";
import Sidebar from "../Dashboard/Sidebar.jsx";
import StaffNavBar from "./StaffNavBar.jsx";
const REPORTAPI = "dummy.link.com/report";

export default function ReportDashboard() {

    useEffect(() => {
        let checkIsAuthenticated = isAuthenticated();
        console.log(checkIsAuthenticated);
    }, []);


    return (
        <>
            <StaffNavBar />

        </>
    );
}
import React,{ useEffect} from "react";
import {isAuthenticated} from "../HelperComponents/HelperFunction.jsx";
import Sidebar from "../Dashboard/Sidebar.jsx";
import StaffNavBar from "./StaffNavBar.jsx";
import {CountriesSelected, PanelsBought} from "./FeatureCards.jsx";
const REPORTAPI = "dummy.link.com/report";

export default function ReportDashboard() {

    useEffect(() => {
        let checkIsAuthenticated = isAuthenticated();
        console.log(checkIsAuthenticated);
    }, []);

    return (
        <>
            <StaffNavBar />
            <div className="container vh-100 vw-100">
                <div className="d-flex">
                    <CountriesSelected />
                    <PanelsBought />

                </div>
            </div>
        </>
    );
}
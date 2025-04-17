import React, {useEffect, useState} from "react";
import {isAuthenticated} from "../HelperComponents/HelperFunction.jsx";
import Sidebar from "../Dashboard/Sidebar.jsx";
import StaffNavBar from "./StaffNavBar.jsx";
import {CountriesSelected, PanelsBought} from "./FeatureCards.jsx";
import StatsTrends from "./KPI-Components/StatsTrends.jsx";
import TestWidget from "./KPI-Components/TestWidget.jsx";
const REPORTAPI = "dummy.link.com/report";

export default function ReportDashboard() {
    const [activeTab, setActiveTab] = useState("report");

    useEffect(() => {
        let checkIsAuthenticated = isAuthenticated();
        console.log(checkIsAuthenticated);
    }, []);

    return (
        <>
            <StaffNavBar activeTab={activeTab} setActiveTab={setActiveTab}  />

            <div className="container vh-100 vw-100">
                {activeTab === "stats" ? (
                    <TestWidget />
                ) : (
                    <div className="d-flex">
                        <CountriesSelected />
                        <PanelsBought />
                    </div>
                )}
            </div>
        </>
    );
}
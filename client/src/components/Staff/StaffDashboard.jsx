import  React, {useEffect, useState} from "react";
import {isAuthenticated} from "../HelperComponents/HelperFunction.jsx";
import Sidebar from "../Dashboard/Sidebar.jsx";
import StaffNavBar from "./StaffNavBar.jsx";
import {CountriesSelected, PanelsBought} from "./FeatureCards.jsx";
import StatsTrends from "./KPI-Components/StatsTrends.jsx";
import TestWidget from "./KPI-Components/TestWidget.jsx";
import TopDonors from "./KPI-Components/TopDonors.jsx";
import EnquiryDashboard from "./EnquiryDashboard.jsx";
const REPORTAPI = "dummy.link.com/report";

export default function StaffDashboard() {
    const [activeTab, setActiveTab] = useState("report");

    useEffect(() => {
        let checkIsAuthenticated = isAuthenticated();
        console.log(checkIsAuthenticated);
    }, []);

    let content;
    if (activeTab === "stats") {
        content = <TestWidget />;
    } else if (activeTab === "report") {
        content = (
            <div className="d-flex flex-column">
                <div className="d-flex w-100">
                    <TopDonors />
                </div>
                <div className="d-flex">
                    <CountriesSelected />
                    <PanelsBought />
                </div>
            </div>
        );
    } else if (activeTab === "enquires") {
        content = <EnquiryDashboard />;
    }

    return (
        <>
            <div className="d-flex">
                <StaffNavBar activeTab={activeTab} setActiveTab={setActiveTab}  />

                <div className="container vh-100 vw-100">
                    {content}
                </div>
            </div>

        </>
    );
}
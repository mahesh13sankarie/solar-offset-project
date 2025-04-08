import React from "react";
import {PDFViewer} from "@react-pdf/renderer";
import ReportGenerator from "../Report/ReportGenerator.jsx";

export default function ReportGenerate() {
    return (<>
<div className="vh-100 vw-100">
    <PDFViewer>
        <ReportGenerator />
    </PDFViewer>

</div>

    </>)
}
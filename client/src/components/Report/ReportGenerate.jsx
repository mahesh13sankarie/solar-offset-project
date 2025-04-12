import React from "react";
import {PDFViewer} from "@react-pdf/renderer";
import PDFReportGenerator from "./PDFReportGenerator.jsx";

export default function ReportGenerate() {
    return (<>
<div style={{width:'750px',height:'750px'}}>
    <PDFViewer>
        <PDFReportGenerator />
    </PDFViewer>

</div>

    </>)
}
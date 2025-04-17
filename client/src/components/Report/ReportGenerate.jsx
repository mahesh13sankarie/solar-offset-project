import React from "react";
import {PDFViewer} from "@react-pdf/renderer";
import Invoice from "./Invoice/Invoice.jsx";

export default function ReportGenerate() {
    return (<>
<div style={{width:'100%',height:'100vh'}}>
    <PDFViewer width="100%" height="100%">
        <Invoice />
    </PDFViewer>

</div>

    </>)
}
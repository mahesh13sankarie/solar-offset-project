import React from "react";
import {PDFViewer} from "@react-pdf/renderer";
import Invoice from "./Invoice/Invoice.jsx";
import Inv from "./Inv.jsx";

export default function ReportGenerate() {
    const state = location.state;
    return (<>
<div style={{width:'100%',height:'100vh'}}>
    <PDFViewer style={{ width: "100%", height: "100vh" }}>
        <Inv
            countryPanelId={state.countryPanelId}
            amount={state.amount}
            total={state.total}
            quantity={state.quantity}
            panelName={state.panelName}
            paymentType={state.paymentType}
            paymentMethodId={state.paymentMethodId}
        />
    </PDFViewer>

</div>

    </>)
}
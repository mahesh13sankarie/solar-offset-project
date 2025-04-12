import {Document, Page, View,StyleSheet,Text} from "@react-pdf/renderer";
import {Table, TR, TH, TD} from "@ag-media/react-pdf-table";

// Create styles
export const styles = StyleSheet.create({
    page: {
        backgroundColor: "#fff",
        color: "#262626",
        fontFamily: "Helvetica",
        fontSize: "12px",
        padding: "30px 50px",
    },
    header: {
        flexDirection: "row",
        justifyContent: "space-between",
        marginBottom: 20,
    },
    title: {
        fontSize: 24,
    },
    textBold: {
        fontFamily: "Helvetica-Bold",
    },
    spaceY: {
        display: "flex",
        flexDirection: "column",
        gap: "2px",
    },
    billTo: {
        marginBottom: 10,
    },
    table: {
        width: "100%",
        borderColor: "1px solid #f3f4f6",
        margin: "20px 0",
    },
    tableHeader: {
        backgroundColor: "#e5e5e5",
    },
    td: {
        padding: 6,
    },
    totals: {
        display: "flex",
        alignItems: "flex-end",
    },
});

const tableData = [
    {
        description: "Solar Panel - Mono 400W",
        quantity: 10,
        unitPrice: 250,
        total: 2500,
    },
    {
        description: "Inverter - 5kW Grid Tie",
        quantity: 2,
        unitPrice: 900,
        total: 1800,
    }
];

function Invoice() {
    return (<>
            <Document>
                <Page size="A4" style={styles.page}>
                    <View style={styles.header}>
                        <View>
                            <Text style={[styles.title,styles.bold]}>INVOICE</Text>
                            <Text>INVOICE #123-123-123</Text>
                        </View>
                        <View style={styles.spaceY}>
                            <Text style={styles.textBold}>Solar Offset</Text>
                            <Text>21 Regent Court</Text>
                            <Text>Sheffield, UK</Text>
                        </View>
                    </View>

                    <View style={styles.spaceY}>
                        <Text style={[styles.billTo, styles.textBold]}>Bill To:</Text>
                        <Text>Client Name</Text>
                        <Text>Client Address</Text>
                        <Text>City, State ZIP</Text>
                    </View>


                </Page>
            </Document>
        </>);
}

export default Invoice;
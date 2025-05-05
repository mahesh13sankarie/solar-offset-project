import { Document, Page, View, StyleSheet, Text } from "@react-pdf/renderer";

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
        display: "table",
        width: "auto",
        marginVertical: 20,
        borderStyle: "solid",
        borderWidth: 1,
        borderRightWidth: 0,
        borderBottomWidth: 0,
    },
    tableRow: {
        flexDirection: "row",
    },
    tableCol: {
        width: "33.33%",
        borderStyle: "solid",
        borderWidth: 1,
        borderLeftWidth: 0,
        borderTopWidth: 0,
        padding: 6,
    },
    tableHeader: {
        backgroundColor: "#e5e5e5",
        fontWeight: "bold",
    },
    totals: {
        display: "flex",
        alignItems: "flex-end",
        marginTop: 120,
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
    },
];

const totalData = [
    { label: "Subtotal", value: "$4300.00" },
    { label: "Tax (0%)", value: "$215.00" },
    { label: "Total", value: "$4515.00" },
];

function Invoice() {
    return (
        <>
            <Document>
                <Page size="A4" style={styles.page}>
                    <View style={styles.header}>
                        <View>
                            <Text style={[styles.title, styles.bold]}>INVOICE</Text>
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
                    <View style={styles.table}>
                        {/* Table Header */}
                        <View style={styles.tableRow}>
                            <Text style={[styles.tableCol, styles.tableHeader]}>Description</Text>
                            <Text style={[styles.tableCol, styles.tableHeader]}>Quantity</Text>
                            <Text style={[styles.tableCol, styles.tableHeader]}>Unit Price</Text>
                            <Text style={[styles.tableCol, styles.tableHeader]}>Total</Text>
                        </View>

                        {/* Table Body */}
                        {tableData.map((item, index) => (
                            <View style={styles.tableRow} key={index}>
                                <Text style={styles.tableCol}>{item.description}</Text>
                                <Text style={styles.tableCol}>{item.quantity}</Text>
                                <Text style={styles.tableCol}>${item.unitPrice.toFixed(2)}</Text>
                                <Text style={styles.tableCol}>${item.total.toFixed(2)}</Text>
                            </View>
                        ))}
                    </View>
                    <View style={styles.totals}>
                        <View
                            style={{
                                minWidth: "256px",
                            }}
                        >
                            {totalData.map((item) => (
                                <View
                                    style={{
                                        flexDirection: "row",
                                        justifyContent: "space-between",
                                        marginBottom: "8px",
                                    }}
                                >
                                    <Text style={item.label === "Total" ? styles.textBold : {}}>
                                        {item.label}
                                    </Text>
                                    <Text style={item.label === "Total" ? styles.textBold : {}}>
                                        {item.value}
                                    </Text>
                                </View>
                            ))}
                        </View>
                    </View>
                </Page>
            </Document>
        </>
    );
}

function PaymentInvoice({ userId, countryPanelId, amount, total, quantity, panelName, paymentType, paymentMethodId }) {
    const unitPrice = quantity ? amount / quantity : 0;
    const tax = amount * 0.00;
    return (
        <Document>
            <Page size="A4" style={styles.page}>
                <View style={styles.header}>
                    <View>
                        <Text style={[styles.title, styles.textBold]}>INVOICE</Text>
                        <Text>User ID: {userId}</Text>
                        <Text>Invoice ID: INV-{userId}-{countryPanelId}</Text>
                    </View>
                    <View style={styles.spaceY}>
                        <Text style={styles.textBold}>Solar Offset</Text>
                        <Text>21 Regent Court</Text>
                        <Text>Sheffield, UK</Text>
                    </View>
                </View>

                <View style={styles.spaceY}>
                    <Text style={[styles.billTo, styles.textBold]}>Payment Details:</Text>
                </View>

                <View style={styles.spaceY}>
                    <Text>Panel Name: {panelName}</Text>
                    <Text>Quantity: {quantity}</Text>
                    <Text>Unit Price: ${unitPrice.toFixed(2)}</Text>
                </View>

                <View style={styles.table}>
                    <View style={styles.tableRow}>
                        <Text style={[styles.tableCol, styles.tableHeader]}>Payment Method</Text>
                        <Text style={[styles.tableCol, styles.tableHeader]}>Payment Type</Text>
                        <Text style={[styles.tableCol, styles.tableHeader]}>Amount</Text>
                    </View>
                    <View style={styles.tableRow}>
                        <Text style={styles.tableCol}>{paymentMethodId}</Text>
                        <Text style={styles.tableCol}>{paymentType}</Text>
                        <Text style={styles.tableCol}>${amount.toFixed(2)}</Text>
                    </View>
                </View>

                <View style={styles.totals}>
                    <View style={{ minWidth: "256px" }}>
                        <View style={{ flexDirection: "row", justifyContent: "space-between", marginBottom: "8px" }}>
                            <Text>Subtotal</Text>
                            <Text>${amount.toFixed(2)}</Text>
                        </View>
                        <View style={{ flexDirection: "row", justifyContent: "space-between", marginBottom: "8px" }}>
                            <Text>Tax (0%)</Text>
                            <Text>${tax.toFixed(2)}</Text>
                        </View>
                        <View style={{ flexDirection: "row", justifyContent: "space-between", marginBottom: "8px" }}>
                            <Text style={styles.textBold}>Total</Text>
                            <Text style={styles.textBold}>${total.toFixed(2)}</Text>
                        </View>
                    </View>
                </View>
            </Page>
        </Document>
    );
}

export { PaymentInvoice };

export default Invoice;

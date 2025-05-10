import React, { useEffect, useState } from "react";
import { api } from "../../api";

export default function EnquiryDashboard() {
    const [enquiries, setEnquiries] = useState([]);

    useEffect(() => {
        api.auth
            .getEnquiries()
            .then((res) => {
                setEnquiries(res.data);
            })
            .catch((err) => console.error("Error fetching enquiries:", err));
    }, []);
    console.log(enquiries);

    return (
        <div className="container mt-4 mb-5">
            <h2>Enquiries</h2>
            {enquiries.map((enquiry) => (
                <div key={enquiry.id} className="card mb-3">
                    <div className="card-header d-flex justify-content-between">
                        <div>
                            <strong>{enquiry.name}</strong> (<em>{enquiry.email}</em>)
                        </div>
                        <small>{new Date(enquiry.createdAt).toLocaleString()}</small>
                    </div>
                    <div className="card-body">
                        <h5 className="card-title">{enquiry.subject}</h5>
                        <details>
                            <summary style={{ cursor: "pointer" }}>View Message</summary>
                            <p className="card-text mt-2">{enquiry.body}</p>
                        </details>
                    </div>
                </div>
            ))}
        </div>
    );
}

import React, { useEffect, useState } from "react";
import axios from "axios";

export default function TopDonors() {
    const [topUsers, setTopUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        async function fetchTopUsers() {
            try {
                const token = localStorage.getItem("token");
                if (!token) {
                    console.error("No token found, please login first");
                    setError("Authentication required. Please login.");
                    return;
                }
                const res = await axios.get("http://localhost:8000/api/v1/transaction/staff", {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                });
                const data = res.data;

                const userDonations = {};

                data.forEach((transaction) => {
                    const { user, amount } = transaction;
                    if (userDonations[user]) {
                        userDonations[user] += amount;
                    } else {
                        userDonations[user] = amount;
                    }
                });

                const sortedUsers = Object.entries(userDonations)
                    .sort((a, b) => b[1] - a[1])
                    .map(([user, amount]) => ({ user, amount }));

                setTopUsers(sortedUsers);
                setLoading(false);
            } catch (err) {
                console.error("Error fetching top donors:", err);
                setError("Failed to load top donors");
                setLoading(false);
            }
        }

        fetchTopUsers();
    }, []);

    if (loading) {
        return (
            <div className="card p-3 m-3 shadow" style={{ width: "600px" }}>
                Loading Top Donors...
            </div>
        );
    }

    if (error) {
        return (
            <div className="card p-3 m-3 shadow" style={{ width: "600px" }}>
                {error}
            </div>
        );
    }

    return (
        <div className="card p-3 m-3 shadow w-100">
            <h5 className="mb-4 text-center">Top Donors Report</h5>
            <div className="table-responsive">
                <table className="table table-striped table-hover table-sm ">
                    <thead className="table-dark">
                        <tr>
                            <th>#</th>
                            <th>Email</th>
                            <th>Amount Donated ($)</th>
                        </tr>
                    </thead>
                    <tbody>
                        {topUsers.map((user, index) => (
                            <tr key={index}>
                                <td>{index + 1}</td>
                                <td>{user.user}</td>
                                <td>${user.amount.toFixed(2)}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

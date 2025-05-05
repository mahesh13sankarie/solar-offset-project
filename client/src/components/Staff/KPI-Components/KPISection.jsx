import React from 'react';
import { FaSun, FaDonate, FaRecycle, FaUsers } from 'react-icons/fa';

const KPIWidget = ({ icon, label, value, bg }) => {
    return (
        <div className={`card shadow-sm text-dark ${bg} mb-3`} style={{ minWidth: '220px', borderRadius: '16px', backgroundColor: bg, border: 'none' }}>
            <div className="card-body d-flex align-items-center">
                <div className="fs-1 me-3 d-flex align-items-center justify-content-center text-white bg-dark rounded-circle" style={{ width: '50px', height: '50px' }}>
                    {icon}
                </div>
                <div>
                    <div className="fs-4 fw-semibold mb-1">{value}</div>
                    <div className="small opacity-75">{label}</div>
                </div>
            </div>
        </div>
    );
};

const KPISection = ({ stats }) => {
    return (
        <div className="d-flex justify-content-center text-center flex-wrap gap-4">
            <KPIWidget
                icon={<FaSun />}
                label="Total Panels Funded"
                value={stats.totalPanels || 0}
                bg="#e0f7fa"
            />
            <KPIWidget
                icon={<FaDonate />}
                label="Total Amount Donated"
                value={`£${stats.totalAmount?.toLocaleString() || '0'}`}
                bg="#e8f5e9"
            />
            <KPIWidget
                icon={<FaRecycle />}
                label="Total Carbon Offset Per Year"
                value={`${stats.totalCarbon || 0} kg CO₂`}
                bg="#fff3e0"
            />
            <KPIWidget
                icon={<FaUsers />}
                label="Total Active Users"
                value={`${stats.activeDonors || 0} `}
                bg="#ede7f6"
            />
        </div>
    );
};

export default KPISection;
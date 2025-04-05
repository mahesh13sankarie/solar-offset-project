import React from "react";

export default function StaffNavBar() {
    return (
     <>
        <div className="side-nav">
                <nav className="navbar navbar-expand-sm text-light bg-dark">
                    <a href="/" className="navbar-brand text-light" style={{textDecoration: 'none',marginLeft: '20px'}} >
                        <i className="bi bi-arrow-left-circle text-decoration-none" > Home</i>
                    </a>
                    <div className="container-fluid " >
                    <ul className="navbar-nav d-flex justify-content-evenly " style={{width:'100%'}} >

                        <li>Report Generate</li>
                        <li>Statistics</li>
                    </ul>
                    </div>
                </nav>
        </div>
     </>
    )
}
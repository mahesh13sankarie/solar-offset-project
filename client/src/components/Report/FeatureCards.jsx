import React, { useState, useEffect } from "react";

export  function CountriesSelected(props) {
    const [countries, setCountries] = useState([]);

    useEffect(() => {
        // Dummy data for countries
        const dummyCountries = [
            { name: "United States" },
            { name: "Germany" },
            { name: "India" },
            { name: "Brazil" },
            { name: "Australia" },
            { name: "Canada" },
            { name: "Japan" },
            { name: "China" }
        ];

        setCountries(dummyCountries);  // Set the countries to the state
    }, []);  // Empty dependency array to run once on component mount

    return (
        <div className="container mt-4">
            <div className="card shadow-lg">
                <div className="card-header text-center">
                    <h3 className="card-title">Countries Selected by User</h3>
                    <p className="text-muted">Total Countries: {countries.length}</p>
                </div>

                <div className="card-body">
                    <ul className="list-group list-group-flush mt-3">
                        {countries.map((country, index) => (
                            <li key={index} className="list-group-item d-flex justify-content-between">
                                <span>{country.name}</span>
                            </li>
                        ))}
                    </ul>
                </div>

                <div className="card-footer text-center">
                    <p className="text-muted">Displaying {countries.length} selected countries</p>
                </div>
            </div>
        </div>
    );
}

export  function PanelsBought(){
    return (
       <>
           <div className="container mt-4">
               <div className="card shadow-lg">
                   <div className="card-header text-center">
                       <h3 className="card-title">Panels Bought</h3>
                       <p className="text-muted">Total Panels: </p>
                   </div>

                   <div className="card-body">
                       <ul className="list-group list-group-flush mt-3">

                       </ul>
                   </div>

                   <div className="card-footer text-center">
                       <p className="text-muted">Displaying total panels</p>
                   </div>
               </div>
           </div>
       </>
    )
}


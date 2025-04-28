import React, { useState, useEffect } from "react";

export  function CountriesSelected(props) {
    const [countries, setCountries] = useState([]);

    useEffect(() => {
        fetch("http://localhost:8000/api/v1/transaction/staff")
            .then(response => response.json())
            .then(data => {
                const countryCountMap = {};
                data.forEach(transaction => {
                    const country = transaction.country;
                    countryCountMap[country] = (countryCountMap[country] || 0) + 1;
                });

                const sortedCountries = Object.entries(countryCountMap)
                    .sort((a, b) => b[1] - a[1])
                    .map(([name, count]) => ({ name, count }));

                setCountries(sortedCountries);
            })
            .catch(error => {
                console.error("Error fetching countries:", error);
            });
    }, []);

    return (
        <div className="container mt-4">
            <div className="card shadow-lg">
                <div className="card-header text-center">
                    <h3 className="card-title">Countries Selected by User</h3>
                    <p className="text-muted">Total Countries: {countries.length}</p>
                </div>

                <div className="card-body">
                    <table className="table table-striped table-bordered">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Country Name</th>
                                <th>Selections</th>
                            </tr>
                        </thead>
                        <tbody>
                            {countries.map((country, index) => (
                                <tr key={index}>
                                    <td>{index + 1}</td>
                                    <td>{country.name}</td>
                                    <td>{country.count}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>

                <div className="card-footer text-center">
                    <p className="text-muted">Displaying {countries.length} selected countries</p>
                    <button className="btn btn-primary mt-2">Generate PDF</button>
                </div>
            </div>
        </div>
    );
}

export  function PanelsBought(){
    return (
       <div className="container mt-4">
           <div className="card shadow-lg">
               <div className="card-header text-center">
                   <h3 className="card-title">Panels Bought</h3>
                   <p className="text-muted">Total Panels: </p>
               </div>

               <div className="card-body">
                   <table className="table table-striped table-bordered">
                       <thead>
                           <tr>
                               <th>#</th>
                               <th>Panel Name</th>
                               <th>Quantity</th>
                               <th>Total Cost</th>
                           </tr>
                       </thead>
                       <tbody>
                           {/* Future panel purchase data goes here */}
                       </tbody>
                   </table>
               </div>

               <div className="card-footer text-center">
                   <p className="text-muted">Displaying total panels</p>
                   <button className="btn btn-success mt-2">Download Report</button>
               </div>
           </div>
       </div>
    )
}

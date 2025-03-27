import React, {useEffect, useState} from 'react'
import Navbar from "./Navbar.jsx";
import {Link} from "react-router-dom";

const InstallationCost = () => {
    const [data, setData] = useState([]);
   // const[country, setCountry] = useState("");
   // const[description, setDescription] = useState("");
    //const[solarpanel, setSolarPanel] = useState[""]
    useEffect(()=>{
        const fetchData = async () => {
            try{
                const response = await fetch("http://localhost:3000/singlecountry");
                if (!response.ok) {
                    throw new Error("Failed to fetch data");
                }
                const jsonData = await response.json();
                setData(jsonData);
                //setCountry(jsonData[0].country);
                //setDescription(jsonData[0].description);
            }catch (error) {
                setError(error.message);
            }
        }

        fetchData();
    },[])

    return(
        <div>
            <section>
                <Navbar/>
            </section>
            <section id="installationcost" className="about section">


                <div className="container">
                    {data.map((country, countryIndex) => (

                        <div key={countryIndex}>
                            <a href="/SolarComparison"><button className="btn btn-link">
                                <i className="bi bi-arrow-left-circle"></i> Back
                            </button></a>
                            <div className="row gy-4">

                                <div className="container section-title" data-aos="fade-up">
                                    <h2>{country.country}</h2>
                                </div>
                                <div>
                                    <p>{country.description}</p>
                                </div>
                                <h2></h2>
                                <div className="container section-title" data-aos="fade-up">
                                    <h2>Installation Cost</h2>
                                </div>
                                <table className="table table-bordered table-striped">
                                    <thead>
                                    <tr>
                                        <td width="30%">
                                            Type
                                        </td>
                                        <td>
                                            Price ({country.currency})
                                        </td>
                                        <td>
                                            Installation Cost ({country.currency})
                                        </td>
                                        <td>
                                            Efficiency (%)
                                        </td>
                                        <td>
                                            Warranty (Years)
                                        </td>
                                    </tr>
                                    </thead>
                                    <tbody id="carbon-comparison">
                                    {country.solar_panels?.map((item, index) => (
                                        <tr key={index}>
                                            <td>{item.type}</td>
                                            <td>{item.panel_cost}</td>
                                            <td>{item.installation_cost}</td>
                                            <td>{item.efficiency}</td>
                                            <td>{item.warranty}</td>

                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                                <h5></h5>
                                <div className="container section-title">
                                    <Link to="/solarComparison" className="btn-get-started"><i className="bi bi-gift-fill"></i> Donate Now </Link>
                                </div>
                            </div>
                        </div>
                    ))}

                </div>

            </section>
        </div>
    );
};

export default InstallationCost
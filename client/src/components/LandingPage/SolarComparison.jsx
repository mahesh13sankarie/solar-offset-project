import Navbar from "./Navbar.jsx";
import React, {useEffect, useState} from "react";


const SolarComparison = () => {

    const [data, setData] = useState([]);
    const [search, setSearch] = useState("");

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await fetch("http://localhost:3000/countries"); // Fake API URL
                if (!response.ok) {
                    throw new Error("Failed to fetch data");
                }
                const jsonData = await response.json();
                setData(jsonData);
            } catch (error) {
                setError(error.message);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    const handleSearch = (event) => {
        setSearch(event.target.value);
    };

    const filteredData = data.filter((item) =>
        item.country.toLowerCase().includes(search.toLowerCase())
    );

    const BarChart = () =>{
        const [options,setOptions] = useState({
            title:{
                text:"Country's Electricity Comparison"
            }
        })
    }

    return(
        <div>
            <section>
                <Navbar/>
            </section>

            <section id="comparison" className="about section">
                <div className="container section-title" data-aos="fade-up">
                    <h2>Country Comparison</h2>
                </div>

                <div className="container">
                    <div className="row gy-4">
                        <input
                            type="text"
                            className="form-control mb-3 w-25"
                            placeholder="Search country..."
                            value={search}
                            onChange={handleSearch}
                        />
                        <table className="table table-bordered table-striped">
                            <thead>
                            <tr>
                                <td width="30%">
                                    Country
                                </td>
                                <td>
                                    Carbon Emissions
                                </td>
                                <td>
                                    Electricity Consumption
                                </td>
                                <td>
                                    Population
                                </td>
                                <td>
                                    Action
                                </td>
                            </tr>
                            </thead>
                            <tbody id="carbon-comparison">
                            {filteredData.map((item) => (
                                <tr key={item.id}>
                                    <td>{item.country}</td>
                                    <td>{item.carbon_emission}</td>
                                    <td>{item.electricity_consumption}</td>
                                    <td>{item.population}</td>
                                    <td>{<button class=' btn btn-sm btn-outline-success'>Detail</button>}<br/>
                                        <br/>{<button class='btn btn-sm btn-outline-info'>Donate</button>}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                </div>

                <div className="container">
                    <hr/>
                </div>
            </section>
        </div>

    );
};

export default SolarComparison;
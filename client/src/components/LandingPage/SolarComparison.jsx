import Navbar from "./Navbar.jsx";
import React, {useEffect, useState} from "react";
import { Bar } from "react-chartjs-2";
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from "chart.js";

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);
const SolarComparison = () => {

    const [data, setData] = useState([]);
    const [search, setSearch] = useState("");
    const [chartData, setChartData] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await fetch("http://localhost:3000/countries");
                if (!response.ok) {
                    throw new Error("Failed to fetch data");
                }
                const jsonData = await response.json();
                setData(jsonData);
                const chartLabels = jsonData.map(item=>item.country);
                const carbon = jsonData.map(item=>item.carbon_emission);
                const electricity = jsonData.map(item=>item.electricity_consumption);

                setChartData({
                    labels: chartLabels,
                    datasets: [
                        {
                            label: "Carbon Emissions",
                            data: carbon,
                            backgroundColor: "rgba(255, 99, 132, 0.5)",
                            borderColor: "rgba(255, 99, 132, 1)",
                            borderWidth: 1,
                        },
                        {
                            label: "Electricity Consumption",
                            data: electricity,
                            backgroundColor: "rgba(54, 162, 235, 0.5)",
                            borderColor: "rgba(54, 162, 235, 1)",
                            borderWidth: 1,
                        }
                    ]
                });
            } catch (error) {
                setError(error.message);
            }
        };


        fetchData();
    }, []);

    const filteredData = data.filter((item) =>
        item.country.toLowerCase().includes(search.toLowerCase())
    );

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
                            onChange={(e) => setSearch(e.target.value)}
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
                                    <td>{<button className=' btn btn-sm btn-outline-success'>Detail</button>}<br/>
                                        <br/>{<button className='btn btn-sm btn-outline-info'>Donate</button>}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                </div>

                <div className="container">
                    <hr/>
                    <div className="container section-title" data-aos="fade-up">
                        <h2>Carbon Footprint</h2>
                    </div>
                    <div className="row gy-4">


                        {chartData ? (
                            <Bar data={chartData} options={{responsive: true, maintainAspectRatio: false}}/>
                        ) : (
                            <p>Loading chart...</p>
                        )}
                    </div>
                </div>
            </section>
        </div>

    );
};

export default SolarComparison;
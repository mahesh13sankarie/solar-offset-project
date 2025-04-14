import React, { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import Navbar from "./Navbar.jsx";

const InstallationCost = () => {
	const [country, setCountry] = useState(null);
	const [error, setError] = useState(null);
	const { countryCode } = useParams();

	useEffect(() => {
		const fetchData = async () => {
			try {
				const response = await fetch(`http://localhost:8000/api/v1/countries/${countryCode}`);
				if (!response.ok) throw new Error("Failed to fetch data");
				const jsonData = await response.json();
				setCountry(jsonData);
			} catch (error) {
				setError(error.message);
			}
		};

		fetchData();
	}, [countryCode]);

	return (
		<div>
			<Navbar />
			<section className="container mt-4">
				{/* Back Button */}
				<Link to="/SolarComparison">
					<button className="btn btn-link mb-3">
						<i className="bi bi-arrow-left-circle"></i> Back
					</button>
				</Link>

				{/* Error Message */}
				{error && <div className="alert alert-danger">{error}</div>}

				{/* Country Data */}
				{country && (
					<>
						{/* Country Overview */}
						<div className="card shadow-sm p-4 mb-5">
							<h2 className="mb-3">{country.country}</h2>
							<p className="text-muted">{country.description}</p>
							<div className="row mt-3">
								<div className="col-md-4">
									<strong>Carbon Emissions:</strong> {country.carbonEmissions.toLocaleString()} tCO₂
								</div>
								<div className="col-md-4">
									<strong>Electricity Availability:</strong>{" "}
									{country.electricityAvailability.toLocaleString()} MWh
								</div>
								<div className="col-md-4">
									<strong>Solar Potential:</strong> {country.solarPowerPotential}%
								</div>
								<div className="col-md-4 mt-3">
									<strong>Renewable Energy:</strong> {country.renewablePercentage}%
								</div>
								<div className="col-md-4 mt-3">
									<strong>Population:</strong> {country.population} million
								</div>
							</div>
						</div>

						{/* Panel Cards */}
						<h4 className="mb-4">Available Solar Panels</h4>
						<div className="row">
							{country.solarPanels?.map((panel, i) => (
								<div className="col-md-6 col-lg-4 mb-4" key={i}>
									<div className="card h-100 shadow-sm d-flex flex-column">
										<div className="card-body d-flex flex-column">
											<h5 className="card-title">{panel.panelName}</h5>
											<p className="text-muted" style={{ fontSize: "0.9rem" }}>
												{panel.description}
											</p>
											<ul className="list-group list-group-flush mt-3 mb-4">
												<li className="list-group-item">
													<strong>Installation:</strong> £{panel.installationCost ?? "-"}
												</li>
												<li className="list-group-item">
													<strong>Production:</strong> {panel.productionPerPanel ?? "-"} W
												</li>
												<li className="list-group-item">
													<strong>Efficiency:</strong> {panel.efficiency ?? "-"}%
												</li>
												<li className="list-group-item">
													<strong>Warranty:</strong> {panel.warranty ?? "-"} years
												</li>
											</ul>
											<div className="mt-auto">
												<Link
													to={`/Payment/${countryCode}?panelIndex=${i}`}
													className="btn btn-success w-100"
												>
													<i className="bi bi-heart-fill"></i> Donate
												</Link>
											</div>
										</div>
									</div>
								</div>
							))}
						</div>
					</>
				)}
			</section>
		</div>
	);
};

export default InstallationCost;

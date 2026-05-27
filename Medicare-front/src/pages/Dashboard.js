import React, { useState } from "react";
import MapView from "../components/MapView";
import PlantsLayer from "../components/PlantsLayer";
import AirCard from "../components/AirCard";
import LegendCard from "../components/LegendCard";
import WeatherCard from "../components/WeatherCard";
import ClickRiskCard from "../components/ClickRiskCard";
import ClickRiskHistoryCard from "../components/ClickRiskHistoryCard";
import "../styles/Dashboard.css";

export default function Dashboard() {
  const [center, setCenter] = useState([48.85, 2.35]);

  const [latInput, setLatInput] = useState("48.85");
  const [lonInput, setLonInput] = useState("2.35");

  const handleGoToPoint = () => {
    const lat = parseFloat(latInput);
    const lon = parseFloat(lonInput);

    if (isNaN(lat) || isNaN(lon)) {
      alert("Veuillez entrer une latitude et une longitude valides.");
      return;
    }

    setCenter([lat, lon]);
  };

  return (
    <div className="dashboard-container">
      <div className="map-box">
        <MapView onPositionChange={setCenter} center={center}>
          <PlantsLayer center={center} radius={5} />
        </MapView>
      </div>

      <div className="info-column">
        <div className="card">
          <h3>Aller à un point</h3>

          <p>Latitude</p>
          <input
            type="text"
            value={latInput}
            onChange={(e) => setLatInput(e.target.value)}
          />

          <p>Longitude</p>
          <input
            type="text"
            value={lonInput}
            onChange={(e) => setLonInput(e.target.value)}
          />

          <br /><br />
          <button onClick={handleGoToPoint}>Aller</button>
        </div>

        <AirCard center={center} />
        <WeatherCard center={center} />
        <ClickRiskCard center={center} />
        <ClickRiskHistoryCard center={center} />
        <LegendCard />
      </div>
    </div>
  );
}
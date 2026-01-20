import React, { useState } from "react";
import MapView from "../components/MapView";
import PlantsLayer from "../components/PlantsLayer";
import AirCard from "../components/AirCard";
import LegendCard from "../components/LegendCard";
import WeatherCard from "../components/WeatherCard";
import "../styles/Dashboard.css";

export default function Dashboard() {
  const [center, setCenter] = useState(null);

  return (
    <div className="dashboard-container">
      <div className="map-box">
        <MapView onPositionChange={setCenter}>
          <PlantsLayer center={center} radius={5} />
        </MapView>
      </div>

      <div className="info-column">
        <AirCard center={center} />
        <WeatherCard center={center} />
        <LegendCard />{" "}
      </div>
    </div>
  );
}

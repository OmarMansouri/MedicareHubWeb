import React from "react";
import MapView from "../components/MapView";
import PlantsLayer from "../components/PlantsLayer";
import AirCard from "../components/AirCard";
import WeatherCard from "../components/WeatherCard";
import "../styles/Dashboard.css";

export default function Dashboard() {
  return (
    <div className="dashboard-container">
      <div className="map-box">
        <MapView>
          <PlantsLayer />
        </MapView>
      </div>

      <div className="info-column">
        <AirCard />
        <WeatherCard />
      </div>
    </div>
  );
}

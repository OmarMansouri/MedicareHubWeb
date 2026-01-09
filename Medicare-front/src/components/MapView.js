import React from "react";
import { MapContainer, TileLayer } from "react-leaflet";
import "leaflet/dist/leaflet.css";

export default function MapView({ children }) {
  const center = [20, 0]; // vue globale
  return (
    <MapContainer
      center={center}
      zoom={2}
      style={{ height: "100%", width: "100%" }}
    >
      <TileLayer
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">
          OpenStreetMap
        </a> contributors'
      />
      {children /* <PlantsLayer/> ou autres */}
    </MapContainer>
  );
}

import React, { useEffect, useState } from "react";
import { MapContainer, TileLayer, Marker, Popup, useMap } from "react-leaflet";
import L from "leaflet";
import "leaflet/dist/leaflet.css";

const DefaultIcon = L.icon({
  iconUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png",
  shadowUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png",
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
});
L.Marker.prototype.options.icon = DefaultIcon;


function RecenterMap({ position }) {
  const map = useMap();
  if (position) map.setView(position, 13); 
  return null;
}

export default function MapView({ children }) {
  const [position, setPosition] = useState(null); 
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!navigator.geolocation) {
      setError("La géolocalisation n’est pas prise en charge par ce navigateur.");
      return;
    }

    navigator.geolocation.getCurrentPosition(
      (pos) => {
        const { latitude, longitude } = pos.coords;
        setPosition([latitude, longitude]);
        setError(null);
      },
      (err) => {
        console.error("Erreur géolocalisation :", err.message);
        setError("Impossible de récupérer votre position.");
      }
    );
  }, []);

  const defaultCenter = [20, 0];

  return (
    <MapContainer
      center={position || defaultCenter}
      zoom={position ? 13 : 2}
      style={{ height: "100%", width: "100%" }}
    >
      <TileLayer
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">
          OpenStreetMap
        </a> contributors'
      />

      {}
      <RecenterMap position={position} />

      {}
      {position && (
        <Marker position={position}>
          <Popup>
            Vous êtes ici :<br />
            Lat : {position[0].toFixed(4)}<br />
            Lng : {position[1].toFixed(4)}
          </Popup>
        </Marker>
      )}

      {}
      {children}

      {}
      {error && (
        <div
          style={{
            position: "absolute",
            top: 10,
            left: "50%",
            transform: "translateX(-50%)",
            backgroundColor: "rgba(255,255,255,0.9)",
            padding: "6px 10px",
            borderRadius: "6px",
            color: "red",
          }}
        >
          {error}
        </div>
      )}
    </MapContainer>
  );
}
import React, { useEffect, useState } from "react";
import {
  MapContainer,
  TileLayer,
  Marker,
  Popup,
  useMap,
  useMapEvents,
} from "react-leaflet";
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

function ClickHandler({ onClick }) {
  useMapEvents({
    click(e) {
      onClick([e.latlng.lat, e.latlng.lng]);
    },
  });
  return null;
}

export default function MapView({ children, onPositionChange }) {
  const defaultCenter = [48.85, 2.35];

  const [position, setPosition] = useState(defaultCenter);

  useEffect(() => {
    onPositionChange?.(defaultCenter);
  }, [onPositionChange]);

  const handleClick = (coords) => {
    setPosition(coords);
    onPositionChange?.(coords);
  };

  return (
    <MapContainer
      center={position}
      zoom={13}
      style={{ height: "100%", width: "100%" }}
    >
      <TileLayer
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">
          OpenStreetMap
        </a> contributors'
      />

      <RecenterMap position={position} />
      <ClickHandler onClick={handleClick} />

     
      {position && (
        <Marker position={position}>
          <Popup>
            <b>Position sélectionnée</b>
            <br />
            Latitude : {position[0].toFixed(4)} <br />
            Longitude : {position[1].toFixed(4)}
          </Popup>
        </Marker>
      )}


      {children}
    </MapContainer>
  );
}
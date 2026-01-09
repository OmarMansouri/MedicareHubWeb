import React, { useEffect, useState } from "react";
import "../styles/Maps.css";
import L from "leaflet";
import {
  MapContainer,
  TileLayer,
  Marker,
  Popup,
  LayerGroup,
  Circle,
} from "react-leaflet";

const DefaultIcon = L.icon({
  iconUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png",
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
});

L.Marker.prototype.options.icon = DefaultIcon;

const center = [48.805652, 2.422507]; 

export default function Maps() {
  const [aqiData, setAqiData] = useState(null);

  useEffect(() => {
    const [lat, lon] = center;
    fetch(`http://localhost:8081/api/air/coords?lat=${lat}&lon=${lon}`)
      .then((res) => res.json())
      .then((data) => {
        if (data.status === "ok") {
          setAqiData(data.data);
        } else {
          console.error("Erreur API :", data);
        }
      })
      .catch((err) => console.error("Erreur réseau :", err));
  }, []);

  const getColor = (aqi) => {
    if (aqi <= 50) return "green";
    if (aqi <= 100) return "yellow";
    if (aqi <= 150) return "orange";
    if (aqi <= 200) return "red";
    if (aqi <= 300) return "purple";
    return "maroon";
  };

  const getCategory = (aqi) => {
    if (aqi <= 50) return "🟢 Bonne";
    if (aqi <= 100) return "🟡 Modérée";
    if (aqi <= 150) return "🟠 Mauvaise (sensibles)";
    return "🔴 Mauvaise";
  };

  return (
    <MapContainer
      center={center}
      zoom={12}
      style={{ height: "100vh", width: "100%" }}
    >
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />

      {aqiData && (
        <>
          {}
          <Marker position={center}>
            <Popup>
              <strong>{aqiData.city.name}</strong>
              <br />
              AQI : <strong>{aqiData.aqi}</strong>
              <br />
              Qualité : {getCategory(aqiData.aqi)}
              <br />
              Dernière maj : {aqiData.time.s}
            </Popup>
          </Marker>

          {}
          <LayerGroup>
            <Circle
              center={center}
              pathOptions={{
                fillColor: getColor(aqiData.aqi),
                color: getColor(aqiData.aqi),
              }}
              radius={1000}
              fillOpacity={0.25}
            />
            <Circle
              center={center}
              pathOptions={{
                fillColor: getColor(aqiData.aqi),
                color: getColor(aqiData.aqi),
              }}
              radius={250}
              stroke={false}
              fillOpacity={0.4}
            />
          </LayerGroup>
        </>
      )}
    </MapContainer>
  );
}
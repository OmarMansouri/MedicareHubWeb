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
  const [weather, setWeather] = useState(null);

  useEffect(() => {
    const [lat, lon] = center;

    fetch(`http://172.31.250.86:3000/api/air/coords?lat=${lat}&lon=${lon}`)
      .then((res) => res.json())
      .then((data) => {
        if (data.status === "ok") {
          setAqiData(data.data);
        } else {
          console.error("Erreur API Air :", data);
        }
      })
      .catch((err) => console.error("Erreur réseau Air :", err));

    fetch(`http://172.31.250.86:3000/api/weather/coords?lat=${lat}&lon=${lon}`)
      .then((res) => res.json())
      .then((data) => {
        if (data.current) {
          setWeather(data.current);
        } else {
          console.error("Erreur API Météo :", data);
        }
      })
      .catch((err) => console.error("Erreur réseau Météo :", err));
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
    if (aqi <= 50) return " Bonne";
    if (aqi <= 100) return "🟡 Modérée";
    if (aqi <= 150) return "🟠 Mauvaise (sensibles)";
    return "🔴 Mauvaise";
  };

  const getWeatherDesc = (code) => {
    const map = {
      0: "Ciel clair ☀️",
      1: "Principalement clair 🌤️",
      2: "Partiellement nuageux ⛅",
      3: "Couvert ☁️",
      45: "Brouillard 🌫️",
      48: "Brouillard givrant 🧊",
      51: "Bruine légère 🌦️",
      61: "Pluie faible 🌧️",
      63: "Pluie modérée 🌧️",
      65: "Pluie forte 🌧️",
      80: "Averses 🌦️",
      95: "Orage ⛈️",
    };
    return map[code] || "Inconnu";
  };

  return (
    <div className="dashboard-container">
      {}
      <div className="map-box">
        <MapContainer
          center={center}
          zoom={12}
          style={{ height: "100%", width: "100%" }}
        >
          <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">
            OpenStreetMap</a> contributors'
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
      </div>

      {}
      <div className="info-card">
        {}
        <h3>Qualité de l’air</h3>
        {aqiData ? (
          <>
            <p>
              <strong>Ville :</strong> {aqiData.city.name}
            </p>
            <p>
              <strong>AQI :</strong>{" "}
              <span style={{ color: getColor(aqiData.aqi) }}>
                {aqiData.aqi}
              </span>
            </p>
            <p>
              <strong>Qualité :</strong> {getCategory(aqiData.aqi)}
            </p>
            <p>
              <strong>Dernière maj :</strong> {aqiData.time.s}
            </p>
          </>
        ) : (
          <p>Chargement des données de qualité de l’air...</p>
        )}

        <hr />

        {}
        <h3>Météo</h3>
        {weather ? (
          <>
            <p>
              <strong>Température :</strong> {weather.temperature_2m} °C 🌡️
            </p>
            <p>
              <strong>Vent :</strong> {weather.wind_speed_10m} km/h 💨
            </p>
            <p>
              <strong>Conditions :</strong>{" "}
              {getWeatherDesc(weather.weather_code)}
            </p>
          </>
        ) : (
          <p>Chargement des données météo...</p>
        )}
      </div>
    </div>
  );
}

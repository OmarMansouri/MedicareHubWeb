import React, { useEffect, useState } from "react";
const API_BASE =
  window.location.hostname === "localhost" || window.location.hostname === "127.0.0.1"
    ? "http://localhost:8081"      
    : "http://172.31.250.86:8081"; 
export default function WeatherCard({ center }) {
  const [weather, setWeather] = useState(null);
  const [loading, setLoading] = useState(false);

  const desc = (code) =>
    ({
      0: "Ciel clair ",
      1: "Principalement clair ",
      2: "Partiellement nuageux ",
      3: "Couvert ",
      45: "Brouillard ",
      48: "Brouillard givrant ",
      51: "Bruine ",
      61: "Pluie faible ",
      63: "Pluie modérée ",
      65: "Pluie forte ",
      71: "Neige légère ",
      80: "Averses ",
      95: "Orage ",
    }[code] || "Inconnu");

  useEffect(() => {
    if (!center) return;

    const [lat, lon] = center;
    setLoading(true);

    fetch(`${API_BASE}/api/weather/coords?lat=${lat}&lon=${lon}`)
      .then((res) => res.json())
      .then((data) =>
        data.current ? setWeather(data.current) : setWeather(null)
      )
      .catch((err) => console.error("Erreur météo :", err))
      .finally(() => setLoading(false));
  }, [center]);

  if (loading) return <div className="card">Chargement météo…</div>;
  if (!weather) return <div className="card">Aucune donnée météo trouvée.</div>;

  return (
    <div className="card">
      <h3>Météo</h3>
      <p>
        <strong>Température :</strong> {weather.temperature_2m} °C 
      </p>
      <p>
        <strong>Vent :</strong> {weather.wind_speed_10m} km/h 
      </p>
      <p>
        <strong>Conditions :</strong> {desc(weather.weather_code)}
      </p>
    </div>
  );
}

import React, { useEffect, useState } from "react";

export default function WeatherCard() {
  const [weather, setWeather] = useState(null);
  const center = [48.85, 2.35];

  useEffect(() => {
    const [lat, lon] = center;
    fetch(`http://localhost:8081/api/weather/coords?lat=${lat}&lon=${lon}`)
      .then(res => res.json())
      .then(data => data.current && setWeather(data.current))
      .catch(console.error);
  }, []);

  const desc = (code) => ({
    0: "Ciel clair ☀️",
    1: "Principalement clair 🌤️",
    2: "Partiellement nuageux ⛅",
    3: "Couvert ☁️",
    61: "Pluie faible 🌧️",
    63: "Pluie modérée 🌧️",
    80: "Averses 🌦️",
    95: "Orage ⛈️"
  }[code] || "Inconnu");

  if (!weather) return <div className="card">Chargement météo…</div>;

  return (
    <div className="card">
      <h3>Météo</h3>
      <p><strong>Température :</strong> {weather.temperature_2m} °C 🌡️</p>
      <p><strong>Vent :</strong> {weather.wind_speed_10m} km/h 💨</p>
      <p><strong>Conditions :</strong> {desc(weather.weather_code)}</p>
    </div>
  );
}
import React, { useEffect, useState } from "react";

export default function AirCard() {
  const [aqiData, setAqiData] = useState(null);
  const center = [48.85, 2.35];

  useEffect(() => {
    const [lat, lon] = center;
    fetch(`http://localhost:8081/api/air/coords?lat=${lat}&lon=${lon}`)
      .then((res) => res.json())
      .then((data) => data.status === "ok" && setAqiData(data.data))
      .catch(console.error);
  }, []);

  const getColor = (a) => {
    if (a <= 50) return "green";
    if (a <= 100) return "yellow";
    if (a <= 150) return "orange";
    if (a <= 200) return "red";
    if (a <= 300) return "purple";
    return "maroon";
  };
  const getLabel = (a) => {
    if (a <= 50) return "🟢 Bonne";
    if (a <= 100) return "🟡 Modérée";
    if (a <= 150) return "🟠 Mauvaise (sensibles)";
    return "🔴 Mauvaise";
  };

  if (!aqiData) return <div className="card">Chargement AQI…</div>;

  return (
    <div className="card">
      <h3>Qualité de l’air</h3>
      <p>
        <strong>Ville :</strong> {aqiData.city.name}
      </p>
      <p>
        <strong>AQI :</strong>{" "}
        <span style={{ color: getColor(aqiData.aqi) }}>{aqiData.aqi}</span>
      </p>
      <p>
        <strong>Qualité :</strong> {getLabel(aqiData.aqi)}
      </p>
      <p>
        <strong>Dernière maj :</strong> {aqiData.time.s}
      </p>
    </div>
  );
}

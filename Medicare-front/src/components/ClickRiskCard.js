import React, { useEffect, useState } from "react";

const API_BASE =
  window.location.hostname === "localhost" || window.location.hostname === "127.0.0.1"
    ? "http://localhost:8081"
    : "http://172.31.250.86:8081";

export default function ClickRiskCard({ center }) {
  const [data, setData] = useState(null);

  useEffect(() => {
    if (!center) {
      return;
    }

    const lat = center[0];
    const lon = center[1];

    fetch(`${API_BASE}/api/click-risk/save?lat=${lat}&lon=${lon}`, {
      method: "POST"
    })
      .then((res) => res.json())
      .then((result) => {
        setData(result);
      })
      .catch((err) => {
        console.error("Erreur click risk :", err);
      });
  }, [center]);

  if (!center) {
    return <div className="card">Cliquez sur la carte.</div>;
  }

  if (!data) {
    return <div className="card">Chargement du score...</div>;
  }

  return (
    <div className="card">
      <h3>Risque du point</h3>
      <p><strong>Latitude :</strong> {data.latitude}</p>
      <p><strong>Longitude :</strong> {data.longitude}</p>
      <p><strong>Pollution :</strong> {data.pollutionScore}</p>
      <p><strong>Météo :</strong> {data.meteoScore}</p>
      <p><strong>Industrie :</strong> {data.industryScore}</p>
      <p><strong>Total :</strong> {data.totalScore}</p>
      <p><strong>Niveau :</strong> {data.level}</p>
    </div>
    
  );
}
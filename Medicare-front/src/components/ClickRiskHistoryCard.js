import React, { useEffect, useState } from "react";

const API_BASE =
  window.location.hostname === "localhost" || window.location.hostname === "127.0.0.1"
    ? "http://localhost:8081"
    : "http://172.31.250.86:8081";

export default function ClickRiskHistoryCard({ center }) {
  const [data, setData] = useState(null);

  useEffect(() => {
    if (!center) {
      return;
    }

    const lat = center[0];
    const lon = center[1];

    fetch(`${API_BASE}/api/click-risk/average?lat=${lat}&lon=${lon}`)
      .then((res) => res.json())
      .then((result) => {
        setData(result);
      })
      .catch((err) => {
        console.error("Erreur historique point :", err);
      });
  }, [center]);

  if (!center) {
    return <div className="card">Cliquez sur la carte.</div>;
  }

  if (!data) {
    return <div className="card">Chargement historique...</div>;
  }

  return (
    <div className="card">
      <h3>Score moyen historique</h3>
      <p><strong>Latitude :</strong> {data.latitude}</p>
      <p><strong>Longitude :</strong> {data.longitude}</p>
      <p><strong>Nombre d’enregistrements :</strong> {data.count}</p>
      <p><strong>Score moyen :</strong> {Number(data.averageScore).toFixed(2)}</p>
      <p><strong>Niveau :</strong> {data.level}</p>
    </div>
  );
}
import React, { useEffect, useState } from "react";

export default function AirCard({ center }) {
  const [aqiData, setAqiData] = useState(null);
  const [loading, setLoading] = useState(false);

  const getColor = (a) => {
    if (a <= 50) return "green";
    if (a <= 100) return "yellow";
    if (a <= 150) return "orange";
    if (a <= 200) return "red";
    if (a <= 300) return "purple";
    return "maroon";
  };

  const getLabel = (a) => {
    if (a <= 50) return " Bonne";
    if (a <= 100) return " Modérée";
    if (a <= 150) return " Mauvaise (sensibles)";
    return " Mauvaise";
  };

  useEffect(() => {
    if (!center) return;
    const [lat, lon] = center;
    setLoading(true);

    fetch(`/api/air/coords?lat=${lat}&lon=${lon}`)
      .then((res) => res.json())
      .then((data) => {
        if (data.status === "ok") setAqiData(data.data);
        else setAqiData(null);
      })
      .catch((err) => console.error("Erreur AQI :", err))
      .finally(() => setLoading(false));
  }, [center]);

  if (loading) return <div className="card">Chargement AQI...</div>;
  if (!aqiData)
    return (
      <div className="card">Aucune donnée de qualité de l’air trouvée.</div>
    );

  return (
    <div className="card">
      <h3>Qualité de l’air</h3>
      <p>
        <strong>Ville :</strong> {aqiData.city.name}
      </p>
      <p>
        <strong>AQI :</strong>{" "}
        <span style={{ color: getColor(aqiData.aqi), fontWeight: "bold" }}>
          {aqiData.aqi}
        </span>
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

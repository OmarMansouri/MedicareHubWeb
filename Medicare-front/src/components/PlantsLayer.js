import React, { useEffect, useState } from "react";
import { Circle, Popup } from "react-leaflet";

export default function PlantsLayer({ center, radius = 5 }) {
  const [plants, setPlants] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!center) return; 

    const [lat, lon] = center;
    setLoading(true);

    const url = `/api/plants?lat=${lat}&lon=${lon}&radiusKm=${radius}`;
    console.log("Requête :", url);

    fetch(url)
      .then((res) => res.json())
      .then((data) => {
        if (!Array.isArray(data)) {
          console.warn("⚠️ Réponse inattendue :", data);
          setPlants([]);
          return;
        }

        const valid = data.filter(
          (p) =>
            typeof p.latitude === "number" &&
            typeof p.longitude === "number" &&
            !Number.isNaN(p.latitude) &&
            !Number.isNaN(p.longitude) &&
            Math.abs(p.latitude) <= 90 &&
            Math.abs(p.longitude) <= 180
        );

        console.log(` ${valid.length} centrales reçues dans ${radius} km`);
        setPlants(valid);
      })
      .catch((err) => {
        console.error(" Erreur lors du chargement des centrales :", err);
        setPlants([]);
      })
      .finally(() => setLoading(false));
  }, [center, radius]);

  const getPlantColor = (co2) => {
    if (!co2) return "gray";
    if (co2 < 100000) return "green";
    if (co2 < 1000000) return "orange";
    return "red";
  };

  if (loading)
    return (
      <div
        style={{
          position: "absolute",
          top: 10,
          left: 10,
          background: "rgba(255,255,255,0.9)",
          padding: "5px 10px",
          borderRadius: "6px",
          zIndex: 1000,
        }}
      >
        Chargement des centrales...
      </div>
    );

  if (!loading && (!plants || plants.length === 0))
    return (
      <div
        style={{
          position: "absolute",
          top: 10,
          left: 10,
          background: "rgba(255,255,255,0.9)",
          padding: "5px 10px",
          borderRadius: "6px",
          fontSize: "14px",
          zIndex: 1000,
        }}
      >
        Aucune centrale trouvée dans un rayon de {radius} km.
      </div>
    );

  return (
    <>
      {plants
        .filter(
          (p) =>
            typeof p.latitude === "number" &&
            typeof p.longitude === "number" &&
            !Number.isNaN(p.latitude) &&
            !Number.isNaN(p.longitude)
        )
        .map((p, i) => (
          <Circle
            key={i}
            center={[p.latitude, p.longitude]}
            radius={800} 
            pathOptions={{
              fillColor: getPlantColor(p.emissionCo2Tons),
              color: getPlantColor(p.emissionCo2Tons),
              fillOpacity: 0.6,
            }}
          >
            <Popup>
              <strong>{p.name}</strong>
              <br />
              {p.country_long || p.country}
              <br />
              Type : {p.primaryFuel}
              <br />
              Capacité : {p.capacityMw} MW
              <br />
              CO₂ :{" "}
              {p.emissionCo2Tons
                ? `${p.emissionCo2Tons.toLocaleString()} t/an`
                : "n/a"}
            </Popup>
          </Circle>
        ))}
    </>
  );
}
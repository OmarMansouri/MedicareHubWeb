import React, { useEffect, useState } from "react";
import { Circle, Popup } from "react-leaflet";

/**
 * PlantsLayer
 * ------------
 * Affiche uniquement les centrales électriques situées
 * dans une zone proche du marqueur principal (AQI/Météo).
 * 
 * Props :
 *  - center : [latitude, longitude]
 *  - radius : rayon en kilomètres (par défaut 200 km)
 */
export default function PlantsLayer({ center, radius = 200 }) {
  const [plants, setPlants] = useState([]);

  useEffect(() => {
    if (!center) return; // évite le fetch tant que le centre n'est pas défini

    const [lat, lon] = center;

    fetch(`http://localhost:8081/api/plants?lat=${lat}&lon=${lon}&radiusKm=${radius}`)
      .then((res) => res.json())
      .then((data) => {
        console.log("✅ Centrales reçues :", data);
        if (Array.isArray(data)) {
          setPlants(data);
        } else {
          console.warn("⚠️ Réponse inattendue de l'API:", data);
          setPlants([]);
        }
      })
      .catch((err) => {
        console.error("Erreur lors du chargement des centrales:", err);
        setPlants([]);
      });
  }, [center, radius]);

  // 🎨 Couleur des cercles selon les émissions estimées
  const getPlantColor = (co2) => {
    if (!co2) return "gray";
    if (co2 < 100000) return "green";
    if (co2 < 1000000) return "orange";
    return "red";
  };

  return (
    <>
      {plants.length === 0 && (
        <p
          style={{
            position: "absolute",
            top: 10,
            left: 10,
            backgroundColor: "rgba(255,255,255,0.9)",
            padding: "5px 10px",
            borderRadius: "6px",
            fontSize: "14px",
          }}
        >
          Aucune centrale trouvée dans cette zone.
        </p>
      )}

      {Array.isArray(plants) &&
        plants.map((p, i) => (
          <Circle
            key={i}
            center={[p.latitude, p.longitude]}
            radius={8000} // ~8 km de rayon visuel
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
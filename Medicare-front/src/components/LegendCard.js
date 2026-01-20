import React from "react";
import "../styles/Dashboard.css";

export default function LegendCard() {
  return (
    <div className="card">
      <h3> Explications</h3>

      <h4>Couleurs des centrales</h4>
      <ul style={{ listStyle: "none", paddingLeft: "10px" }}>
        <li>
          <span style={{ color: "green", fontWeight: "bold" }}>● Vert :</span>{" "}
          Faibles émissions CO₂
        </li>
        <li>
          <span style={{ color: "orange", fontWeight: "bold" }}>
            ● Orange :
          </span>{" "}
          Émissions moyennes
        </li>
        <li>
          <span style={{ color: "red", fontWeight: "bold" }}>● Rouge :</span>{" "}
          Émissions élevées
        </li>
        <li>
          <span style={{ color: "gray", fontWeight: "bold" }}>● Gris :</span>{" "}
          Données CO₂ indisponibles
        </li>
      </ul>

      <h4> Qualité de l’air (AQI)</h4>
      <ul style={{ listStyle: "none", paddingLeft: "10px" }}>
        <li style={{ color: "green", fontWeight: "bold" }}>0 – 50 : ● Bonne</li>
        <li style={{ color: "yellow", fontWeight: "bold" }}>
          51 – 100 : ● Modérée
        </li>
        <li style={{ color: "orange", fontWeight: "bold" }}>
          101 – 150 : ● Mauvaise (personnes sensibles)
        </li>
        <li style={{ color: "red", fontWeight: "bold" }}>
          151 + : ● Mauvaise pour tous
        </li>
      </ul>

      <h4>Comment utiliser la carte</h4>
      <p style={{ fontSize: "14px" }}>
        • Au chargement : la carte se centre sur votre position GPS.
        <br />
        • Cliquez n’importe où : le marqueur se déplace, et les données (qualité
        de l’air, météo, centrales) s’actualisent automatiquement pour cette
        zone.
        <br />• Les cercles affichés représentent les centrales à proximité (par
        défaut dans 5 km).
      </p>

      <p style={{ fontSize: "13px", color: "#666" }}>
        Source des données : OpenAQ, Open‑Meteo, Global Power Plant Database
      </p>
    </div>
  );
}

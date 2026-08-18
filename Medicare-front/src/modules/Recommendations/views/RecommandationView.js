import React, { useState, useEffect } from "react";
import RecommandationsList from "../components/RecommandationsList";
import { fetchRecommandations } from "../api/recommendationsApi";

export default function RecommandationsView() {
  const patient = JSON.parse(localStorage.getItem("patient"));
  const idPatient = patient ? patient.idPatient : null;
  const [recommandations, setRecommandations] = useState(null);
  const [erreur, setErreur] = useState("");



  function charger(id) {
    setErreur("");
    setRecommandations(null);

    fetchRecommandations(id)
      .then((data) => {
        setRecommandations(data.recommandations || []);
      })
      .catch((err) => {
        console.error(err);
        setErreur("Erreur : impossible de communiquer avec le serveur");
      });
  }

  
    useEffect(() => {
      if (idPatient) {
      charger(idPatient);
      }
    }, []); 

      return (
        <div style={{ padding: 30 }}>
          <h1 style={{ fontFamily: "Georgia, serif", color: "#1a3c5e" }}>
            Recommandations personnalisées
          </h1>
          
          {patient ? (
            <p style={{ fontFamily: "Georgia, serif", color: "#1a3c5e", marginBottom: 15 }}>
                Patient : <strong>{patient.prenom} {patient.nom}</strong>
            </p>
            ) : (
            <p style={{ color: "red" }}>Vous devez être connecté.</p>
            )
          }

          {erreur && <p style={{ color: "red" }}>{erreur}</p>}
          {recommandations && <RecommandationsList recommandations={recommandations}
    />}
        </div>
      );
}
    
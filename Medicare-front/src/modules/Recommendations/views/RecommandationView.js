import React, { useState, useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import RecommandationsList from "../components/RecommandationsList";
import { fetchRecommandations } from "../api/recommendationsApi";

export default function RecommandationsView() {
  const [searchParams] = useSearchParams();
  const [recommandations, setRecommandations] = useState(null);
  const [erreur, setErreur] = useState("");

  const idPatient = searchParams.get("patient");

    useEffect(() => {
      if (!idPatient) {
        setErreur("Aucun identifiant de patient fourni");
        return;
      }

      fetchRecommandations(idPatient)
        .then((data) => {
          setRecommandations(data.recommandations || []);
      })
        .catch((err) => {
          console.error(err);
          setErreur("Erreur : impossible de communiquer avec le serveur");
          
        });
    }, [idPatient]);

    return (
      <div style={{ padding: 30 }}>
        <h1 style={{ fontFamily: "Georgia, serif", color: "#1a3c5e" }}>
          Recommandations personnalisées
        </h1>
        <p style={{ color: "#555", marginBottom: 20 }}>
          Patient n° {idPatient}
        </p>

        {erreur && <p style={{ color: "red" }}>{erreur}</p>}
        {recommandations && <RecommandationsList recommandations={recommandations}
  />}
      </div>
    );
}
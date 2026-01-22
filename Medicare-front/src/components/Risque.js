import React, { useState } from "react";
import "../styles/Risque.css";



// composant pour calculer le risque d'un patient
export default function Risque() {
  
// etats pour stocker l'id du patient le resultat et les erreurs
  const [idPatient, setIdPatient] = useState("");
  const [resultat, setResultat] = useState(null);
  const [erreur, setErreur] = useState("");

    // fonction appelee quand on clique sur le bouton calculer
    const calculer = () => {
    setErreur("");
    setResultat(null);

        // verifier que l'id du patient est bien saisi
     if (!idPatient) {
      setErreur("Veuillez entrer un identifiant de patient");
      return;
    }

        // appel l'API backend pour calculer le risque
      fetch(`http://172.31.250.86:8081/risque/patient/${idPatient}`)
      .then((res) => res.json())
      .then((data) => {
        if (data.error) {
          setErreur(data.error);
        } else {
          setResultat(data);
        }
      })
      .catch(() => setErreur("Erreur : impossible de contacter le serveur"));
  };

  return (
    <div className="risque-container">
      <h1>Évaluation du risque individuel</h1>

      <div className="risque-box">
        <label>Identifiant du patient :</label>
        <input
          type="number"
          value={idPatient}
          onChange={(e) => setIdPatient(e.target.value)}
          placeholder="Ex : 1"
        />

        <button onClick={calculer}>Calculer</button>

        {erreur && <p className="erreur">{erreur}</p>}

        {resultat && (
          <div className="resultat">
            <p>
              <strong>Score :</strong> {resultat.score}
            </p>
            <p>
              <strong>Niveau :</strong> {resultat.niveau}
            </p>

            <h3>Détails du calcul</h3>
<ul>
  {resultat.details.map((d, index) => (
    <li key={index}>{d}</li>
  ))}
</ul>

          </div>
        )}
      </div>
    </div>
  );
}

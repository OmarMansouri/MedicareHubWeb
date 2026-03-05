import React, { useState } from "react";
import "../styles/Risque.css";

// calculer le risque d'un patient
export default function Risque() {
  
// etats pour stocker l'id du patient le resultat et les erreurs
  const [idPatient, setIdPatient] = useState("");
  const [resultat, setResultat] = useState(null);
  const [erreur, setErreur] = useState("");

  // fonction appelee quand on clique sur le bouton calculer
  const calculer = () => {
  console.log("Clic sur le bouton calculer");
  setErreur("");
  setResultat(null);
  
  // verifier que l'id du patient est bien saisi
    if (!idPatient) {
    console.log("Aucun identifiant de patient saisi");
    setErreur("Veuillez entrer un identifiant de patient");
    return;
    }

  // appel API pour calculer le risque
   console.log("Envoi de la requête au backend pour le patient", idPatient);
  fetch(`http://localhost:8081/risque/patient/${idPatient}`)
  .then((res) => {
  console.log("Réponse reçue du serveur");
  return res.json();
  })
        
  .then((data) => {
  console.log("Données reçues :", data);
        
  if (data.error) {
  console.log("Erreur côté serveur :", data.error);

  setErreur(data.error);
  } 
  else {
  console.log("Affichage du score et du niveau de risque");

  setResultat(data);
  } })
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
  <strong>Score profil :</strong> {resultat.scoreProfil}/100
  </p>


 <h3>maladies à risque</h3>
{resultat.podium && resultat.podium.length > 0 && (
<div>
{resultat.podium.map((m, index) => (
<p key={index}>
 <strong>{index + 1}. {m.maladie}</strong> — Score : {m.score}/100 — Niveau : {m.niveau}
</p>
 ))}
</div>
)}
            

<h3>Détails du profil</h3>
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

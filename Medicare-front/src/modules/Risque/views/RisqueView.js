import React, { useState } from "react";
import PodiumMaladies from "../components/PodiumMaladies";
import { calculerRisque, enregistrerRisque, getDerniereEvaluation } from "../api/risqueApi";
import { boutonStyle } from "../styles/styles";

export default function RisqueView() {
// états

const [idPatient, setIdPatient] = useState("");
const [resultat, setResultat] = useState(null);
const [erreur, setErreur] = useState("");
const [messageEnregistrement, setMessageEnregistrement] = useState("");
const [derniereEvaluation, setDerniereEvaluation] = useState("");

// charger la derniere evaluation du patient
    function chargerDerniereEvaluation(id) {
    getDerniereEvaluation(id)
    .then(function(data) {
    
      if (data.dateCalcul) {
       setDerniereEvaluation("Dernière évaluation : " + data.dateCalcul.substring(0, 10));
       } 
       else {
    setDerniereEvaluation("Aucune évaluation enregistrée");
       } })
      
    .catch(function() { 
      setDerniereEvaluation("Erreur lors du chargement."); 
       });
      }

    // calculer le risque du patient
    function calculer() {
    console.log("Clic sur le bouton calculer");
    setErreur("");
     setResultat(null);

   if (!idPatient) {
    console.log("Aucun identifiant de patient saisi");
     setErreur("Veuillez entrer un identifiant de patient");
      return;
      }

    console.log("Envoi de la requête au backend pour le patient", idPatient);
     calculerRisque(idPatient)
     .then(function(data) {
   console.log("Données reçues :", data);

      
   if (data.error) {
    setErreur(data.error);
     } 
    else {
      setResultat(data);
      chargerDerniereEvaluation(idPatient);
     }
   })

   .catch(function() { 
    setErreur("Erreur : impossible de contacter le serveur"); 
      });
      }

    // enregistrer le résultat en base
    function enregistrer() {
    console.log("Enregistrement du résultat pour le patient", idPatient);
     setMessageEnregistrement("");

   enregistrerRisque(idPatient, resultat.podium)
    .then(function() { 
    setMessageEnregistrement("Résultat enregistré avec succès !"); 
     })
   .catch(function() { 
      setMessageEnregistrement("Erreur lors de l'enregistrement."); 
   });
    }


   return (
   <div style={{ maxWidth: 500, margin: "40px auto" }}>

   <h2 style={{ textAlign: "center" }}>Évaluation du risque</h2>

   <div style={{ background: "white", borderRadius: 10, padding: 25 }}>

    <label>Identifiant du patient :</label>
      <input

   type="number"
   value={idPatient}
    onChange={(e) => setIdPatient(e.target.value)}
   placeholder="Ex : 1"
    style={{ width: "100%", marginBottom: 15 }}
   />


   <button onClick={calculer} style={boutonStyle}>
     Calculer
   </button>

   {erreur ? <p style={{ color: "red" }}>{erreur}</p> : null}
    {derniereEvaluation ? <p>{derniereEvaluation}</p> : null}

     {resultat ? (
      <div style={{ marginTop: 20 }}>

       <p>
      <strong>Score profil : </strong> {resultat.scoreProfil}/100
      </p>

      <h3>Maladies à risque</h3>
       <PodiumMaladies podium={resultat.podium} />

      <button onClick={enregistrer} style={boutonStyle}>
       Enregistrer le résultat
      </button>

   {messageEnregistrement ? <p>{messageEnregistrement}</p> : null}

    <h3>Détails du profil</h3>
     <ul>
    {resultat.details.map((d, index) => (
    <li key={index}>{d}</li>
      ))}
    </ul>

    </div>
    ) : null}

   </div>
   </div>
    );
   }
import React, { useState } from "react";
import PodiumMaladies from "../components/PodiumMaladies";
import { calculerRisque, enregistrerRisque, getDerniereEvaluation } from "../api/risqueApi";
import { boutonStyle } from "../styles/styles";
import { useNavigate } from "react-router-dom";

export default function RisqueView() {
// états

const patient = JSON.parse(localStorage.getItem("patient"));
const idPatient = patient ? patient.idPatient : null;
const [resultat, setResultat] = useState(null);
const [erreur, setErreur] = useState("");
const [messageEnregistrement, setMessageEnregistrement] = useState("");
const [derniereEvaluation, setDerniereEvaluation] = useState("");
const navigate = useNavigate();

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

   <div style={{ background: "white", borderRadius: 10, padding: 25, boxShadow: "0 2px 10px rgba(0,0,0,0.1)" }}>

    {patient ? (
    <p style={{ fontFamily: "Georgia, serif", color: "#1a3c5e", marginBottom: 15 }}>
        Patient : <strong>{patient.prenom} {patient.nom}</strong>
    </p>
    ) : (
    <p style={{ color: "red" }}>Vous devez être connecté.</p>
    )}


   <div style={{ textAlign: "center"}}>
    <button onClick={calculer} style={boutonStyle}>Calculer</button>
   </div>
    
   {erreur ? <p style={{ color: "red" }}>{erreur}</p> : null}
    {derniereEvaluation ? <p>{derniereEvaluation}</p> : null}

     {resultat ? (
      <div style={{ marginTop: 20 }}>

       <div style={{ background: "#f0f6ff", borderRadius: 8, padding: 12, marginBottom: 15 }}>
        <strong>Score profil : {resultat.scoreProfil}/100</strong>
        </div>
      <h3>Maladies à risque</h3>
       <PodiumMaladies podium={resultat.podium} />

      <button onClick={enregistrer} style={boutonStyle}>
       Enregistrer le résultat
      </button>

   {messageEnregistrement ? <p>{messageEnregistrement}</p> : null}

    <h3>Détails du profil</h3>
     <div style={{ background: "#f5f5f5", borderRadius: 8, padding: 15}}>
    {resultat.details.map((d, index) => (
    <p key={index}>•{d}</p>
      ))}
    </div>

    <button
        onClick={() => navigate(`/recommendations?patient=${idPatient}`)}
        style={boutonStyle}
    >
      Voir les recommandations
    </button>

    </div>
    ) : null}

   </div>
   </div>
    );
   }
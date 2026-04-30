import React, { useState } from "react";
import ListeMaladies from "../components/ListeMaladies";
import { saveAntecedents } from "../Api/AntecedentsApi";
import { boutonStyle } from "../styles/styles";

export default function AntecedentsView() {

  // mes états
  const [selected, setSelected] = useState([]);
  const [typeRelation, setTypeRelation] = useState("familial");
  const [idPatient, setIdPatient] = useState("");
  const [message, setMessage] = useState("");

  // liste des maladies chroniques qu'on peut avoir comme antécédent
  const diseases = [
    { id: 37, nom: "Asthme" },
    { id: 38, nom: "Hypertension" },
    { id: 39, nom: "Hypotension" },
    { id: 41, nom: "Stress" },
    { id: 42, nom: "Anxiété" },
    { id: 43, nom: "Dépression légère" },
  ];

  // cocher ou décocher une maladie
  function handleCheckbox(id) {
    if (selected.includes(id)) {


    // on retire la maladie si déjà cochée
    const nouvelleliste = selected.filter((x) => x !== id);
    setSelected(nouvelleliste);
    }       

    // sinon on l'ajoute à la liste
    else {
    const nouvelleliste = selected.concat(id);
      setSelected(nouvelleliste); 
    } }

  // envoyer les antécédents au serveur
   function enregistrer() {
    setMessage("");
    console.log("patient:", idPatient, "maladies sélectionnées:", selected);

    if (!idPatient) {
    setMessage("Veuillez entrer un identifiant de patient.");
    return;
    } 

    if (selected.length === 0) {
    setMessage("Veuillez sélectionner au moins une maladie.");
    return;
    }

    saveAntecedents(idPatient, selected, typeRelation)
    .then(function() {
    setMessage("Antécédents enregistrés avec succès !");
     })
     
    .catch(function() {
    setMessage("Erreur lors de l'enregistrement.");
    });
    }
    
    return (
    <div style={{ maxWidth: 500, margin: "40px auto", fontFamily: "Arial, sans-serif" }}>

    <h2 style={{ textAlign: "center" }}>
        🩺 Mes antécédents
    </h2>

    <div style={{ background: "white", borderRadius: 10, padding: 25, boxShadow: "0 2px 10px rgba(0,0,0,0.1)" }}>

    <label style={{ color: "#555", fontSize: 14 }}>Identifiant du patient :</label>
     <input
        type="number"
        value={idPatient}
        onChange={(e) => setIdPatient(e.target.value)}
        placeholder="Ex : 1"
        style={{ width: "100%", padding: 10, margin: "8px 0 15px 0", border: "1px solid #d0dce8", borderRadius: 6, fontSize: 15, boxSizing: "border-box" }}    
        />

     <p style={{ color: "#555", fontSize: 14, fontFamily: "Georgia, serif", fontStyle: "italic", marginBottom: 10 }}>
        Sélectionnez vos antécédents médicaux
     </p>

     <ListeMaladies
        diseases={diseases}
        selected={selected}
        onCheck={handleCheckbox}
        />

     <p style={{ marginTop: 15, fontFamily: "Georgia, serif", color: "#2c3e50" }}><strong>Type d'antécédent :</strong></p>
     <label style={{ marginRight: 15, fontFamily: "Century Gothic, sans-serif" }}>
        <input

     type="radio"
     checked={typeRelation === "familial"}
     onChange={() => setTypeRelation("familial")}
     />
     {" "}Familial
            
    </label>
        <label style={{ fontFamily: "Century Gothic, sans-serif" }}>
     <input
    
    type="radio"
    checked={typeRelation === "personnel"}
     onChange={() => setTypeRelation("personnel")}
     />
     {" "}Personnel
    
    </label>
      <div style={{ textAlign: "center" }}>

        <button onClick={enregistrer} style={boutonStyle}>
         Enregistrer
        </button>
        </div>

    {message && <p style={{ textAlign: "center", marginTop: 10, color: message.includes("succès") ? "#28a745" : "red", fontSize: 14 }}>{message}</p>}

    </div>
     </div>
     );}
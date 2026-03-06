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
    <div style={{ width: "500px", margin: "40px auto" }}>
    <h2>Renseigner mes antécédents</h2>
    <label>Identifiant du patient :</label>
      <input
      type="number"
      value={idPatient}
      onChange={(e) => setIdPatient(e.target.value)}
      placeholder="Ex : 1"
      style={{ width: "100%", marginBottom: "20px" }}
    />
    <ListeMaladies
      diseases={diseases}
      selected={selected}
      onCheck={handleCheckbox}
      />

      <p style={{ marginTop: "15px" }}><strong>Type d'antécédent :</strong></p>
      <label style={{ marginRight: "15px" }}>
        <input
      type="radio"
      checked={typeRelation === "familial"}
        onChange={() => setTypeRelation("familial")}
        />

    {" "}Familial
      </label>
      <label>
        <input
      type="radio"
      checked={typeRelation === "personnel"}
      onChange={() => setTypeRelation("personnel")}
        />
        {" "}Personnel
        </label>

      <button onClick={enregistrer} style={boutonStyle}>
      Enregistrer
      </button>

  {message ? <p style={{ marginTop: "10px" }}>{message}</p> : null}

  </div>
     );}
import React from "react";

// affiche le podium des maladies à risque

export default function PodiumMaladies(props) {
 var podium = props.podium;
 
 //ajout de couleurs selon niveau
 function getCouleur(niveau){
  if (niveau === "élevé") return "#dc3545"
   if (niveau === "moyen") return "#fd7e14"
   else return"#28a745"
 }
 return (
<div style={
{ borderRadius: 8, padding: 15 }}>
  {podium.map((m, index) => {
    var couleur = getCouleur(m.niveau);
    return(
      <div key={index} style={{ background: "#f8f9fa" , padding: 10, marginBottom: 10, borderLeft: "4px solid " + couleur}}>
        <p style={{fontFamily: "bold"}}>
          {index +1}.{m.maladie}
        </p>
         <p style={{color: couleur}}>
          score : {m.score}/100 -- Niveau : {m.niveau}
        </p>
      </div>
      );
  })}
 </div>
 );}
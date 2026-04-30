import React from "react";

// affiche le podium des maladies à risque

export default function PodiumMaladies(props) {
 var podium = props.podium;
 return (

<div style={
{ background: "#e8f4fd", borderRadius: 8, padding: 15 }}>
  {podium.map((m, index) => (
<p key={index}>
  <strong>{index + 1}. {m.maladie}</strong> — Score : {m.score}/100 — Niveau : {m.niveau}
 </p>
))} 
 </div>
 );}
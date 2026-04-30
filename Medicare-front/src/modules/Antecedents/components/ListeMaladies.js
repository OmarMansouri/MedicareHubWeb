import React from "react";

export default function ListeMaladies(props) {
var diseases = props.diseases;
var selected = props.selected;
 var onCheck = props.onCheck;

return (
 <div style={{ border: "1px solid #b8d4f0", borderRadius: 8, padding: 15, maxHeight: 250, overflowY: "auto" }}>
  {diseases.map((d) => (
 <label key={d.id} style={{ display: "block", padding: "8px 0", cursor: "pointer", borderBottom: "1px solid #f0f0f0", fontFamily: "Century Gothic, sans-serif", fontSize: 15 }}>
  
<input
  type="checkbox"
    checked={selected.includes(d.id)}
   onChange={() => onCheck(d.id)}
    style={{ marginRight: 10 }}
   />
  {d.nom}
  </label>
  ))}
   </div>
   );
   }
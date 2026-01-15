import React, { useState, useEffect } from "react";
import axios from "axios";

export default function Prediagnostic() {
  const [symptomes, setSymptomes] = useState([]);
  const [symptomesSelectionnes, setSymptomesSelectionnes] = useState([]);
  const [symptomesPoses, setSymptomesPoses] = useState([]);
  const [questionCourante, setQuestionCourante] = useState(null);
  const [etape, setEtape] = useState("selection");

  useEffect(() => {
    axios.get("/api/symptomes")
      .then(res => setSymptomes(res.data))
      .catch(err => console.error("Erreur récupération symptômes :", err));
  }, []);

  const handleCheckboxChange = (id) => {
    setSymptomesSelectionnes(prev => 
      prev.includes(id) ? prev.filter(x => x!==id) : [...prev, id]
    );
  };

  const demarrerArbre = async () => {
    setSymptomesPoses([...symptomesSelectionnes]);
    const res = await axios.post("/api/prochaineQuestion", { symptomesPoses: symptomesSelectionnes });
    if (res.data.question) setQuestionCourante(res.data.question);
    setEtape("questions");
  };

  const repondre = async (oui) => {
    const nouveauxSymptomes = oui ? [...symptomesSelectionnes, questionCourante.symptomeId] : symptomesSelectionnes;
    const nouveauxPoses = [...symptomesPoses, questionCourante.symptomeId];

    setSymptomesSelectionnes(nouveauxSymptomes);
    setSymptomesPoses(nouveauxPoses);

    const res = await axios.post("/api/prochaineQuestion", { symptomesPoses: nouveauxPoses });
    if (res.data.question) setQuestionCourante(res.data.question);
    else setEtape("fin");
  };

  const recommencer = () => {
    setSymptomesSelectionnes([]);
    setSymptomesPoses([]);
    setQuestionCourante(null);
    setEtape("selection");
  };

  return (
    <div style={{ maxWidth: 500, margin: "40px auto", fontFamily: "Arial" }}>
      <h2>Assistant médical</h2>

      {etape==="selection" && (
        <div>
          <p>Sélectionnez vos symptômes :</p>
          <div style={{ border:"1px solid #ccc", borderRadius:5, padding:15, maxHeight:300, overflowY:"auto" }}>
            {symptomes.map(s => (
              <label key={s.id} style={{ display:"block", padding:"8px 0", cursor:"pointer", borderBottom:"1px solid #f0f0f0" }}>
                <input type="checkbox" checked={symptomesSelectionnes.includes(s.id)} onChange={()=>handleCheckboxChange(s.id)} style={{ marginRight:10 }} />
                {s.nom}
              </label>
            ))}
          </div>
          <button onClick={demarrerArbre} disabled={symptomesSelectionnes.length===0} style={{ marginTop:20, width:"100%", padding:12, background:symptomesSelectionnes.length===0?"#ccc":"#1976d2", color:"white", border:"none", borderRadius:5 }}>
            Commencer ({symptomesSelectionnes.length} symptome(s))
          </button>
        </div>
      )}

      {etape==="questions" && questionCourante && (
        <div style={{ marginTop:30 }}>
          <h3>{questionCourante.texte}</h3>
          <button onClick={() => repondre(true)} style={{ marginRight:10, padding:"10px 20px", background:"white", color:"#333", border:"1px solid #ccc", borderRadius:5, cursor:"pointer" }}>Oui</button>
          <button onClick={() => repondre(false)} style={{ padding:"10px 20px", background:"white", color:"#333", border:"1px solid #ccc", borderRadius:5, cursor:"pointer" }}>Non</button>


        </div>
      )}

      {etape==="fin" && (
        <div style={{ marginTop:30 }}>
          <h3>Plus de questions à poser</h3>
          <p>Analyse terminée.</p>
          <div style={{ marginTop:15, padding:15, background:"white", borderRadius:5 }}>
            <strong>Symptômes confirmés :</strong>
            <div style={{ marginTop:8 }}>
              {symptomesSelectionnes.map(id => symptomes.find(s => s.id === id)?.nom).join(", ") || "Aucun"}
            </div>
          </div>
          <button onClick={recommencer} style={{ marginTop:20, padding:"10px 20px", background:"#607d8b", color:"white", border:"none", borderRadius:5 }}>Recommencer</button>
        </div>
      )}
    </div>
  )
}

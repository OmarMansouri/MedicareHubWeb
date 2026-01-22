import React, { useState, useEffect } from "react";
import axios from "axios";

export default function Prediagnostic() {
  const [symptomes, setSymptomes] = useState([]);
  const [symptomesPresents, setSymptomesPresents] = useState([]);
  const [symptomesAbsents, setSymptomesAbsents] = useState([]);
  const [questionCourante, setQuestionCourante] = useState(null);
  const [etape, setEtape] = useState("selection");

  useEffect(() => {
    axios.get("http://172.31.250.86:8081/api/symptomes")
      .then(res => setSymptomes(res.data))
      .catch(err => console.error(err));
  }, []);

  const handleCheckboxChange = (id) => {
    setSymptomesPresents(prev => 
      prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]
    );
  };

  const demarrerArbre = async () => {
    const res = await axios.post("http://172.31.250.86:8081/api/prochaineQuestion", { 
      symptomesPresents,
      symptomesAbsents: []
    });
    if (res.data.question) {
      setQuestionCourante(res.data.question);
      setEtape("questions");
    } else {
      setEtape("fin");
    }
  };

  const repondre = async (oui) => {
    const nouveauxPresents = oui ? [...symptomesPresents, questionCourante.symptomeId] : symptomesPresents;
    const nouveauxAbsents = oui ? symptomesAbsents : [...symptomesAbsents, questionCourante.symptomeId];
    setSymptomesPresents(nouveauxPresents);
    setSymptomesAbsents(nouveauxAbsents);

    const res = await axios.post("http://172.31.250.86/api/prochaineQuestion", { 
      symptomesPresents: nouveauxPresents,
      symptomesAbsents: nouveauxAbsents
    });
    if (res.data.question) {
      setQuestionCourante(res.data.question);
    } else {
      setEtape("fin");
    }
  };

  const recommencer = () => {
    setSymptomesPresents([]);
    setSymptomesAbsents([]);
    setQuestionCourante(null);
    setEtape("selection");
  };

  const boutonStyle = (bg) => ({
    padding: "10px 25px",
    margin: "10px",
    backgroundColor: bg,
    color: "white",
    border: "none",
    borderRadius: 5,
    cursor: "pointer",
    fontWeight: "bold",
    transition: "0.2s",
  });

  const boutonHover = (e) => {
    e.target.style.transform = "scale(1.05)";
    e.target.style.boxShadow = "0 3px 8px rgba(0,0,0,0.2)";
  };

  const boutonOut = (e) => {
    e.target.style.transform = "scale(1)";
    e.target.style.boxShadow = "none";
  };

  return (
    <div style={{ maxWidth: 500, margin: "40px auto", fontFamily: "Arial, sans-serif" }}>
      <h2 style={{ textAlign: "center", fontSize: 28, marginBottom: 10 }}>Assistant médical</h2>
      {etape === "selection" && (
        <div>
          <p style={{ color: "#555", fontSize: 16, textAlign: "center" }}>
            Sélectionnez vos symptômes pour commencer
          </p>
          <div style={{ border: "1px solid #ccc", borderRadius: 8, padding: 15, maxHeight: 300, overflowY: "auto" }}>
            {symptomes.map(s => (
              <label key={s.id} style={{ display: "block", padding: "8px 0", cursor: "pointer", borderBottom: "1px solid #f0f0f0" }}>
                <input 
                  type="checkbox"
                  checked={symptomesPresents.includes(s.id)}
                  onChange={() => handleCheckboxChange(s.id)}
                  style={{ marginRight: 10 }}
                />
                {s.nom}
              </label>
            ))}
          </div>
          <button
            onClick={demarrerArbre}
            disabled={symptomesPresents.length === 0}
            style={{
              ...boutonStyle(symptomesPresents.length === 0 ? "#ccc" : "#2c3e50"),
              width: "100%",
            }}
            onMouseOver={boutonHover}
            onMouseOut={boutonOut}
          >
            Commencer ({symptomesPresents.length} symptôme(s))
          </button>
        </div>
)}


      {etape === "questions" && questionCourante && (
        <div style={{ marginTop: 30, textAlign: "center" }}>
          <h3 style={{ marginBottom: 20 }}>{questionCourante.texte}</h3>
          <button
            onClick={() => repondre(true)}
            style={boutonStyle("#3498db")} 
            onMouseOver={boutonHover}
            onMouseOut={boutonOut}
          >
            Oui
          </button>
          <button
            onClick={() => repondre(false)}
            style={boutonStyle("#7f8c8d")} 
            onMouseOver={boutonHover}
            onMouseOut={boutonOut}
          >
            Non
          </button>
        </div>
      )}

      {etape === "fin" && (
        <div style={{ marginTop: 30 }}>
          <h3>Analyse terminée</h3>
          <div style={{ marginTop: 15, padding: 15, background: "#f5f5f5", borderRadius: 5 }}>
            <strong>Symptômes confirmés :</strong>
            <div style={{ marginTop: 8 }}>
              {symptomesPresents.map(id => symptomes.find(s => s.id === id)?.nom).join(", ") || "Aucun"}
            </div>
          </div>
          <button
            onClick={recommencer}
            style={boutonStyle("#2c3e50")}
            onMouseOver={boutonHover}
            onMouseOut={boutonOut}
          >
            Recommencer
          </button>
        </div>
      )}
    </div>
  );
}

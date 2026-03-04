import { useState, useEffect } from "react";
import { getSymptomes, prochaineQuestion } from "../api/prediagnosticApi"; 
import SymptomeSelection from "../components/SymptomesSelection";
import QuestionArbre from "../components/QuestionArbre";
import ResultatFinal from "../components/ResultatFinal";
import { useNavigate } from "react-router-dom";

export default function PrediagnosticView() {

  const navigate = useNavigate();
  const [symptomes, setSymptomes] = useState([]);
  const [symptomesPresents, setSymptomesPresents] = useState([]);
  const [symptomesAbsents, setSymptomesAbsents] = useState([]);
  const [questionCourante, setQuestionCourante] = useState(null);
  const [resultat, setResultat] = useState(null);
  const [etape, setEtape] = useState("selection");

  useEffect(() => {
    getSymptomes() 
      .then(data => setSymptomes(data))
      .catch(err => console.error(err));
  }, []);

  const patient = JSON.parse(localStorage.getItem("patient"));
  if (!patient) {
    return (
      <div style={{ display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "center" ,fontFamily: "Georgia, serif",height: "70vh" }}>
        <p style={{ color: "#1a3c5e", fontSize: 18 }}>
          Vous devez être connecté pour accéder au prédiagnostic.
        </p>
        <button
          onClick={() => navigate("/connexion")}
          style={{ padding: "10px 25px", backgroundColor: "#0d6efd", color: "white", border: "none", borderRadius: 5, cursor: "pointer", fontWeight: "bold",fontFamily: "Arial, sans-serif" }}
        >
          Se connecter
        </button>
      </div>
    );
  }

  const handleCheckboxChange = (id) => {
    setSymptomesPresents(prev =>
      prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]
    );
  };

  const demarrerArbre = async () => {
    console.log("IDs envoyés au backend:", symptomesPresents);
    const data = await prochaineQuestion(symptomesPresents, []); 
    if (data.type === "QUESTION") {
      setQuestionCourante(data.question);
      setEtape("questions");
    } else if (data.type === "RESULTAT") {
      setResultat(data.resultats);
      setEtape("fin");
    }
  };

  const repondre = async (oui) => {
    const nouveauxPresents = oui ? [...symptomesPresents, questionCourante.symptomeId] : symptomesPresents;
    const nouveauxAbsents = oui ? symptomesAbsents : [...symptomesAbsents, questionCourante.symptomeId];
    setSymptomesPresents(nouveauxPresents);
    setSymptomesAbsents(nouveauxAbsents);

    const data = await prochaineQuestion(nouveauxPresents, nouveauxAbsents); 
    if (data.type === "QUESTION") {
      setQuestionCourante(data.question);
    } else if (data.type === "RESULTAT") {
      setResultat(data.resultats);
      setEtape("fin");
    }
  };

  const recommencer = () => {
    setSymptomesPresents([]);
    setSymptomesAbsents([]);
    setQuestionCourante(null);
    setResultat(null);
    setEtape("selection");
  };

  return (
    <div style={{ maxWidth: 500, margin: "40px auto", fontFamily: "Arial, sans-serif" }}>
      <h2 style={{ 
  textAlign: "center",
  fontFamily: "Georgia, serif",
  color: "#1a3c5e",
  letterSpacing: 3,
  textTransform: "uppercase",
  fontSize: 28
}}> 🩺 Assistant Médical</h2>
      {etape === "selection" && (
        <SymptomeSelection
          symptomes={symptomes}
          symptomesPresents={symptomesPresents}
          onCheckboxChange={handleCheckboxChange}
          onDemarrer={demarrerArbre}
        />
      )}
      {etape === "questions" && questionCourante && (
        <QuestionArbre
          questionCourante={questionCourante}
          repondre={repondre}
        />
      )}
      {etape === "fin" && (
        <ResultatFinal
          symptomes={symptomes}
          symptomesPresents={symptomesPresents}
          resultat={resultat}
          recommencer={recommencer}
        />
      )}
    </div>
  );
}
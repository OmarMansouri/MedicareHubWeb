import { useState, useEffect } from "react";
import { getSymptomes, prochaineQuestion } from "../api/prediagnosticApi"; 
import SymptomeSelection from "../components/SymptomesSelection";
import QuestionArbre from "../components/QuestionArbre";
import ResultatFinal from "../components/ResultatFinal";

export default function PrediagnosticView() {

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

  const handleCheckboxChange = (id) => {
    setSymptomesPresents(prev =>
      prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]
    );
  };

  const demarrerArbre = async () => {
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
      <h2 style={{ textAlign: "center" }}>Assistant médical</h2>
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

import { boutonStyle, boutonHover, boutonOut } from "../styles/styles";

export default function ResultatFinal({ symptomes, symptomesPresents, resultat, recommencer }) {
  return (
    <div style={{ marginTop: 30 }}>
      <h3>Analyse terminée</h3>
      <div style={{ marginTop: 15, padding: 15, background: "#f5f5f5", borderRadius: 5 }}>
        <strong>Symptômes confirmés :</strong>
        <div style={{ marginTop: 8 }}>
          {symptomesPresents.map(id => symptomes.find(s => s.id === id)?.nom).join(", ") || "Aucun"}
        </div>
      </div>
      {resultat && resultat.length > 0 && (
        <div style={{ marginTop: 15, padding: 15, background: "#e8f4fd", borderRadius: 8 }}>
          <strong>Résultats :</strong>
          {resultat.map((r, idx) => (
            <div key={idx}>{r.nom} - {Math.round(r.probabilite * 100)}%</div>
          ))}
        </div>
      )}
      <button
        onClick={recommencer}
        style={boutonStyle("#2c3e50")}
        onMouseOver={boutonHover}
        onMouseOut={boutonOut}
      >
        Recommencer
      </button>
    </div>
  );
}
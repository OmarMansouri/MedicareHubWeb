
import { boutonStyle, boutonHover, boutonOut } from "../styles/styles";
import ProgressBar from 'react-bootstrap/ProgressBar';

export default function ResultatFinal({ symptomes, symptomesPresents, resultat, recommencer }) {
  return (
    <div style={{ marginTop: 30 }}>
      <h3 style={{ textAlign: "center",fontFamily: "Georgia, serif",color: "#2c3e50",letterSpacing: 1,fontSize: 22}}>Analyse terminée</h3>
      <div style={{ marginTop: 15, padding: 15, background: "#f5f5f5", borderRadius: 5 }}>
        <strong style={{ fontSize: 18,fontFamily: "Georgia, serif", color: "#2c3e50", letterSpacing: 1 }}>Symptômes confirmés:</strong>
        <div style={{ marginTop: 8,fontFamily: "Gill Sans, sans-serif" }}>
          {symptomesPresents.map(id => symptomes.find(s => s.id === id)?.nom).join(", ") || "Aucun"}
        </div>
      </div>
      {resultat && resultat.length > 0 && (
        <div style={{ marginTop: 15, padding: 15, background: "#e8f4fd", borderRadius: 8 }}>
          <strong style={{ fontSize: 18,fontFamily: "Georgia, serif", color: "#2c3e50", letterSpacing: 1 }}>Maladies Probables :</strong>
          {resultat.map((r, idx) => {
            const now = Math.round(r.probabilite * 100);
            const variants = [ "dark","primary", "secondary"];
            const couleurs = [ "#2c3e50","#0d6efd", "#6c757d"];
            return (
              <div key={idx} style={{ display: "flex", alignItems: "center", gap: 10, marginTop: 10 }}>
                <span style={{ minWidth: 100, fontWeight: "bold",fontFamily: "Gill Sans, sans-serif" , color: couleurs[idx] }}>{r.nom}</span>
                <div style={{ flex: 1, height: 25 }}>
                  <ProgressBar 
                    variant={variants[idx]} 
                    now={now} 
                    label={`${now}%`} 
                    style={{ height: 25 }}
                  />
               </div>
              </div>  
           );
          })}
        </div>
      )}
      <div style={{ textAlign: "center" }}>
        <button
          onClick={recommencer}
          style={boutonStyle("#0d6efd")}
          onMouseOver={boutonHover}
          onMouseOut={boutonOut}
        >
          Recommencer
        </button>
      </div>
    </div>
  );
}
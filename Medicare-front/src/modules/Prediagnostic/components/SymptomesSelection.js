
import { boutonStyle, boutonHover, boutonOut } from "../styles/styles";

export default function SymptomeSelection({ symptomes, symptomesPresents, onCheckboxChange, onDemarrer }) {
  return (
    <div>
      <p style={{ color: "#555", fontSize: 16, textAlign: "center",fontFamily: "Georgia, serif",marginBottom: 15 ,fontStyle: "italic" }}>
        Sélectionnez vos symptômes 
      </p>
      <div style={{ maxWidth: 600, margin: "0 auto" }}>
          <div style={{ border: "1px solid #b8d4f0", borderRadius: 8, padding: 15, maxHeight: 300, overflowY: "auto" }}>
            {symptomes.map(s => (
              <label key={s.id} style={{ display: "block", padding: "8px 0", cursor: "pointer", borderBottom: "1px solid #f0f0f0", fontFamily: "Century Gothic, sans-serif",fontSize: 15  }}>
                <input
                  type="checkbox"
                  checked={symptomesPresents.includes(s.id)}
                  onChange={() => onCheckboxChange(s.id)}
                  style={{ marginRight: 10 }}
                />
                {s.nom}
              </label>
            ))}
          </div>
          <div style={{ textAlign: "center", marginTop: 10 }}>
          <button
            onClick={onDemarrer}
            disabled={symptomesPresents.length === 0}
            style={{...boutonStyle(symptomesPresents.length === 0 ? "#ccc" : "#2c3e50"),width: "90%", marginTop: 10}}
            onMouseOver={boutonHover}
            onMouseOut={boutonOut}
          >
            Commencer ({symptomesPresents.length} symptôme(s))
          </button>
          </div>
        </div>
    </div>
  );
}
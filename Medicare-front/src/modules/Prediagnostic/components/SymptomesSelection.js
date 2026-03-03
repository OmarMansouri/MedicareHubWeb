
import { boutonStyle, boutonHover, boutonOut } from "../styles/styles";

export default function SymptomeSelection({ symptomes, symptomesPresents, onCheckboxChange, onDemarrer }) {
  return (
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
              onChange={() => onCheckboxChange(s.id)}
              style={{ marginRight: 10 }}
            />
            {s.nom}
          </label>
        ))}
      </div>
      <button
        onClick={onDemarrer}
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
  );
}
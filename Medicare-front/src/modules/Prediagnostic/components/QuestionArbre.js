
import { boutonStyle, boutonHover, boutonOut } from "../styles/styles.js";

export default function QuestionArbre({ questionCourante, repondre }) {
  return (
    <div style={{ marginTop: 30, textAlign: "center" }}>
      <h3 style={{ marginBottom: 20,fontFamily: "Georgia, serif", fontSize: 25, color: "#2c3e50" }}>{questionCourante.texte}</h3>
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
        style={boutonStyle("#6c757d")}
        onMouseOver={boutonHover}
        onMouseOut={boutonOut}
      >
        Non
      </button>
    </div>
  );
}

import { boutonStyle, boutonHover, boutonOut } from "../styles/styles.js";

export default function QuestionArbre({ questionCourante, repondre }) {
  return (
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
  );
}
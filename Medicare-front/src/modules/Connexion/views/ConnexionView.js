import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { connexion } from "../api/patientApi";

export default function ConnexionView() {

  const [email, setEmail] = useState("");
  const [motDePasse, setMotDePasse] = useState("");
  const [erreur, setErreur] = useState(null);
  const navigate = useNavigate();

  const handleConnexion = async () => {
    try {
      const data = await connexion(email, motDePasse);
      localStorage.setItem("patient", JSON.stringify(data));
      navigate("/");
    } catch (err) {
      setErreur("Email ou mot de passe incorrect");
    }
  };


  return (
    <div style={{ maxWidth: 400, margin: "40px auto", fontFamily: "Arial, sans-serif" }}>
      <h2 style={{
        textAlign: "center",
        fontFamily: "Georgia, serif",
        color: "#1a3c5e",
        letterSpacing: 3,
        textTransform: "uppercase",
        fontSize: 28
      }}>🩺 Connexion</h2>

      <div style={{ display: "flex", flexDirection: "column", gap: 12, marginTop: 30 }}>
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={e => setEmail(e.target.value)}
          style={{ padding: 10, borderRadius: 5, border: "1px solid #b8d4f0", fontFamily: "Century Gothic, sans-serif" }}
        />
        <input
          type="password"
          placeholder="Mot de passe"
          value={motDePasse}
          onChange={e => setMotDePasse(e.target.value)}
          style={{ padding: 10, borderRadius: 5, border: "1px solid #b8d4f0", fontFamily: "Century Gothic, sans-serif" }}
        />
        {erreur && (
          <p style={{ color: "red", textAlign: "center", fontStyle: "italic" }}>
            {erreur}
          </p>
        )}
        <div style={{ textAlign: "center" }}>
          <button
            onClick={handleConnexion}
            style={{ padding: "10px 25px", backgroundColor: "#0d6efd", color: "white", border: "none", borderRadius: 5, cursor: "pointer", fontFamily: "Century Gothic, sans-serif", fontWeight: "bold" }}
          >
            Se connecter
          </button>
        </div>
      </div>
    </div>
  );
}
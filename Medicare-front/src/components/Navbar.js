import React from "react";
import { Link, useLocation,useNavigate } from "react-router-dom";

export default function Navbar() {
  const location = useLocation(); 
  const navigate = useNavigate();
  const linkClass = (path) =>
    `nav-link ${
      location.pathname === path ? "active fw-bold text-primary" : ""
    }`;
  const patient = JSON.parse(localStorage.getItem("patient"));
  const handleDeconnexion = () => { localStorage.removeItem("patient"); navigate("/");};  

  return (
    <nav className="navbar navbar-expand-lg bg-light shadow-sm py-3">
      <div className="container justify-content-center">
        <ul className="nav">
          <li className="nav-item">
            <Link className={linkClass("/")} to="/">
              Home
            </Link>
          </li>

          <li className="nav-item">
<Link className={linkClass("/dashboard")} to="/dashboard">              Maps
            </Link>
          </li>

          <li className="nav-item">
            <Link className={linkClass("/prediagnostic")} to="/prediagnostic">
              Prédiagnostic
            </Link>
          </li>
          <li className="nav-item">
  <Link className={linkClass("/risque")} to="/risque">
    Risque
  </Link>
</li>
          <li className="nav-item">
            <Link className={linkClass("/Antecedents")} to="/Antecedents">
              Antécédents
            </Link>
          </li>

        </ul>

        {patient ? (
          <div style={{ display: "flex", alignItems: "center", gap: 8 }}>
            <img
              src="https://cdn-icons-png.flaticon.com/512/149/149071.png"
              alt="avatar"
              style={{ width: 35, height: 35, borderRadius: "50%" }}
            />
            <span style={{ fontWeight: "bold", color: "#2c3e50" }}>{patient.prenom}</span>
            <button onClick={handleDeconnexion} className="btn btn-outline-danger btn-sm ms-2">
              Déconnexion
            </button>
          </div>
        ) : (
          <Link className={linkClass("/connexion")} to="/connexion">Connexion</Link>
        )}

      </div>
    </nav>
  );
}

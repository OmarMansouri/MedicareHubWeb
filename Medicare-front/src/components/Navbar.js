import React from "react";
import { Link, useLocation } from "react-router-dom";

export default function Navbar() {
  const location = useLocation(); // pour savoir quelle page est active

  const linkClass = (path) =>
    `nav-link ${
      location.pathname === path ? "active fw-bold text-primary" : ""
    }`;

  return (
    <nav className="navbar navbar-expand-lg bg-light shadow-sm py-3">
      <div className="container justify-content-center">
        <ul className="nav">
          <li className="nav-item">
            <Link className={linkClass("/")} to="/">
              🏠 Home
            </Link>
          </li>

          <li className="nav-item">
            <Link className={linkClass("/dashboard")} to="/dashboard">
              🌍 Dashboard
            </Link>
          </li>
        </ul>
      </div>
    </nav>
  );
}

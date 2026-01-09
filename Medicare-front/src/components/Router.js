import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import App from "./App"; // page d'accueil ou autre
import Navbar from "./Navbar"; // ta barre de navigation
import NotFound from "./NotFound"; // page 404
import Dashboard from "../pages/Dashboard"; // ton nouveau tableau de bord

export default function Router() {
  return (
    <BrowserRouter>
      <Navbar />

      <Routes>
        {/* 🏠 Page d'accueil */}
        <Route path="/" element={<App />} />

        {/* 🌍 Tableau de bord environnemental (Carte + Air + Météo + Centrales) */}
        <Route path="/dashboard" element={<Dashboard />} />

        {/* 🔍 Erreur 404 */}
        <Route path="*" element={<NotFound />} />
      </Routes>
    </BrowserRouter>
  );
}

import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import App from "./App";
import Navbar from "./Navbar";
import NotFound from "./NotFound";
import Maps from "./Maps";
//import Prediagnostic from "./Prediagnostic";
import Dashboard from "../pages/Dashboard";
import Risque from "./Risque";
import PrediagnosticView from "../modules/Prediagnostic/views/PrediagnosticView";
import ConnexionView from "../modules/Connexion/views/ConnexionView";



export default function Router() {
  return (
    <BrowserRouter>
      <Navbar />
      <Routes>
        <Route path="/" element={<App />} />
        <Route path="/maps" element={<Maps />} />
        {/*<Route path="/prediagnostic" element={<Prediagnostic />} />*/}
        <Route path="/risque" element={<Risque />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="*" element={<NotFound />} />
        <Route path="/prediagnostic" element={<PrediagnosticView />} />
        <Route path="/connexion" element={<ConnexionView />} />


      </Routes>
    </BrowserRouter>
  );
}

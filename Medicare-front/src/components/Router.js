import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import App from "./App";
import Navbar from "./Navbar";
import NotFound from "./NotFound";
import Maps from "./Maps";
import Prediagnostic from "./Prediagnostic";
import Dashboard from "../pages/Dashboard";
import Risque from "./Risque";


export default function Router() {
  return (
    <BrowserRouter>
      <Navbar />
      <Routes>
        <Route path="/" element={<App />} />
        <Route path="/maps" element={<Maps />} />
        <Route path="/prediagnostic" element={<Prediagnostic />} />
        <Route path="/risque" element={<Risque />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="*" element={<NotFound />} />
        

      </Routes>
    </BrowserRouter>
  );
}

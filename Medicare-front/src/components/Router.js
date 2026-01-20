import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import App from "./App"; 
import Navbar from "./Navbar"; 
import NotFound from "./NotFound"; 
import Dashboard from "../pages/Dashboard"; 

export default function Router() {
  return (
    <BrowserRouter>
      <Navbar />

      <Routes>
        {}
        <Route path="/" element={<App />} />

        {}
        <Route path="/dashboard" element={<Dashboard />} />

        {}
        <Route path="*" element={<NotFound />} />
      </Routes>
    </BrowserRouter>
  );
}

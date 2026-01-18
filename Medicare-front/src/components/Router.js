import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import App from "./App";
import Sample from "./Sample";
import Navbar from "./Navbar";
import NotFound from "./NotFound";
import Maps from "./Maps";
import Prediagnostic from "./Prediagnostic";

export default function Router() {
  return (
    <BrowserRouter>
      <div>
        <Navbar />
        <Routes>
          <Route path="/" element={<App />} />
          <Route path="/sample" element={<Sample />} />
          <Route path="*" element={<NotFound />} />
          <Route path="/maps" element={<Maps />} />
          <Route path="/prediagnostic" element={<Prediagnostic />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

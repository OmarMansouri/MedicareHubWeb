import React from 'react';
import ReactDOM from 'react-dom/client';
import './styles/index.css';
import Router from "./components/Router";
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/css/bootstrap.min.css';

const root = ReactDOM.createRoot(document.getElementById('root'));
localStorage.removeItem("patient");
root.render(
    <React.StrictMode>
        <Router />
    </React.StrictMode>
);

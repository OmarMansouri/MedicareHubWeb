import React, { useState, useEffect } from "react";

function RiskCard(props) {

  const [risk, setRisk] = useState(null);

  useEffect(function () {

    if (props.center == null) {
      return;
    }
    var lat = props.center[0];
    var lon = props.center[1];
    var url = "http://localhost:8081/api/risk/coords?lat=" + lat + "&lon=" + lon + "&radiusKm=5";
    fetch(url)
      .then(function (response) {
        return response.json();
      })
      .then(function (data) {
        setRisk(data);
      })
      .catch(function (error) {
        console.log(error);
      });

  }, [props.center]);

  if (risk == null) {
    return (
      <div className="card">
        Pas de données de risque
      </div>
    );
  }

  return (
    <div className="card">
      <h3>Risque Score</h3>

      <p>Niveau : {risk.level}</p>
      <p>Pollution : {risk.pollutionScore} / 3</p>
      <p>Météo : {risk.meteoScore} / 3</p>
      <p>Industrie : {risk.industryScore} / 3</p>
      <p>Total : {risk.totalScore} / 9</p>

    </div>
  );
}

export default RiskCard;
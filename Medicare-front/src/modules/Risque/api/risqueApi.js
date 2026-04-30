//calculer le risque
export function calculerRisque(idPatient) {
 return fetch("http://localhost:8081/risque/patient/" + idPatient)
.then(function(res) { return res.json(); });
 }

//enregistrer le résultat
export function enregistrerRisque(idPatient, podium) {
    
return fetch("http://localhost:8081/risque/patient/" + idPatient + "/enregistrer", {
 method: "POST",
  headers: { "Content-Type": "application/json" },
   body: JSON.stringify({ podium: podium }),
})
 .then(function(res) { return res.json(); });
 }


// récupérer la dernière évaluation
export function getDerniereEvaluation(idPatient) {
   return fetch("http://localhost:8081/risque/patient/" + idPatient + "/derniere-evaluation")
   .then(function(res) { return res.json(); });
 }
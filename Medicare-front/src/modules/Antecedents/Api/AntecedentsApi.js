export function saveAntecedents(idPatient, diseaseIds, typeRelation) {
return fetch("http://172.31.250.86:8081/antecedents/patient/" + idPatient, { // correction ip
method: "POST",
 headers: { "Content-Type": "application/json" },
  body: JSON.stringify({
  diseaseIds: diseaseIds,
typeRelation: typeRelation,
   }),
  }).then(function(res) {
  return res.json();
  
  });
}
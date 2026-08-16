export function saveAntecedents(idPatient, diseaseIds, typeRelation) {
return fetch("http://localhost:8081/antecedents/patient/" + idPatient, { // correction ip
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
  //enregistrer les facteurs positifs du patient
  export function saveFacteurs(idPatient, facteurIds){
    return fetch ("http://localhost:8081/facteurs/patient/" + idPatient,{
      method : "POST",
      headers : {"Content-Type" :"application/json"},
        body : JSON.stringify({ facteurIds : facteurIds}),
      })
      .then(function(res){ 
        return res.json();
      });
    }

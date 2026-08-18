import axios from "axios";

export async function getSymptomes() {
  const res = await axios.get('/api/symptomes');
  return res.data;
}

export async function prochaineQuestion(symptomesPresents, symptomesAbsents) {
  const patient = JSON.parse(localStorage.getItem("patient"));
  const res = await axios.post('/api/prochaineQuestion', {
    symptomesPresents,
    symptomesAbsents,
    patientId: patient ? patient.idPatient : null
  });
  return res.data;
}
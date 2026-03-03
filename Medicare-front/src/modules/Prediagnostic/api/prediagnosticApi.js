import axios from "axios";

export async function getSymptomes() {
  const res = await axios.get('/api/symptomes');
  return res.data;
}

export async function prochaineQuestion(symptomesPresents, symptomesAbsents) {
  const res = await axios.post('/api/prochaineQuestion', {
    symptomesPresents,
    symptomesAbsents
  });
  return res.data;
}
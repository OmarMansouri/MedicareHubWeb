
export function fetchRecommandations(idPatient) {
    return fetch(`http://localhost:8081/api/recommendations/patient/${idPatient}`)
      .then((res) => res.json());
  }
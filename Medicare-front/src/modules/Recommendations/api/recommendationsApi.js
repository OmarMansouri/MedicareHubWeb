
export function fetchRecommandations(idPatient) {
    return fetch(`http://172.31.250.86:8081/api/recommendations/patient/${idPatient}`)
      .then((res) => res.json());
  }
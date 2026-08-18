
export function fetchRecommandations(idPatient) {
    return fetch(`/api/recommendations/patient/${idPatient}`)
      .then((res) => res.json());
  }
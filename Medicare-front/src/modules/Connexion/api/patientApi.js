import axios from "axios";

export async function connexion(email, motDePasse){
    const res = await axios.post ('http://172.31.250.86:8081/api/patient/connexion',{email,motDePasse});
    return res.data;
}

export async function inscription(nom, prenom, email, motDePasse) {
  const res = await axios.post('http://172.31.250.86:8081/api/patient/inscription', {nom,prenom, email, motDePasse});
  return res.data;
  
}
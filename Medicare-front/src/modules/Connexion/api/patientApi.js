import axios from "axios";

export async function connexion(email, motDePasse){
    const res = await axios.post ('/api/patient/connexion',{email,motDePasse});
    return res.data;
}

export async function inscription(nom, prenom, email, motDePasse) {
  const res = await axios.post('/api/patient/inscription', {nom,prenom, email, motDePasse});
  return res.data;
  
}
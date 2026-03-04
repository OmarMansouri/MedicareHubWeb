export const boutonStyle = (bg) => ({
  padding: "10px 25px",
  margin: "10px",
  backgroundColor: bg,
  color: "white",
  border: "none",
  borderRadius: 5,
  cursor: "pointer",
  fontWeight: "bold",
  transition: "0.2s",
  fontFamily: "Century Gothic, sans-serif"
});

export const boutonHover = (e) => { 
  e.target.style.transform = "scale(1.05)";
  e.target.style.boxShadow = "0 3px 8px rgba(0,0,0,0.2)"; };

export const boutonOut = (e) => { 
  e.target.style.transform = "scale(1)";
  e.target.style.boxShadow = "none";};
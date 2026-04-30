import React from "react";

export default function RecommandationsList({ recommandations }) {
    if (!recommandations || recommandations.length === 0) {
      return (
        <p style={{ color: "#888", fontStyle: "italic", marginTop: 15 }}>
          Aucune recommandation disponible pour ce patient.
        </p>
      );
    }

    return (
      <div>
        {recommandations.map((r, index) => (
          <div key={index} style={styles.recoCard}>
            <p style={styles.contenu}>{r.contenu}</p>
          </div>
        ))}
      </div>
    );
}

const styles = {
    recoCard: {backgroundColor: "#f8fbff", border: "1px solid #d6e7f5",borderRadius: 8,padding: 12,marginBottom: 8,},
    contenu: {margin: 0, fontSize: 15, color: "#333", fontFamily: "Century Gothic, Gill Sans, Arial, sans-serif", lineHeight: 1.5,},
};
package medicare.back.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "facteur_positif")
public class FacteurPositif {

@Id
@GeneratedValue( strategy = GenerationType.IDENTITY)
private int id;

@Column (name = "nom")
private String nom;

@Column(name = "reduction")
private int reduction;

public int getId () {
    return id;
}

public String getNom () {
    return nom;
}

public int getReduction () { 
    return reduction;
}
}
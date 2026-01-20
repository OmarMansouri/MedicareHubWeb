package medicare.back.models;

public class Plant {
    public String name; // nom de la centrale
    public String country; // code pays
    public String country_long; // nom complet du pays 
    public String primaryFuel; // type d’énergie
    public Double capacityMw; // puissance
    public Double latitude;
    public Double longitude;
    public Double emissionCo2Tons; // estimation CO₂
}
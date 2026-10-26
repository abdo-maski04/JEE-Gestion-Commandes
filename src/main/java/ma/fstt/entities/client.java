package ma.fstt.entities;

public class client {

    private Long idClient;
    private String nom;
    private String prenom;
    private String adresse;

    public client() {
    }

    public String getPrenom() {
        return prenom;
    }

    public Long getIdClient() {
        return idClient;
    }

    public String getNom() {
        return nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
}

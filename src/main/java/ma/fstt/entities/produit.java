package ma.fstt.entities;

import java.math.BigDecimal;

public class produit {

    private Long idProduit;
    private String nom;
    private String description;
    private Long stock;
    private BigDecimal prix;

    public produit() {
    }

    public Long getIdProduit() {
        return idProduit;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public Long getStock() {
        return stock;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setIdProduit(Long idProduit) {
        this.idProduit = idProduit;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }
}

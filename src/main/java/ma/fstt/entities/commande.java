package ma.fstt.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class commande {

    private Long idComm;
    private BigDecimal prixTotal;
    private Long idClient;
    private LocalDate dateCommande;

    public commande() {
    }

    public Long getIdComm() {
        return idComm;
    }

    public BigDecimal getPrixTotal() {
        return prixTotal;
    }

    public Long getIdClient() {
        return idClient;
    }

    public LocalDate getDateCommande() {
        return dateCommande;
    }

    public void setIdComm(Long idComm) {
        this.idComm = idComm;
    }

    public void setPrixTotal(BigDecimal prixTotal) {
        this.prixTotal = prixTotal;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public void setDateCommande(LocalDate dateCommande) {
        this.dateCommande = dateCommande;
    }
}

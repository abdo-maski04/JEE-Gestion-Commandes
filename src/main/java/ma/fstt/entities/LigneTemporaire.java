package ma.fstt.entities;

import java.math.BigDecimal;

public class LigneTemporaire {
    private int index;
    private Long idProduit;
    private String nomProduit;
    private Long qte;
    private BigDecimal pu;
    private BigDecimal sousTotal;

    public LigneTemporaire() {
    }

    public LigneTemporaire(int index, Long idProduit, String nomProduit, Long qte, BigDecimal pu) {
        this.index = index;
        this.idProduit = idProduit;
        this.nomProduit = nomProduit;
        this.qte = qte;
        this.pu = pu;
        this.sousTotal = pu.multiply(new BigDecimal(qte));
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Long getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(Long idProduit) {
        this.idProduit = idProduit;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public Long getQte() {
        return qte;
    }

    public void setQte(Long qte) {
        this.qte = qte;
    }

    public BigDecimal getPu() {
        return pu;
    }

    public void setPu(BigDecimal pu) {
        this.pu = pu;
    }

    public BigDecimal getSousTotal() {
        return sousTotal;
    }

    public void setSousTotal(BigDecimal sousTotal) {
        this.sousTotal = sousTotal;
    }
}

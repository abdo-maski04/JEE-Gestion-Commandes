package ma.fstt.entities;

import java.math.BigDecimal;

public class ligne_commande {

    private Long idLigneComm;
    private Long qte;
    private BigDecimal pu;
    private Long idProduit;
    private Long idCom;

    public ligne_commande() {
    }

    public Long getIdLigneComm() {
        return idLigneComm;
    }

    public Long getQte() {
        return qte;
    }

    public BigDecimal getPu() {
        return pu;
    }

    public Long getIdProduit() {
        return idProduit;
    }

    public Long getIdCom() {
        return idCom;
    }

    public void setIdLigneComm(Long idLigneComm) {
        this.idLigneComm = idLigneComm;
    }

    public void setQte(Long qte) {
        this.qte = qte;
    }

    public void setPu(BigDecimal pu) {
        this.pu = pu;
    }

    public void setIdProduit(Long idProduit) {
        this.idProduit = idProduit;
    }

    public void setIdCom(Long idCom) {
        this.idCom = idCom;
    }
}

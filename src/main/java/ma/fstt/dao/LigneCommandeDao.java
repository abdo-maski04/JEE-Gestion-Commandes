package ma.fstt.dao;

import ma.fstt.entities.ligne_commande;
import ma.fstt.service.IDAO;
import ma.fstt.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LigneCommandeDao implements IDAO<ligne_commande, Long> {

    public List<ligne_commande> findByCommande(Long idCommande) throws SQLException {
        String sql = "select * from ligne_commande where id_com = ? order by id_ligne_comm";
        List<ligne_commande> liste = new ArrayList<ligne_commande>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, idCommande);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ligne_commande lc = new ligne_commande();
                    lc.setIdLigneComm(rs.getLong("id_ligne_comm"));
                    lc.setQte(rs.getLong("qte"));
                    lc.setPu(rs.getBigDecimal("pu"));
                    lc.setIdProduit(rs.getLong("id_produit"));
                    lc.setIdCom(rs.getLong("id_com"));
                    liste.add(lc);
                }
            }
        }
        return liste;
    }

    public List<ligne_commande> findAll() throws SQLException {
        String sql = "select * from ligne_commande order by id_ligne_comm";
        List<ligne_commande> liste = new ArrayList<ligne_commande>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                ligne_commande lc = new ligne_commande();
                lc.setIdLigneComm(rs.getLong("id_ligne_comm"));
                lc.setQte(rs.getLong("qte"));
                lc.setPu(rs.getBigDecimal("pu"));
                lc.setIdProduit(rs.getLong("id_produit"));
                lc.setIdCom(rs.getLong("id_com"));
                liste.add(lc);
            }
        }
        return liste;
    }

    public void save(ligne_commande ligneCommande) throws SQLException {
        String sql = "INSERT INTO ligne_commande (qte, pu, id_produit, id_com) VALUES (?, ?, ?, ?)";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setLong(1, ligneCommande.getQte());
            ps.setBigDecimal(2, ligneCommande.getPu());
            ps.setLong(3, ligneCommande.getIdProduit());
            ps.setLong(4, ligneCommande.getIdCom());
            ps.executeUpdate();
        }
    }

    private ligne_commande mapRow(ResultSet rs) throws SQLException {
        ligne_commande lc = new ligne_commande();
        lc.setIdLigneComm(rs.getLong("id_ligne_comm"));
        lc.setQte(rs.getLong("qte"));
        lc.setPu(rs.getBigDecimal("pu"));
        lc.setIdProduit(rs.getLong("id_produit"));
        lc.setIdCom(rs.getLong("id_com"));
        return lc;
    }

    @Override
    public ligne_commande findById(Long id) throws SQLException {
        String sql = "SELECT id_ligne_comm, qte, pu, id_produit, id_com FROM ligne_commande WHERE id_ligne_comm = ?";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    @Override
    public void update(ligne_commande ligneCommande) throws SQLException {
        String sql = "UPDATE ligne_commande SET qte = ?, pu = ?, id_produit = ?, id_com = ? WHERE id_ligne_comm = ?";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setLong(1, ligneCommande.getQte());
            ps.setBigDecimal(2, ligneCommande.getPu());
            ps.setLong(3, ligneCommande.getIdProduit());
            ps.setLong(4, ligneCommande.getIdCom());
            ps.setLong(5, ligneCommande.getIdLigneComm());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(ligne_commande ligneCommande) throws SQLException {
        Long id = ligneCommande.getIdLigneComm();
        String sql = "DELETE FROM ligne_commande WHERE id_ligne_comm = ?";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}

package ma.fstt.dao;

import ma.fstt.entities.produit;
import ma.fstt.service.IDAO;
import ma.fstt.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProduitDao implements IDAO<produit, Long> {

    public List<produit> findAll() throws SQLException {
        String sql = "select * from produit order by id_produit";
        List<produit> liste = new ArrayList<produit>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                produit p = new produit();
                p.setIdProduit(rs.getLong("id_produit"));
                p.setNom(rs.getString("nom"));
                p.setDescription(rs.getString("description"));
                p.setStock(rs.getLong("stock"));
                p.setPrix(rs.getBigDecimal("prix"));
                liste.add(p);
            }
        }
        return liste;
    }

    public void save(produit produit) throws SQLException {
        String sql = "INSERT INTO produit (nom, description, stock, prix) VALUES (?, ?, ?, ?)";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, produit.getNom());
            ps.setString(2, produit.getDescription());
            ps.setLong(3, produit.getStock());
            ps.setBigDecimal(4, produit.getPrix());
            ps.executeUpdate();
        }
    }

    private produit mapRow(ResultSet rs) throws SQLException {
        produit p = new produit();
        p.setIdProduit(rs.getLong("id_produit"));
        p.setNom(rs.getString("nom"));
        p.setDescription(rs.getString("description"));
        p.setStock(rs.getLong("stock"));
        p.setPrix(rs.getBigDecimal("prix"));
        return p;
    }

    @Override
    public produit findById(Long id) throws SQLException {
        String sql = "SELECT id_produit, nom, description, stock, prix FROM produit WHERE id_produit = ?";

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
    public void update(produit produit) throws SQLException {
        String sql = "UPDATE produit SET nom = ?, description = ?, stock = ?, prix = ? WHERE id_produit = ?";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, produit.getNom());
            ps.setString(2, produit.getDescription());
            ps.setLong(3, produit.getStock());
            ps.setBigDecimal(4, produit.getPrix());
            ps.setLong(5, produit.getIdProduit());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(produit produit) throws SQLException {
        Long id = produit.getIdProduit();
        String sql = "DELETE FROM produit WHERE id_produit = ?";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}

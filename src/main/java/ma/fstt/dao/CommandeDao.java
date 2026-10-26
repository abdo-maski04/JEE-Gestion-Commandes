package ma.fstt.dao;

import ma.fstt.entities.commande;
import ma.fstt.service.IDAO;
import ma.fstt.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CommandeDao implements IDAO<commande, Long> {

    public List<commande> findAll() throws SQLException {
        String sql = "select * from commande order by date_commande desc";
        List<commande> liste = new ArrayList<commande>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                commande c = new commande();
                c.setIdComm(rs.getLong("id_comm"));
                c.setPrixTotal(rs.getBigDecimal("prix_total"));
                c.setIdClient(rs.getLong("id_client"));
                c.setDateCommande(rs.getObject("date_commande", LocalDate.class));
                liste.add(c);
            }
        }
        return liste;
    }

    public void save(commande commande) throws SQLException {
        String sql = "INSERT INTO commande (prix_total, id_client, date_commande) VALUES (?, ?, ?)";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setBigDecimal(1, commande.getPrixTotal());
            ps.setLong(2, commande.getIdClient());
            ps.setObject(3, commande.getDateCommande());
            ps.executeUpdate();
        }
    }

    private commande mapRow(ResultSet rs) throws SQLException {
        commande c = new commande();
        c.setIdComm(rs.getLong("id_comm"));
        c.setPrixTotal(rs.getBigDecimal("prix_total"));
        c.setIdClient(rs.getLong("id_client"));
        c.setDateCommande(rs.getObject("date_commande", LocalDate.class));
        return c;
    }

    @Override
    public commande findById(Long id) throws SQLException {
        String sql = "SELECT id_comm, prix_total, id_client, date_commande FROM commande WHERE id_comm = ?";

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
    public void update(commande commande) throws SQLException {
        String sql = "UPDATE commande SET prix_total = ?, id_client = ?, date_commande = ? WHERE id_comm = ?";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setBigDecimal(1, commande.getPrixTotal());
            ps.setLong(2, commande.getIdClient());
            ps.setObject(3, commande.getDateCommande());
            ps.setLong(4, commande.getIdComm());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(commande commande) throws SQLException {
        Long id = commande.getIdComm();
        String sql = "DELETE FROM commande WHERE id_comm = ?";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}

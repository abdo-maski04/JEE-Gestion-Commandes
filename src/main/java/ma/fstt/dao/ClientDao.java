package ma.fstt.dao;

import ma.fstt.entities.client;
import ma.fstt.service.IDAO;
import ma.fstt.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDao implements IDAO<client,Long> {

    public List<client> findAll() throws SQLException {
        String sql="select * from client order by id_client";
        List<client> liste=new ArrayList<client>();
        try(Connection connection= DBConnection.getConnection();
            PreparedStatement ps=connection.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
        ){
            while(rs.next()){
                client c=new client();
                c.setIdClient(rs.getLong("id_client"));
                c.setNom(rs.getString("nom"));
                c.setPrenom(rs.getString("prenom"));
                c.setAdresse(rs.getString("adresse"));
                liste.add(c);
            }
        }
        return liste;
    }
    public void save(client client) throws SQLException {
        String sql = "INSERT INTO client (nom, prenom, adresse) VALUES (?, ?, ?)";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setString(3, client.getAdresse());
            ps.executeUpdate();
        }
    }


    private client mapRow(ResultSet rs) throws SQLException {
        client c = new client();
        c.setIdClient(rs.getLong("id_client"));
        c.setNom(rs.getString("nom"));
        c.setPrenom(rs.getString("prenom"));
        c.setAdresse(rs.getString("adresse"));
        return c;
    }

    @Override
    public client findById(Long id) throws SQLException {
        String sql = "SELECT id_client, nom, prenom, adresse FROM client WHERE id_client = ?";

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
    public void update(client client) throws SQLException {
        String sql = "UPDATE client SET nom = ?, prenom = ?, adresse = ? WHERE id_client = ?";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setString(3, client.getAdresse());
            ps.setLong(4, client.getIdClient());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(client client) throws SQLException {
        Long id = client.getIdClient();
        String sql = "DELETE FROM client WHERE id_client = ?";

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}

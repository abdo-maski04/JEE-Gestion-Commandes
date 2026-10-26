package servlets;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.fstt.entities.client;
import ma.fstt.dao.ClientDao;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/clients")
public class ClientServlet extends HttpServlet {

    private final ClientDao clientDao = new ClientDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            if ("edit".equals(req.getParameter("action"))) {
                Long id = lireId(req);
                if (id != null) {
                    req.setAttribute("clientAModifier", clientDao.findById(id));
                }
            }
            req.setAttribute("clients", clientDao.findAll());
            req.getRequestDispatcher("/WEB-INF/views/clients.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Erreur lors de la lecture des clients", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        try {
            if ("delete".equals(action)) {
                Long id = lireId(req);
                if (id != null) {
                    try {
                        clientDao.delete(clientDao.findById(id));
                    } catch (SQLException e) {
                        if ("23503".equals(e.getSQLState())) {
                            req.setAttribute("erreur",
                                    "Impossible de supprimer ce client : il a des commandes.");
                            doGet(req, resp);
                            return;
                        }
                        throw e;
                    }
                }
            } else {
                String nom = req.getParameter("nom");
                String prenom = req.getParameter("prenom");
                String adresse = req.getParameter("adresse");

                if (nom == null || nom.isBlank() || prenom == null || prenom.isBlank()) {
                    req.setAttribute("erreur", "Le nom et le prénom sont obligatoires.");
                    doGet(req, resp);
                    return;
                }

                client c = new client();
                c.setNom(nom.trim());
                c.setPrenom(prenom.trim());
                c.setAdresse(adresse == null ? "" : adresse.trim());

                if ("update".equals(action)) {
                    Long id = lireId(req);
                    if (id != null) {
                        c.setIdClient(id);
                        clientDao.update(c);
                    }
                } else {
                    clientDao.save(c);
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Erreur d'accès à la base de données", e);
        }

        resp.sendRedirect(req.getContextPath() + "/clients");
    }

    private Long lireId(HttpServletRequest req) {
        try {
            return Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
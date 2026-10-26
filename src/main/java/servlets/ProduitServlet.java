package servlets;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.fstt.entities.produit;
import ma.fstt.dao.ProduitDao;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/produits")
public class ProduitServlet extends HttpServlet {

    private final ProduitDao produitDao = new ProduitDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            if ("edit".equals(req.getParameter("action"))) {
                Long id = lireId(req);
                if (id != null) {
                    req.setAttribute("produitAModifier", produitDao.findById(id));
                }
            }
            req.setAttribute("produits", produitDao.findAll());
            req.getRequestDispatcher("/WEB-INF/views/produits.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Erreur lors de la lecture des produits", e);
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
                        produitDao.delete(produitDao.findById(id));
                    } catch (SQLException e) {
                        if ("23503".equals(e.getSQLState())) {
                            req.setAttribute("erreur",
                                    "Impossible de supprimer ce produit : il est utilisé dans des commandes.");
                            doGet(req, resp);
                            return;
                        }
                        throw e;
                    }
                }
            } else {
                String nom = req.getParameter("nom");
                String description = req.getParameter("description");
                String stockStr = req.getParameter("stock");
                String prixStr = req.getParameter("prix");

                if (nom == null || nom.isBlank() || description == null || description.isBlank()) {
                    req.setAttribute("erreur", "Le nom et la description sont obligatoires.");
                    doGet(req, resp);
                    return;
                }

                produit p = new produit();
                p.setNom(nom.trim());
                p.setDescription(description.trim());
                
                try {
                    p.setStock(stockStr != null && !stockStr.isBlank() ? Long.parseLong(stockStr.trim()) : 0L);
                    p.setPrix(prixStr != null && !prixStr.isBlank() ? new java.math.BigDecimal(prixStr.trim()) : java.math.BigDecimal.ZERO);
                } catch (NumberFormatException e) {
                    req.setAttribute("erreur", "Le stock et le prix doivent être des nombres valides.");
                    doGet(req, resp);
                    return;
                }

                if ("update".equals(action)) {
                    Long id = lireId(req);
                    if (id != null) {
                        p.setIdProduit(id);
                        produitDao.update(p);
                    }
                } else {
                    produitDao.save(p);
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Erreur d'accès à la base de données", e);
        }

        resp.sendRedirect(req.getContextPath() + "/produits");
    }

    private Long lireId(HttpServletRequest req) {
        try {
            return Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

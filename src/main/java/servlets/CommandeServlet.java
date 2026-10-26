package servlets;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.fstt.entities.LigneTemporaire;
import ma.fstt.entities.commande;
import ma.fstt.entities.ligne_commande;
import ma.fstt.entities.produit;
import ma.fstt.dao.ClientDao;
import ma.fstt.dao.CommandeDao;
import ma.fstt.dao.LigneCommandeDao;
import ma.fstt.dao.ProduitDao;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/commandes")
public class CommandeServlet extends HttpServlet {

    private final CommandeDao commandeDao = new CommandeDao();
    private final ClientDao clientDao = new ClientDao();
    private final LigneCommandeDao ligneCommandeDao = new LigneCommandeDao();
    private final ProduitDao produitDao = new ProduitDao();
    private static final String SESSION_LIGNES = "lignesTemporaires";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            if ("edit".equals(req.getParameter("action"))) {
                Long id = lireId(req);
                if (id != null) {
                    req.setAttribute("commandeAModifier", commandeDao.findById(id));
                    req.setAttribute("lignesCommande", ligneCommandeDao.findByCommande(id));
                }
            }
            req.setAttribute("commandes", commandeDao.findAll());
            req.setAttribute("clients", clientDao.findAll());
            req.setAttribute("produits", produitDao.findAll());
            
            HttpSession session = req.getSession();
            @SuppressWarnings("unchecked")
            List<LigneTemporaire> lignes = (List<LigneTemporaire>) session.getAttribute(SESSION_LIGNES);
            if (lignes != null && !lignes.isEmpty()) {
                req.setAttribute("lignesTemporaires", lignes);
                BigDecimal total = lignes.stream()
                    .map(LigneTemporaire::getSousTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                req.setAttribute("totalTemporaire", total);
            }
            
            req.getRequestDispatcher("/WEB-INF/views/commandes.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Erreur lors de la lecture des commandes", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        HttpSession session = req.getSession();

        try {
            if ("delete".equals(action)) {
                Long id = lireId(req);
                if (id != null) {
                    commandeDao.delete(commandeDao.findById(id));
                }
                resp.sendRedirect(req.getContextPath() + "/commandes");
                return;
            }
            
            if ("addLigne".equals(action)) {
                ajouterLigneTemporaire(req, session);
                doGet(req, resp);
                return;
            }
            
            if ("removeLigne".equals(action)) {
                supprimerLigneTemporaire(req, session);
                doGet(req, resp);
                return;
            }
            
            if ("add".equals(action)) {
                String idClientStr = req.getParameter("id_client");
                String dateCommandeStr = req.getParameter("date_commande");

                if (idClientStr == null || idClientStr.isBlank()) {
                    req.setAttribute("erreur", "Le client est obligatoire.");
                    doGet(req, resp);
                    return;
                }

                @SuppressWarnings("unchecked")
                List<LigneTemporaire> lignes = (List<LigneTemporaire>) session.getAttribute(SESSION_LIGNES);
                if (lignes == null || lignes.isEmpty()) {
                    req.setAttribute("erreur", "Ajoutez au moins un produit à la commande.");
                    doGet(req, resp);
                    return;
                }

                commande c = new commande();
                try {
                    c.setIdClient(Long.parseLong(idClientStr.trim()));
                    c.setDateCommande(dateCommandeStr != null && !dateCommandeStr.isBlank() ? 
                        LocalDate.parse(dateCommandeStr.trim()) : LocalDate.now());
                    
                    BigDecimal total = lignes.stream()
                        .map(LigneTemporaire::getSousTotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                    c.setPrixTotal(total);
                } catch (NumberFormatException e) {
                    req.setAttribute("erreur", "Le client doit être valide.");
                    doGet(req, resp);
                    return;
                }

                commandeDao.save(c);
                
                for (LigneTemporaire lt : lignes) {
                    ligne_commande lc = new ligne_commande();
                    lc.setQte(lt.getQte());
                    lc.setPu(lt.getPu());
                    lc.setIdProduit(lt.getIdProduit());
                    lc.setIdCom(c.getIdComm());
                    ligneCommandeDao.save(lc);
                }
                
                session.removeAttribute(SESSION_LIGNES);
                resp.sendRedirect(req.getContextPath() + "/commandes");
                return;
            }
            
            if ("update".equals(action)) {
                Long id = lireId(req);
                if (id != null) {
                    commande c = new commande();
                    String idClientStr = req.getParameter("id_client");
                    String dateCommandeStr = req.getParameter("date_commande");
                    
                    c.setIdComm(id);
                    c.setIdClient(Long.parseLong(idClientStr.trim()));
                    c.setDateCommande(dateCommandeStr != null && !dateCommandeStr.isBlank() ? 
                        LocalDate.parse(dateCommandeStr.trim()) : LocalDate.now());
                    commandeDao.update(c);
                }
                resp.sendRedirect(req.getContextPath() + "/commandes");
                return;
            }
        } catch (SQLException e) {
            throw new ServletException("Erreur d'accès à la base de données", e);
        }

        resp.sendRedirect(req.getContextPath() + "/commandes");
    }
    
    private void ajouterLigneTemporaire(HttpServletRequest req, HttpSession session) throws SQLException {
        String idProduitStr = req.getParameter("id_produit");
        String qteStr = req.getParameter("qte");
        
        if (idProduitStr == null || idProduitStr.isBlank() || qteStr == null || qteStr.isBlank()) {
            return;
        }
        
        Long idProduit = Long.parseLong(idProduitStr.trim());
        Long qte = Long.parseLong(qteStr.trim());
        
        produit p = produitDao.findById(idProduit);
        if (p == null) {
            return;
        }
        
        @SuppressWarnings("unchecked")
        List<LigneTemporaire> lignes = (List<LigneTemporaire>) session.getAttribute(SESSION_LIGNES);
        if (lignes == null) {
            lignes = new ArrayList<>();
        }
        
        int index = lignes.size();
        LigneTemporaire lt = new LigneTemporaire(index, idProduit, p.getNom(), qte, p.getPrix());
        lignes.add(lt);
        session.setAttribute(SESSION_LIGNES, lignes);
    }
    
    private void supprimerLigneTemporaire(HttpServletRequest req, HttpSession session) {
        String indexStr = req.getParameter("index");
        if (indexStr == null || indexStr.isBlank()) {
            return;
        }
        
        int index = Integer.parseInt(indexStr.trim());
        @SuppressWarnings("unchecked")
        List<LigneTemporaire> lignes = (List<LigneTemporaire>) session.getAttribute(SESSION_LIGNES);
        if (lignes != null && index >= 0 && index < lignes.size()) {
            lignes.remove(index);
            if (lignes.isEmpty()) {
                session.removeAttribute(SESSION_LIGNES);
            } else {
                session.setAttribute(SESSION_LIGNES, lignes);
            }
        }
    }

    private Long lireId(HttpServletRequest req) {
        try {
            return Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

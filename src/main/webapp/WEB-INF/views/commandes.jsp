<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Commandes</title>
    <style>
        :root {
            --ink: #1d2330;
            --muted: #5d6677;
            --line: #d6dae1;
            --accent: #0f6e6a;
            --danger: #b3261e;
        }
        * { box-sizing: border-box; }
        body {
            margin: 0;
            padding: 2rem 1rem;
            font-family: system-ui, -apple-system, "Segoe UI", sans-serif;
            color: var(--ink);
            background: #fff;
            line-height: 1.5;
        }
        main { max-width: 1200px; margin: 0 auto; }
        h1 { margin: 0 0 1.5rem; font-size: 1.75rem; }
        h2 { margin: 0 0 .75rem; font-size: 1.1rem; }
        h3 { margin: 1rem 0 .5rem; font-size: 1rem; color: var(--muted); }

        .erreur {
            margin: 0 0 1.25rem;
            padding: .75rem 1rem;
            border-left: 4px solid var(--danger);
            background: #fdf0ef;
            color: var(--danger);
        }

        form.saisie {
            display: flex;
            flex-wrap: wrap;
            gap: .75rem;
            align-items: flex-end;
            margin-bottom: 2rem;
            padding-bottom: 1.5rem;
            border-bottom: 1px solid var(--line);
        }
        .champ { display: flex; flex-direction: column; gap: .25rem; flex: 1 1 180px; }
        .champ label { font-size: .875rem; color: var(--muted); }
        input[type="text"], input[type="number"], input[type="date"], select {
            padding: .5rem .6rem;
            border: 1px solid var(--line);
            border-radius: 4px;
            font: inherit;
        }
        button, .lien-bouton {
            padding: .5rem .9rem;
            border: 1px solid var(--accent);
            border-radius: 4px;
            background: var(--accent);
            color: #fff;
            font: inherit;
            cursor: pointer;
            text-decoration: none;
        }
        button.danger { background: #fff; color: var(--danger); border-color: var(--danger); }
        .lien-bouton.secondaire { background: #fff; color: var(--ink); border-color: var(--line); }
        :focus-visible { outline: 3px solid #f0b429; outline-offset: 2px; }

        .tableau { overflow-x: auto; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: .6rem .75rem; text-align: left; border-bottom: 1px solid var(--line); }
        th { font-size: .875rem; color: var(--muted); font-weight: 600; }
        td.actions { white-space: nowrap; }
        td.actions form { display: inline; }
        td.actions a { margin-right: .5rem; color: var(--accent); }
        .vide { color: var(--muted); }

        .lignes-commande {
            margin-top: 1rem;
            padding: 1rem;
            background: #f8f9fa;
            border-radius: 4px;
        }
        .lignes-commande table {
            font-size: .9rem;
        }
    </style>
</head>
<body>
<main>
    <h1>Commandes</h1>

    <%-- Message d'erreur déposé par le Servlet --%>
    <c:if test="${not empty erreur}">
        <p class="erreur" role="alert"><c:out value="${erreur}"/></p>
    </c:if>

    <%-- Mode modification si le Servlet a déposé une commande à modifier --%>
    <c:set var="enEdition" value="${not empty commandeAModifier}"/>

    <section aria-labelledby="titre-formulaire">
        <h2 id="titre-formulaire">${enEdition ? 'Modifier la commande' : 'Créer une commande'}</h2>

        <form class="saisie" action="${pageContext.request.contextPath}/commandes" method="post">
            <input type="hidden" name="action" value="${enEdition ? 'update' : 'add'}">
            <c:if test="${enEdition}">
                <input type="hidden" name="id" value="${commandeAModifier.idComm}">
            </c:if>

            <div class="champ">
                <label for="id_client">Client</label>
                <select id="id_client" name="id_client" required>
                    <option value="">-- Sélectionner un client --</option>
                    <c:forEach var="cl" items="${clients}">
                        <option value="${cl.idClient}"
                                ${enEdition && commandeAModifier.idClient == cl.idClient ? 'selected' : ''}>
                            <c:out value="${cl.nom}"/> <c:out value="${cl.prenom}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="champ">
                <label for="date_commande">Date commande</label>
                <input type="date" id="date_commande" name="date_commande"
                       value='<c:out value="${commandeAModifier.dateCommande}"/>'>
            </div>

            <button type="submit" class="secondaire">Valider la commande</button>
            <c:if test="${enEdition}">
                <a class="lien-bouton secondaire" href="${pageContext.request.contextPath}/commandes">Annuler</a>
            </c:if>
        </form>

        <%-- Formulaire pour ajouter une ligne de commande --%>
        <div class="lignes-commande">
            <h3>Ajouter des produits à la commande</h3>
            <form class="saisie" action="${pageContext.request.contextPath}/commandes" method="post">
                <input type="hidden" name="action" value="addLigne">

                <div class="champ">
                    <label for="id_produit">Produit</label>
                    <select id="id_produit" name="id_produit" required>
                        <option value="">-- Sélectionner un produit --</option>
                        <c:forEach var="prod" items="${produits}">
                            <option value="${prod.idProduit}" data-prix="${prod.prix}">
                                <c:out value="${prod.nom}"/> - <c:out value="${prod.prix}"/> DH (Stock: <c:out value="${prod.stock}"/>)
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="champ">
                    <label for="qte">Quantité</label>
                    <input type="number" id="qte" name="qte" min="1" required>
                </div>

                <button type="submit">Ajouter à la commande</button>
            </form>

            <%-- Affichage des lignes temporaires --%>
            <c:if test="${not empty lignesTemporaires}">
                <table>
                    <thead>
                        <tr>
                            <th>Produit</th>
                            <th>Quantité</th>
                            <th>Prix unitaire</th>
                            <th>Sous-total</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="ligne" items="${lignesTemporaires}">
                            <tr>
                                <td><c:out value="${ligne.nomProduit}"/></td>
                                <td><c:out value="${ligne.qte}"/></td>
                                <td><c:out value="${ligne.pu}"/></td>
                                <td><c:out value="${ligne.sousTotal}"/></td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/commandes" method="post">
                                        <input type="hidden" name="action" value="removeLigne">
                                        <input type="hidden" name="index" value="${ligne.index}">
                                        <button type="submit" class="danger">Supprimer</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <p><strong>Total: <c:out value="${totalTemporaire}"/> DH</strong></p>
            </c:if>
        </div>
    </section>

    <section aria-labelledby="titre-liste">
        <h2 id="titre-liste">Liste des commandes</h2>

        <c:choose>
            <c:when test="${empty commandes}">
                <p class="vide">Aucune commande pour le moment. Ajoutez la première avec le formulaire ci-dessus.</p>
            </c:when>
            <c:otherwise>
                <div class="tableau">
                    <table>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Client</th>
                            <th>Prix total</th>
                            <th>Date commande</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="cmd" items="${commandes}">
                            <tr>
                                <td><c:out value="${cmd.idComm}"/></td>
                                <td><c:out value="${cmd.idClient}"/></td>
                                <td><c:out value="${cmd.prixTotal}"/></td>
                                <td><c:out value="${cmd.dateCommande}"/></td>
                                <td class="actions">
                                    <a href="${pageContext.request.contextPath}/commandes?action=edit&id=${cmd.idComm}">Modifier</a>

                                    <form action="${pageContext.request.contextPath}/commandes" method="post"
                                          onsubmit="return confirm('Supprimer cette commande ?');">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${cmd.idComm}">
                                        <button type="submit" class="danger">Supprimer</button>
                                    </form>
                                </td>
                            </tr>
                            <c:if test="${enEdition && commandeAModifier.idComm == cmd.idComm && not empty lignesCommande}">
                                <tr>
                                    <td colspan="5">
                                        <div class="lignes-commande">
                                            <h3>Lignes de commande</h3>
                                            <table>
                                                <thead>
                                                    <tr>
                                                        <th>ID Ligne</th>
                                                        <th>Quantité</th>
                                                        <th>Prix unitaire</th>
                                                        <th>ID Produit</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="lc" items="${lignesCommande}">
                                                        <tr>
                                                            <td><c:out value="${lc.idLigneComm}"/></td>
                                                            <td><c:out value="${lc.qte}"/></td>
                                                            <td><c:out value="${lc.pu}"/></td>
                                                            <td><c:out value="${lc.idProduit}"/></td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
</main>
</body>
</html>

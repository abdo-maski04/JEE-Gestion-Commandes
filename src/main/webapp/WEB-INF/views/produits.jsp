<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Produits</title>
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
        main { max-width: 900px; margin: 0 auto; }
        h1 { margin: 0 0 1.5rem; font-size: 1.75rem; }
        h2 { margin: 0 0 .75rem; font-size: 1.1rem; }

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
        input[type="text"], input[type="number"] {
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
    </style>
</head>
<body>
<main>
    <h1>Produits</h1>

    <%-- Message d'erreur déposé par le Servlet --%>
    <c:if test="${not empty erreur}">
        <p class="erreur" role="alert"><c:out value="${erreur}"/></p>
    </c:if>

    <%-- Mode modification si le Servlet a déposé un produit à modifier --%>
    <c:set var="enEdition" value="${not empty produitAModifier}"/>

    <section aria-labelledby="titre-formulaire">
        <h2 id="titre-formulaire">${enEdition ? 'Modifier le produit' : 'Ajouter un produit'}</h2>

        <form class="saisie" action="${pageContext.request.contextPath}/produits" method="post">
            <input type="hidden" name="action" value="${enEdition ? 'update' : 'add'}">
            <c:if test="${enEdition}">
                <input type="hidden" name="id" value="${produitAModifier.idProduit}">
            </c:if>

            <div class="champ">
                <label for="nom">Nom</label>
                <input type="text" id="nom" name="nom" required
                       value='<c:out value="${produitAModifier.nom}"/>'>
            </div>
            <div class="champ">
                <label for="description">Description</label>
                <input type="text" id="description" name="description" required
                       value='<c:out value="${produitAModifier.description}"/>'>
            </div>
            <div class="champ">
                <label for="stock">Stock</label>
                <input type="number" id="stock" name="stock" min="0"
                       value='<c:out value="${produitAModifier.stock}"/>'>
            </div>
            <div class="champ">
                <label for="prix">Prix</label>
                <input type="number" id="prix" name="prix" step="0.01" min="0" required
                       value='<c:out value="${produitAModifier.prix}"/>'>
            </div>

            <button type="submit">${enEdition ? 'Enregistrer les modifications' : 'Ajouter le produit'}</button>
            <c:if test="${enEdition}">
                <a class="lien-bouton secondaire" href="${pageContext.request.contextPath}/produits">Annuler</a>
            </c:if>
        </form>
    </section>

    <section aria-labelledby="titre-liste">
        <h2 id="titre-liste">Liste des produits</h2>

        <c:choose>
            <c:when test="${empty produits}">
                <p class="vide">Aucun produit pour le moment. Ajoutez le premier avec le formulaire ci-dessus.</p>
            </c:when>
            <c:otherwise>
                <div class="tableau">
                    <table>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nom</th>
                            <th>Description</th>
                            <th>Stock</th>
                            <th>Prix</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="p" items="${produits}">
                            <tr>
                                <td><c:out value="${p.idProduit}"/></td>
                                <td><c:out value="${p.nom}"/></td>
                                <td><c:out value="${p.description}"/></td>
                                <td><c:out value="${p.stock}"/></td>
                                <td><c:out value="${p.prix}"/></td>
                                <td class="actions">
                                    <a href="${pageContext.request.contextPath}/produits?action=edit&id=${p.idProduit}">Modifier</a>

                                    <form action="${pageContext.request.contextPath}/produits" method="post"
                                          onsubmit="return confirm('Supprimer ce produit ?');">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${p.idProduit}">
                                        <button type="submit" class="danger">Supprimer</button>
                                    </form>
                                </td>
                            </tr>
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

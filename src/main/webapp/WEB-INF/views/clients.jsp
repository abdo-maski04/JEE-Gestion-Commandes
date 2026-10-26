<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Clients</title>
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
        input[type="text"] {
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
    <h1>Clients</h1>

    <%-- Message d'erreur déposé par le Servlet (champ manquant, client avec commandes...) --%>
    <c:if test="${not empty erreur}">
        <p class="erreur" role="alert"><c:out value="${erreur}"/></p>
    </c:if>

    <%-- Mode modification si le Servlet a déposé un client à modifier --%>
    <c:set var="enEdition" value="${not empty clientAModifier}"/>

    <section aria-labelledby="titre-formulaire">
        <h2 id="titre-formulaire">${enEdition ? 'Modifier le client' : 'Ajouter un client'}</h2>

        <form class="saisie" action="${pageContext.request.contextPath}/clients" method="post">
            <input type="hidden" name="action" value="${enEdition ? 'update' : 'add'}">
            <c:if test="${enEdition}">
                <input type="hidden" name="id" value="${clientAModifier.idClient}">
            </c:if>

            <div class="champ">
                <label for="nom">Nom</label>
                <input type="text" id="nom" name="nom" required
                       value='<c:out value="${clientAModifier.nom}"/>'>
            </div>
            <div class="champ">
                <label for="prenom">Prénom</label>
                <input type="text" id="prenom" name="prenom" required
                       value='<c:out value="${clientAModifier.prenom}"/>'>
            </div>
            <div class="champ">
                <label for="adresse">Adresse</label>
                <input type="text" id="adresse" name="adresse"
                       value='<c:out value="${clientAModifier.adresse}"/>'>
            </div>

            <button type="submit">${enEdition ? 'Enregistrer les modifications' : 'Ajouter le client'}</button>
            <c:if test="${enEdition}">
                <a class="lien-bouton secondaire" href="${pageContext.request.contextPath}/clients">Annuler</a>
            </c:if>
        </form>
    </section>

    <section aria-labelledby="titre-liste">
        <h2 id="titre-liste">Liste des clients</h2>

        <c:choose>
            <c:when test="${empty clients}">
                <p class="vide">Aucun client pour le moment. Ajoutez le premier avec le formulaire ci-dessus.</p>
            </c:when>
            <c:otherwise>
                <div class="tableau">
                    <table>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nom</th>
                            <th>Prénom</th>
                            <th>Adresse</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="c" items="${clients}">
                            <tr>
                                <td><c:out value="${c.idClient}"/></td>
                                <td><c:out value="${c.nom}"/></td>
                                <td><c:out value="${c.prenom}"/></td>
                                <td><c:out value="${c.adresse}"/></td>
                                <td class="actions">
                                    <a href="${pageContext.request.contextPath}/clients?action=edit&id=${c.idClient}">Modifier</a>

                                    <form action="${pageContext.request.contextPath}/clients" method="post"
                                          onsubmit="return confirm('Supprimer ce client ?');">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${c.idClient}">
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
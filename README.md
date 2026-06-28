# spring-framework

## Split 0
Objectif: rediriger tous les liens entres dans un servlet
- Creation d'une classe servlet avec les methodes doget et dopost
- Creation d'une methode processHandler : afficher un message de bienvenue dans le framework
- Verification que les liens vers des vues n'affiche pas le message mais le vue

## Split 1
Objectif: scanner tous les controllers
- Creation d'une annotation Controller de target TYPE
- Creation d'une classe utilitaire pour scanner un package specifique ou "ALL"
- Utilisation de init-param pour savoir quel(s) est(sont) les package à scanner
- Affichage de tous les controllers utilisé(s) dans le projet

## Split 2
Objectif: savoir quel URL est associé à une methode
- Creation d'une annotaion UrlMapping de target Method
- Mettre une valeur url
Lorsqu'un url est entree on verifie si elle est supportée ou non
- Si oui, on affiche la methode dont elle est liee
- Sinon, on lève une Exception et on affiche la liste de tous les liens supportés

## Split 3
Objectif: savoir quelle est la method GET/POST
- Creation d'une classe URLMethod : url + methode (GET/POST)
- Update de la classe ControllerMethodDto : class<?>, methode
- Update de l'annotaion UrlMapping : ajout d'un atttribut pour la methode

## Split 3 Bis
Objectif: excecuter la methode associer à l'url
- essayer avec un system.out.println puis voir dans le console du navigateur
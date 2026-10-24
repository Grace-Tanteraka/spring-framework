# spring-framework

## Sprint 0
Objectif: rediriger tous les liens entres dans un servlet
- Creation d'une classe servlet avec les methodes doget et dopost
- Creation d'une methode processHandler : afficher un message de bienvenue dans le framework
- Verification que les liens vers des vues n'affiche pas le message mais le vue

## Sprint 1
Objectif: scanner tous les controllers
- Creation d'une annotation Controller de target TYPE
- Creation d'une classe utilitaire pour scanner un package specifique ou "ALL"
- Utilisation de init-param pour savoir quel(s) est(sont) les package à scanner
- Affichage de tous les controllers utilisé(s) dans le projet

## Sprint 2
Objectif: savoir quel URL est associé à une methode
- Creation d'une annotaion UrlMapping de target Method
- Mettre une valeur url
Lorsqu'un url est entree on verifie si elle est supportée ou non
- Si oui, on affiche la methode dont elle est liee
- Sinon, on lève une Exception et on affiche la liste de tous les liens supportés

## Sprint 3
Objectif: savoir quelle est la method GET/POST
- Creation d'une classe URLMethod : url + methode (GET/POST)
- Update de la classe ControllerMethodDto : class<?>, methode
- Update de l'annotaion UrlMapping : ajout d'un atttribut pour la methode

## Sprint 3 Bis
Objectif: excecuter la methode associer à l'url
- essayer avec un system.out.println puis voir dans le console du navigateur

## Sprint 4
Objectif: utiliser nu contextListener au lieu de init()
- Creer une classe ContextListener extends ServletContextListener

## Sprint 5
Objectif: Model and view fonctionnel
- Creer la classe ModelAndView qui aura string viewName et Map<String, Object>
- Faire les fonctions setViewName() et setAttribute()
- Ajouter la condition d'execution que si c'est du ModelAndView on fait un requestDispatcher forward vers le view
- Coté developpeur, on ajoute des init-param dans web.xml pour definir les suffixes et suffixe des fichiers views

## Sprint 5 bis
Objectif : utiliser la base
- Utiliser le repository de spring MVC

## Sprint 6 
Objectif : construire api
- Construire une annotation sur la methode @webapi
- Au lieu d'aller vers une vue ca retourne du json
- Si type de retour est String on envoie juste ceci sinon on fait toJson()

## Sprint 7
Objectif : Binding
- Traiter les donnees envoyees depuis le view 
- Verifier si la methode a des parametres 
- la methode d'action appelee dans le controlleur peut avoir des arguments
- mettre des nulls par tout
- chercher parmis les requestparameters ce qui match et on remplace les valeurs null;

# Sprint 7 bis
Objectif : 
- On teste a donner un objet depuis le view (ex : Employe)

# Sprint 8
Objectif : Upload file
- Methode 1
    - Creer class MapFile (nom, extention, bites[])
    - Si la class a un parametre de type MapFile on recupere le fichier et on le met dans le parametre
- Methode 2
    - Creer une class dto (misolo map)
    - ajouter a l'attribut de la fonction d'action (dans le controller)

# Sprint 9
Objectif : utiliser la session
- Faire une classe personnelle ayant comme attribut un map
- Ajouter un map dans context, cle : sessionId, val : Session
- Si sessionid existe deja, on utilise son instance sinon on ajoute

# Sprint 10
Objectif : un utilisateur devrait etre connecte avant d'executer une methode de controlleur
- Faire une annotation Auth 
- Ajouter une param-init dans le web.xml et ajouter variable-authentification-check
- Si la session de l'utilisateur contient cette cle alors la methode s'excecute sinon throw AuthentificationException

# Sprint 10 bis
Objecitf : une methode ne peux etre excecute que par un role precis
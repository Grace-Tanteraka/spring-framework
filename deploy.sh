#!/bin/bash

# Définition des variables
APP_NAME="graceframework"
SRC_DIR="./"
BUILD_DIR="bin"
LIB_DIR="lib"
SERVLET_API_JAR="$LIB_DIR/servlet-api.jar"

# Nettoyage et création du répertoire temporaire
rm -rf $BUILD_DIR

# Compilation des fichiers Java avec le JAR des Servlets
find $SRC_DIR -name "*.java" > sources.txt
javac -cp $SERVLET_API_JAR -d $BUILD_DIR @sources.txt
rm sources.txt

# Copier les fichiers web (web.xml, JSP, etc.)
#cp -r $WEB_DIR/* $BUILD_DIR/

#copier web.xml vers web-inf
#cp web.xml $BUILD_DIR/WEB-INF/

# Générer le fichier .war dans le dossier build
#cd $BUILD_DIR || exit
jar -cvf $APP_NAME.jar -C ./bin .
#cd ..

# Déploiement dans Tomcat
#cp -f $BUILD_DIR/$APP_NAME.war $TOMCAT_WEBAPPS/

echo ""

echo "Déploiement terminé. Redémarrez Tomcat si nécessaire."

echo ""

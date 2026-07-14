#!/bin/bash

# Définition des variables
APP_NAME="graceframework"
SRC_DIR="./"
BUILD_DIR="bin"
LIB_DIR="lib"

# Nettoyage et création du répertoire temporaire
rm -rf $BUILD_DIR
mkdir -p $BUILD_DIR

# Compilation de tous les fichiers Java en incluant tout le dossier lib dans le classpath
find $SRC_DIR -name "*.java" > sources.txt

# /!\ Note le "lib/*" (sans oublier les guillemets pour éviter l'expansion du shell)
javac -cp "$LIB_DIR/*" -d $BUILD_DIR @sources.txt
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

echo "Fichier .jar généré avec succès : $BUILD_DIR/$APP_NAME.jar"

echo ""

# Utiliser une image de base Java
FROM openjdk:11-jre-slim

# Définir le répertoire de travail à /app
WORKDIR /app

# Copier le fichier JAR dans l'image
COPY ProjectFinal/target/ProjectFinal-1.0-SNAPSHOT.jar /app/ProjectFinal-1.0-SNAPSHOT.jar

# Exposer le port sur lequel l'application écoute
EXPOSE 8080

# Commande pour exécuter l'application
CMD ["java", "-jar", "ProjectFinal-1.0-SNAPSHOT.jar"]

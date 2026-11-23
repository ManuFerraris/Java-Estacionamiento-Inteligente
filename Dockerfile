# ETAPA 1: Compilación con Maven
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos el pom.xml y descargamos dependencias primero (optimiza caché)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el código fuente y generamos el .war
COPY src ./src
RUN mvn clean package -DskipTests

# ETAPA 2: Ejecución en Tomcat 10.1 (Obligatorio para Jakarta EE)
FROM tomcat:10.1-jdk21-temurin

# Limpiamos las apps por defecto de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiamos el .war generado y lo renombramos a ROOT.war
# (Renombrarlo a ROOT.war hace que tu app abra directo en el dominio "/" sin subcarpetas)
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
# Creamos una imagen donde se hará la compilacion del proyecto y generar el .jar
FROM maven:3.9-eclipse-temurin-21-jammy AS build
WORKDIR /build
COPY pom.xml /build
COPY src /build/src
RUN mvn clean install -DskipTests

# Se crea la imagen donde se instala el JRE, copia el .jar de la imagen del build y ejecuta el .jar
FROM eclipse-temurin:21-jre-jammy
COPY --from=build /build/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
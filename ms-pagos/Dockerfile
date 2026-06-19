FROM eclipse-temurin:25-jre
WORKDIR /app
ADD https://github.com/felipecortesf/sistema-reservas-tours/releases/download/v1.0.0/ms-pagos-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]

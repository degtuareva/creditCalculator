FROM eclipse-temurin:21-jre AS runtime

WORKDIR /app

COPY build/libs/creditCalculator-*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","/app/app.jar"]

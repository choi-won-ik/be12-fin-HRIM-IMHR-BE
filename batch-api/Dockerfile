FROM openjdk:17-ea-slim-buster
COPY ./build/libs/batch-api-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "/app.jar"]

FROM bitnami/kubectl:latest
CMD ["sleep", "3600"]
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY out/artifacts/FinanceTracker_jar/FinanceTracker.jar /app/FinanceTracker.jar
EXPOSE 8080
CMD ["java", "-jar", "FinanceTracker.jar"]
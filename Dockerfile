FROM eclipse-temurin:17-jre-jammy
EXPOSE 8080
ADD target/english-bot-images.jar english-bot-images.jar
ENTRYPOINT ["java", "-jar", "/english-bot-images.jar"]
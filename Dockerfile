FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-default}", "-Dspring.config.location=${SPRING_CONFIG_LOCATION:-classpath:/application.properties}", "-Duser.timezone=Asia/Seoul", "-jar", "app.jar"]
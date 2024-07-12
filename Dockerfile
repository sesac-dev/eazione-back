FROM openjdk:21

WORKDIR /app

COPY ./build/libs/*SHOT.jar /back_application.jar

CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "-Dspring.profiles.active=server", "/back_application.jar"]

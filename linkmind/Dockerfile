FROM amd64/amazoncorretto:17

WORKDIR /app

COPY ./build/libs/toaster-0.0.1-SNAPSHOT /app/toaster.jar

CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "-Dspring.profiles.active=dev", "toaster.jar"]

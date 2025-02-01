FROM eclipse-temurin:17-jdk-jammy AS build

ARG APP_NAME
ENV APP_NAME=${APP_NAME}

WORKDIR /app

COPY . .

RUN ./mvnw clean install package -pl $APP_NAME -DskipTests

FROM eclipse-temurin:17-jre-jammy

ARG APP_NAME
ENV APP_NAME=${APP_NAME}

COPY --from=build /app/$APP_NAME/target/*.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
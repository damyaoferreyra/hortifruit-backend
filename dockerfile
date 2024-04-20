FROM adoptopenjdk/openjdk11:alpine AS builder

COPY . /opt/hortifruit

WORKDIR /opt/hortifruit

RUN apk add --no-cache maven && \
    mvn clean package -Pprod -DskipTests

FROM adoptopenjdk/openjdk11:alpine

COPY --from=builder /opt/hortifruit/target/hortfruit-online-0.0.1-SNAPSHOT.jar /opt/hortifruit/hortfruit-online.jar

ENTRYPOINT [ "java", "-jar", "/opt/hortifruit/hortfruit-online.jar"]

EXPOSE 8080

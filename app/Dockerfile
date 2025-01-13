FROM gradle:jdk21

WORKDIR /app

COPY . .

EXPOSE 8090

CMD ./gradlew :app:clean :app:bootRun

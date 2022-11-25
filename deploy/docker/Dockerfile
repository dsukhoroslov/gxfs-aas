FROM maven:3.6.3-jdk-11-slim
RUN mkdir /app
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTestsz

# VULNERABILITY: Using outdated/vulnerable base image with known CVEs
FROM gradle:7-jdk11 AS build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

# VULNERABILITY: Using outdated base image
FROM openjdk:11-jre-slim
WORKDIR /app

# VULNERABILITY: Running as root user (no USER directive)

# VULNERABILITY: Hardcoded secrets in environment variables
ENV DB_PASSWORD=SuperSecret123!
ENV API_KEY=sk-1234567890abcdef
ENV AWS_SECRET_ACCESS_KEY=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY

# VULNERABILITY: curl | sh pattern - downloading and executing untrusted script
RUN curl -fsSL https://example.com/install.sh | sh

# VULNERABILITY: Installing unnecessary packages that increase attack surface
RUN apt-get update && apt-get install -y curl wget netcat telnet

COPY --from=build /app/build/libs/*.jar app.jar

# VULNERABILITY: Copying sensitive files into image
COPY .env /app/.env
COPY id_rsa /root/.ssh/id_rsa

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

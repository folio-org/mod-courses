# Temporary base image until FOLIO publishes folioci/alpine-jre-openjdk25.
# The setup below mirrors the essentials of the folioci image:
# https://github.com/folio-org/folio-tools/tree/master/folio-java-docker/openjdk21
# When the folioci image is available, replace everything up to the
# "Module-specific" section with: FROM folioci/alpine-jre-openjdk25:latest
FROM eclipse-temurin:25-jre-alpine

USER root

# Install latest patch versions of packages: https://pythonspeed.com/articles/security-updates-in-docker/
RUN apk upgrade --no-cache

# Create user/group 'folio' and the verticle directory
RUN mkdir -p /usr/verticles && \
    addgroup folio && \
    adduser -H -h /usr/verticles -G folio -D folio && \
    chown -R folio:folio /usr/verticles

USER folio
WORKDIR /usr/verticles

# ---- Module-specific ----

ENV VERTICLE_FILE=mod-courses-fat.jar

# Set the location of the verticles
ENV VERTICLE_HOME=/usr/verticles

# Copy your fat jar to the container
COPY target/${VERTICLE_FILE} ${VERTICLE_HOME}/${VERTICLE_FILE}

# Expose this port locally in the container.
EXPOSE 8081

# JAVA_OPTIONS is set by FOLIO deployments; exec makes java PID 1 so it receives stop signals.
# Arguments passed to "docker run" are forwarded to the module.
ENTRYPOINT ["sh", "-c", "exec java -XX:+ExitOnOutOfMemoryError $JAVA_OPTIONS -jar \"$VERTICLE_HOME/$VERTICLE_FILE\" \"$@\"", "--"]

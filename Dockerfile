# Temporary base image until FOLIO publishes folioci/alpine-jre-openjdk25.
# The setup below mirrors the folioci image so the container behaves the same:
# https://github.com/folio-org/folio-tools/tree/master/folio-java-docker/openjdk21
# When the folioci image is available, replace everything up to the
# "Module-specific" section with: FROM folioci/alpine-jre-openjdk25:latest
FROM eclipse-temurin:25-jre-alpine

USER root

# JAVA_APP_DIR is used by run-java.sh for finding the binaries
ENV JAVA_APP_DIR=/usr/verticles \
    JAVA_MAJOR_VERSION=25

RUN mkdir -p ${JAVA_APP_DIR}

# Copied from folio-tools/folio-java-docker (origin: fabric8io-images/run-java-sh)
COPY docker/run-java.sh ${JAVA_APP_DIR}/
RUN chmod 755 ${JAVA_APP_DIR}/run-java.sh

# Create user/group 'folio'
RUN addgroup folio && \
    adduser -H -h ${JAVA_APP_DIR} -G folio -D folio && \
    chown -R folio:folio ${JAVA_APP_DIR}

# Install latest patch versions of packages: https://pythonspeed.com/articles/security-updates-in-docker/
RUN apk upgrade --no-cache

# Run as this user
USER folio
WORKDIR ${JAVA_APP_DIR}

ENTRYPOINT [ "./run-java.sh" ]

# ---- Module-specific ----

ENV VERTICLE_FILE=mod-courses-fat.jar

# Set the location of the verticles
ENV VERTICLE_HOME=/usr/verticles

# Copy your fat jar to the container
COPY target/${VERTICLE_FILE} ${VERTICLE_HOME}/${VERTICLE_FILE}

# Expose this port locally in the container.
EXPOSE 8081

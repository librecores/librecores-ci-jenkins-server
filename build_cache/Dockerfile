#
# Contains build Cache definition for the LibreCores CI Server project.
# TODO: add a DockerHub link. librecores/librecores-ci-mvn-cache
#
FROM maven:3.6.0 as mvncache
ARG LCCI_BUILD_CACHE_TAG=a3b6a335233f7cd249c7dc36779936176244d7fc
ENV MAVEN_OPTS=-Dmaven.repo.local=/mavenrepo
WORKDIR /src
RUN git clone https://github.com/librecores/librecores-ci-jenkins-server.git . && git checkout ${LCCI_BUILD_CACHE_TAG}
RUN mvn compile dependency:resolve dependency:resolve-plugins

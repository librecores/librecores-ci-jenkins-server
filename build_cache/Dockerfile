#
# Contains build Cache definition for the LibreCores CI Server project.
# TODO: add a DockerHub link. librecores/librecores-ci-mvn-cache
#
FROM maven:3.6.0 as mvncache
ARG LCCI_BUILD_CACHE_TAG=4b415786a0dd62559c87ade781242a9f91758185
ENV MAVEN_OPTS=-Dmaven.repo.local=/mavenrepo
WORKDIR /src
RUN git clone https://github.com/librecores/librecores-ci-jenkins-server.git . && git checkout ${LCCI_BUILD_CACHE_TAG}
RUN mvn compile dependency:resolve dependency:resolve-plugins

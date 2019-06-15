#
# Contains build Cache definition for the LibreCores CI Server project.
# TODO: add a DockerHub link. librecores/librecores-ci-mvn-cache
#
FROM maven:3.6.0 as mvncache
ARG LCCI_BUILD_CACHE_TAG=dec5de16eaf79877bd75bb79a8a9d3c86411aab5
ENV MAVEN_OPTS=-Dmaven.repo.local=/mavenrepo
WORKDIR /src
RUN git clone https://github.com/librecores/librecores-ci-jenkins-server.git . && git checkout ${LCCI_BUILD_CACHE_TAG}
RUN mvn dependency:resolve dependency:resolve-plugins -DexcludeScope=test

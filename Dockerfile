#
# Builds the LibreCores CI Server Docker image.
#
FROM librecores/librecores-ci-mvn-cache as mvncache

FROM jenkins/custom-war-packager:pr-104 as builder
COPY --from=mvncache /mavenrepo /mavenrepo
ADD . /lcci-src
WORKDIR /lcci-src
ENV MAVEN_OPTS=-Dmaven.repo.local=/mavenrepo
RUN java -jar /app/custom-war-packager-cli.jar -configPath packager-config.yml

FROM jenkins/jenkins:2.176.1
MAINTAINER Oleg Nenashev <o.v.nenashev@gmail.com>
LABEL Description="Spins up the local development environment" Vendor="FOSSi" Version="0.1"
COPY --from=builder /lcci-src/tmp/output/target/librecores-ci-1.0-SNAPSHOT.war /usr/share/jenkins/jenkins.war

COPY init_scripts/src/main/groovy/ /usr/share/jenkins/ref/init.groovy.d/
COPY userContent ${JENKINS_HOME}/userContent/

# TODO: It should be configurable in "docker run"
ARG DEV_HOST=127.0.0.1
ARG CREATE_ADMIN=true
# If false, only few runs can be actually executed on the master
# See JobRestrictions settings
ARG ALLOW_RUNS_ON_MASTER=false
ARG LOCAL_PIPELINE_LIBRARY_PATH=/var/jenkins_home/pipeline-library

ENV CONF_CREATE_ADMIN=$CREATE_ADMIN
ENV CONF_ALLOW_RUNS_ON_MASTER=$ALLOW_RUNS_ON_MASTER

# Directory for Pipeline Library development sample
ENV LOCAL_PIPELINE_LIBRARY_PATH=${LOCAL_PIPELINE_LIBRARY_PATH}
RUN mkdir -p ${LOCAL_PIPELINE_LIBRARY_PATH}

VOLUME /var/jenkins_home/pipeline-dev
VOLUME /var/jenkins_home/imported_secrets
ENV JAVA_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=2 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp -XX:+ExitOnOutOfMemoryError -XX:+PrintFlagsFinal"
EXPOSE 5005

COPY jenkins2.sh /usr/local/bin/jenkins2.sh
ENTRYPOINT ["/sbin/tini", "--", "/usr/local/bin/jenkins2.sh"]

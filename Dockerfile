#
# This is the obsolete LibreCores developer build pack
#
FROM maven:3.6.0 as mvncache
ADD pom.xml /src/pom.xml
ADD init_scripts/pom.xml /src/init_scripts/pom.xml
WORKDIR /src
ENV MAVEN_OPTS=-Dmaven.repo.local=/mavenrepo
RUN mvn compile dependency:resolve dependency:resolve-plugins

FROM jenkins/custom-war-packager as builder
ENV MAVEN_OPTS=-Dmaven.repo.local=/mavenrepo
ADD . /lcci-src
COPY --from=mvncache /mavenrepo /mavenrepo
RUN cd /lcci-src && make clean package

FROM jenkins/jenkins:2.164.3
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

FROM jenkins/jenkins:2.60.3
MAINTAINER Oleg Nenashev <o.v.nenashev@gmail.com>
LABEL Description="Spins up the local development environment" Vendor="Oleg Nenashev" Version="0.1"

#TODO: Get rid of the experimental UC once the FileSystem SCM plugin is released
# Use experimental UC for FileSystem SCM
# See https://github.com/jenkinsci/docker/issues/538
COPY install-plugins.sh /usr/local/bin/install-plugins.sh
ENV JENKINS_UC_EXPERIMENTAL=https://updates.jenkins.io/experimental
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

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
EXPOSE 5005

COPY jenkins2.sh /usr/local/bin/jenkins2.sh
ENTRYPOINT ["/bin/tini", "--", "/usr/local/bin/jenkins2.sh"]

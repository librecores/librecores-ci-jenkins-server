# LibreCores CI

[LibreCores CI](https://ci.librecores.org/) is a service, which hosts automation flows for the following purposes:

* Continuous Integration of projects being hosted on [LibreCores](http://librecores.org/)
* Continuous Integration and packaging of EDA tools we use in the project ([FuseSoC](https://github.com/olofk/fusesoc), etc.)
* Automation of the project infrastructure

This repository contains backend code and documentation for the [LibreCores CI instance](https://ci.librecores.org/).

## Current state

The instance is **under construction** now.
In the case of any questions, please use bugtracker in this GitHub project.

## GitHub Integration

We have a [librecores-ci-bot](https://github.com/librecores-ci-bot), which allows integrating with GitHub projects.

Commonly it requires the following permissions:
* Read-only - for accessing repos
* Write permission - for commenting and approving pull requests/commits
* Webhook - for automated hook management within repo/organization (not really required since project admins can setup hooks on their own)

## Docker Dev Environment

### Usage

Run image:

```shell
docker run --rm --name ci-jenkins-io-dev -v maven-repo:/root/.m2 -e DEV_HOST=${CURRENT_HOST} -p 8080:8080 -p 50000:50000 librecores/librecores-ci-dev
```

Jenkins will need to connect to the Docker host to run agents.
If you use Docker for Mac, use `-Dio.jenkins.dev.host` and additional `socat` image for forwarding.

```shell
docker run -d -v /var/run/docker.sock:/var/run/docker.sock -p 2376:2375 bobrik/socat TCP4-LISTEN:2375,fork,reuseaddr UNIX-CONNECT:/var/run/docker.sock
```

#### Developing Pipeline libraries

In the _Development_ folder there is a _PipelineLib_ folder, which allows local building and testing of the library.
This folder can be mapped to a local repository in order to develop the library without committing changes: 

```shell
docker run --rm --name librecores-ci-dev -v maven-repo:/root/.m2 -v ${MY_PIPELINE_DEV_DIR}:/var/jenkins_home/pipeline-dev:ro -e DEV_HOST=${CURRENT_HOST} -p 8080:8080 -p 50000:50000 librecores/librecores-ci-dev
```

Once started, you can just start editing the Pipeline library locally.
On every job start the changes will be reflected in the directory without committing anything.

##### Debugging Master

In order to debug the master, use the `-e DEBUG=true -p 5005:5005` when starting the container.
Jenkins will be suspended on the startup in such case.

### Building images

#### Master

Build image:

```shell
docker build -t librecores/librecores-ci-dev .
```

#### Build Agents 

See other Docker repositories in the LibreCores organization.

## Contacts

* [Oleg Nenashev](https://github.com/oleg-nenashev) - Instance maintainer: CI instance itself
* [Stefan Wallentowitz](https://github.com/wallento) - Instance maintainer: AWS, Certificates, etc. 
* [Olof Kindgren](https://github.com/olofk) - Backup contact

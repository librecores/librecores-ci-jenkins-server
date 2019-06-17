# Contributing

Everyone is welcome to contribute to this repository.

## Prerequisites

* Make
* Docker should be installed on the build machine
* Linux or Windows Subsystem for Linux are recommended for building the images
  * If you use Windows, see [this page](https://nickjanetakis.com/blog/setting-up-docker-for-windows-and-wsl-to-work-flawlessly) for Docker configuration on WSL

## Building and running the developer image

See the makefile in the repository root.
The image can be built with the `make build` command and then launched with `make run`.
Repository buld logic

If you are developing Pipeline libraries or Jenkinsfiles for LibreCores CI,
use the `make dev` command.

## Proposing changes

1. Create pull requests against the master branch
2. System configurations for Jenkins will be promoted to production manually after the merge

## Troubleshooting 

In the case of any questions, please use the [librecores/librecores-ci](https://gitter.im/librecores/librecores-ci) gitter chat
or create an issue in this project.

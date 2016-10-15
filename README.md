# LibreCores CI

[LibreCores CI](https://ci.librecores.org/) is a service, which hosts automation flows for the following purposes:

* Continuous Integration of projects being hosted on [LibreCores](http://librecores.org/)
* Continuous Integration and packaging of EDA tools we use in the project ([FuseSoC](https://github.com/olofk/fusesoc), etc.)
* Automation of the project infrastructure

This repository contains backend code and documentation for the [LibreCores CI instance](https://ci.librecores.org/).

## Current status

The instance is **under construction** now.
In the case of any questions, please use bugtracker in this GitHub project.

Status overview from [ORCONF2016](http://orconf.org/):
* [Slides](https://speakerdeck.com/onenashev/orconf2016-librecores-ci-project-overview)
* Video: Coming soon

## GitHub Integration

We have a [librecores-ci-bot](https://github.com/librecores-ci-bot), which allows integrating with GitHub projects.

Commonly it requires the following permissions:
* Read-only - for accessing repos
* Write permission - for commenting and approving pull requests/commits
* Webhook - for automated hook management within repo/organization (not really required since project admins can setup hooks on their own)

## Contacts

* [Oleg Nenashev](https://github.com/oleg-nenashev) - Instance maintainer: CI instance itself
* [Stefan Wallentowitz](https://github.com/wallento) - Instance maintainer: AWS, Certificates, etc. 
* [Olof Kindgren](https://github.com/olofk) - Backup contact

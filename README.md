FringeUtils-Common
==============

[![Java CI with Gradle](https://github.com/pegasystems/fringeutils-common/actions/workflows/gradle.yml/badge.svg)](https://github.com/pegasystems/fringeutils-common/actions/workflows/gradle.yml)
[![GitHub tag](https://img.shields.io/github/tag/pegasystems/fringeutils-common.svg)](https://github.com/pegasystems/fringeutils-common/tags)

FringeUtils-Common is a common library used in Pega-LogViewer and Pega-TracerViewer tools.

This library contains functionality for file reading and common swing components like TreeTable etc.

Build
-----

Uses Nebula release plugin for version control

To build the java library jar and sources use the following command:

```
$ ./gradlew clean build
```

The artefacts `fringeutils-common-<version>.jar` and `fringeutils-common-<version>-sources.jar` are created under `'\target\'` folder.

To install into local maven repository

```
$ ./gradlew publishToMavenLocal
```



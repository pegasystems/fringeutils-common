[![Travis](https://img.shields.io/travis/pegasystems/fringeutils-common.svg)](https://travis-ci.org/pegasystems/fringeutils-common)

FringeUtils-Common
==============
FringeUtils-Common is a common library used in LogViewer and TracerViewer tools.

This library contains functionality for file reading and common swing components like TreeTable etc.

Build
-----
needs JDK8.  

To build the library jar and sources use the following command:

```
$ ./gradlew clean build
```

The artefacts `fringeutils-common-<version>.jar` and `fringeutils-common-<version>-sources.jar` are created under `'\target\'` folder.

To install into local maven repository

```
$ ./gradlew publishToMavenLocal
```

To release the library to JFrog Bintray use the following command:

```
git tag v<major>.<minor>.<patch>
git push --tags
./gradlew final
```

The released artefacts are uploaded to JFrog Bintray under following location

'https://dl.bintray.com/pegasystems/fringeutils/com/pega/gcs/fringeutils-common/<*version*>' folder.


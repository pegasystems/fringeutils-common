FringeUtils-Common
==============
FringeUtils-Common is a common library used in LogViewer and TracerViewer tools.
This library contains functionality for file reading and common swing components like TreeTable etc.


Build
-----
needs JDK8.  

To build the library jar and sources use the following command:
```
$ mvn clean package
```

The release artefacts are `fringeutils-common-<version>.jar` and `fringeutils-common-<version>-sources.jar` under `'\target\'` folder.
The release artefacts can then be copied to `'\dependencies\'` folder in both LogViewer and TracerViewer project.

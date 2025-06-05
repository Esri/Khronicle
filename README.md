# Khronicle

An [SLF4J](https://www.slf4j.org) backend for Android. Similar to [logback-android](https://github.com/tony19/logback-android), but written in Kotlin.

## Instructions

1. Add SLF4J as a dependency, e.g. `implementation("org.slf4j:slf4j-api:2.0.16")`
1. Add Khronicle as a dependency
1. Create a `logger-config.xml` file in `resources/assets/`
1. Use SLF4J to log messages, like you would with any other SLF4J backend

### Proguard options

Logging appenders are created via reflection so class names must be left  intact for runtime.  In 
order to keep them you will need to add the following to your application proguard rules:

```
-keep class com.esri.logger.appender.** { *; }
```

## Requirements

* Android SDK 24 or newer.

## Resources

* [GitHub Markdown Reference](https://docs.github.com/en/get-started/writing-on-github/getting-started-with-writing-and-formatting-on-github/basic-writing-and-formatting-syntax)
* [Esri Copyright Statement](https://github-admin.esri.com/doc/copyright.txt)
* [Public Repository Guidelines](https://github-admin.esri.com/doc/public-repository-requirements-and-guidelines.html)

## Issues

Find a bug or want to request a new feature?  Please let us know by submitting an issue.

## Contributing

Esri welcomes contributions from anyone and everyone. Please see our [guidelines for contributing](https://github.com/esri/contributing).

## Licensing

Copyright 2025 Esri

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

A copy of the license is available in the repository's [LICENSE.txt](LICENSE.txt?raw=true) file.


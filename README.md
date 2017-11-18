# kotlin
[![Release](https://jitpack.io/v/io.vito.ludwieg/kotlin.svg)](https://jitpack.io/#io.vito.ludwieg/kotlin)
> `ludwieg/kotlin` implements basic serialisation and deserialisation mechanisms for the JVM.

This repository yields the `io.vito.ludwieg` package, available through JitPack.

## Installing

Since this library is distributed through JitPack, the following package managers
are supported:

 - Gradle
 - Maven
 - Sbt
 - Leiningen

### Gradle

First, add JitPack to your list of repositories:
```gradle
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```

Then, add the dependency:
```gradle
    dependencies {
            compile 'io.vito.ludwieg:kotlin:0.1'
    }
```

### Maven
First, add JitPack to your list of repositories:
```xml
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
```

Then, add the dependency:
```xml
    <dependency>
        <groupId>io.jitpack</groupId>
        <artifactId>gradle-simple</artifactId>
        <version>1.1</version>
    </dependency>
```


### Sbt
First, add JitPack to your list of resolvers:
```scala
    resolvers += "jitpack" at "https://jitpack.io"
```

Then, add the dependency:
```scala
    libraryDependencies += "io.jitpack" % "gradle-simple" % "1.1"
```

### Leiningen
First, add JitPack to the end of the repository list in your `project.clj`:
```clojure
    :repositories [["jitpack" "https://jitpack.io"]]
```

Then, add the dependency:
```clojure
    :dependencies [[io.jitpack/gradle-simple "1.1"]]
```

## Usage

Ludwieg is WIP. More documentation will be available in the future.

## License

```
MIT License

Copyright (c) 2017 Victor Gama de Oliveira

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

# gradle-jarinjar-plugin
A repackaging of Eclipse's jar in jar loader easily usable as gradle plugin. It configures the default ```jar``` task so that the generated file
contains all the needed dependencies without any need to distribute them separately.

It uses eclipse's jar in jar implementation for running the jar.

# Usage

## Apply the plugin
To use the plugin with Gradle 2.1 or later, add the following to your build.gradle file:
```groovy
plugins {
  id 'net.robyf.jarinjar' version '1.0.8'
}
```

To use the plugin with Gradle 2.0 or older, or to use a snapshot release of the
plugin, add the following to build.gradle:

```groovy
buildscript {
    repositories {
        mavenCentral()
        // The next repo is only needed while using SNAPSHOT versions
        maven { url "https://oss.sonatype.org/content/repositories/snapshots" }
    }
    dependencies {
        classpath "net.robyf:gradle-jarinjar-plugin:1.0.8"
    }
}
apply plugin: 'net.robyf.jarinjar'
```

## Configuration
The behavior of this plugin is controlled by setting various options in the ```executableJar```
block of your build.gradle file.
- ```mainClass = <class name>```: The name of the class to be executed when running the jar.
- ```configuration = <configuration>```: Class path used when running the jar. The default is ```runtime```.

## Example
```groovy
plugins {
    id "net.robyf.jarinjar" version "1.0.8"
}

apply plugin: 'java'

wrapper {
    gradleVersion = '3.0'
}

repositories {
    jcenter()
}

dependencies {
    compile group: 'org.json', name: 'json', version: '20160810'
}

executableJar {
    mainClass 'com.example.MainClass'
}
```
and just run ```./gradlew jar```.

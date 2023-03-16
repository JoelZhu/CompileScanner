# CompileScanner
Classes scanner for Android applications, which supply interfaces to get annotated classes for developers to design decoupling code.

![version](https://img.shields.io/maven-central/v/io.github.joelzhu/scanner-runtime?color=blue&label=version&style=for-the-badge)
![license](https://img.shields.io/github/license/JoelZhu/CompileScanner?style=for-the-badge)
![build status](https://img.shields.io/github/actions/workflow/status/JoelZhu/CompileScanner/android.yml?branch=main&style=for-the-badge)
![top language](https://img.shields.io/github/languages/top/JoelZhu/CompileScanner?color=orange&style=for-the-badge)
![last commit](https://img.shields.io/github/last-commit/JoelZhu/CompileScanner?color=pink&label=commited&style=for-the-badge)

## ✨ Overall
<h4>
  <a href="https://github.com/JoelZhu/CompileScanner/README.md#Deploy">Deploy</a>
</h4>

## 🛠️ Deploy
Deploy the library use the dependencies below:
```
dependencies {
    implementation "io.github.joelzhu:scanner-runtime:[RELEASED_VERSION]"
    annotationProcessor "io.github.joelzhu:scanner-compile:[RELEASED_VERSION]"
}
```
if you are using kotlin, please replace ```annotationProcessor``` with ```kapt```, and add plugin together, as below:
```
plugins {
    ...
    id 'kotlin-kapt'
}

dependencies {
    ...
    kapt "io.github.joelzhu:scanner-compile:[RELEASED_VERSION]"
}
```

**Important!!  
Only implement is not enough, please read the notice below, there are some different compile options to add in different situations.**

Quick guide: <a href="https://github.com/JoelZhu/CompileScanner/README.md#Only an application module">Only an application module</a> | <a href="https://github.com/JoelZhu/CompileScanner/README.md#With library module(s)">With library module(s)</a>

#### Only an application module
If you are only an application module exists, without any library modules to implement, write down the code into application module's ```build.gradle```
```
android {
    ...
    defaultConfig {
        ...
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = [
                        appModule: 'true'
                ]
            }
        }
    }
}
```

#### With library module(s)
##### Application module without ```@CompileScan```
If you are using Android library module(s) in your project, you should specify it in compile options, which also defined into application module's ```build.gradle```
```
android {
    ...
    defaultConfig {
        ...
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = [
                        appModule     : 'true',
                        isMultiModule : 'true'
                ]
            }
        }
    }
}
```
##### Application module with ```@CompileScan```
And if the application module still have annotation: ```@CompileScan```, you should add option ```withAnnotation: 'true'``` into ```build.gradle``` additional, for example:
```
android {
    ...
    defaultConfig {
        ...
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = [
                        ...
                        withAnnotation: 'true'
                ]
            }
        }
    }
}
```

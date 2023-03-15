# CompileScanner
Classes scanner for Android applications, which supply interfaces to get annotated classes for developers to design decoupling code.

[![Maven Central](https://img.shields.io/maven-central/v/io.github.joelzhu/scanner-runtime?color=blue&label=version)](https://github.com/JoelZhu/CompileScanner/releases/latest)
[![Last commit](https://img.shields.io/github/last-commit/JoelZhu/CompileScanner?color=pink)]()
[![License](https://img.shields.io/github/license/JoelZhu/CompileScanner)]()
[![Workflow status](https://img.shields.io/github/actions/workflow/status/JoelZhu/CompileScanner/android.yml?branch=main)](https://github.com/JoelZhu/CompileScanner)
[![Top language](https://img.shields.io/github/languages/top/JoelZhu/CompileScanner?color=orange)](https://github.com/JoelZhu/CompileScanner)

## ✨ Overall
<h4>
  <a href="https://github.com/JoelZhu/CompileScanner/README.md#Deploy">Deploy</a>
</h4>

## 🛠️ Deploy
> If you are only an application module exists, use the script below to download **CompileScanner**
```
android {
    ...
    defaultConfig {
        ...
        // CompileScan requires compile options here.
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = [
                        appModule: 'true'
                ]
            }
        }
    }
}

dependencies {
    implementation "io.github.joelzhu:scanner-runtime:[RELEASED_VERSION]"
    annotationProcessor "io.github.joelzhu:scanner-compile:[RELEASED_VERSION]"
}
```
For more compile options, see 
> if you are using kotlin, please replace ```annotationProcessor``` with ```kapt```, and add plugin together, as below:
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

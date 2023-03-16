# CompileScanner
Classes scanner for Android applications, which supply interfaces to get annotated classes for developers to design decoupling code.

![version](https://img.shields.io/maven-central/v/io.github.joelzhu/scanner-runtime?color=blue&label=version&style=for-the-badge)
![license](https://img.shields.io/github/license/JoelZhu/CompileScanner?style=for-the-badge)
![build status](https://img.shields.io/github/actions/workflow/status/JoelZhu/CompileScanner/android.yml?branch=main&style=for-the-badge)
![top language](https://img.shields.io/github/languages/top/JoelZhu/CompileScanner?color=orange&style=for-the-badge)
![last commit](https://img.shields.io/github/last-commit/JoelZhu/CompileScanner?color=pink&label=commited&style=for-the-badge)

### ✨ Overall
<a href="#%EF%B8%8F-deploy">Deploy</a> | <a href="#%EF%B8%8F-usage">Usage</a> | Troubles(To do)

### 🛠️ Deploy
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
Only implement is not enough, please read the content below, there are some different compile options to add in different situations.**

Quick guide: <a href="#only-an-application-module">Only an application module</a> | <a href="#implemented-library-modules-with-compilescan">Implemented library module(s) with ```@CompileScan```</a> | <a href="#description-of-compile-options">Description of compile options</a>

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

#### Implemented library module(s) with ```@CompileScan```
##### Application module without ```@CompileScan``` in it
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
##### Application module with ```@CompileScan``` in it
And if the application module still have annotation: ```@CompileScan```, you should add option ```withAnnotation: 'true'``` into ```build.gradle``` additional, for example:
```
android {
    ...
    defaultConfig {
        ...
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = [
                        appModule     : 'true',
                        isMultiModule : 'true',
                        withAnnotation: 'true'
                ]
            }
        }
    }
}
```
#### Description of compile options
|Compile Options|Description|
|----|----|
|appModule|Indicate whether current module is application module|
|isMultiModule|Indicate whether implemented library module has ```@CompileScan```|
|withAnnotation|Indicate whether application module has ```@CompileScan```|
### 🗒️ Usage
For simple usage, coding as below:
```
@CompileScan
public class ExampleClass implements IExample {
    ...
}
```
Then, you can get all of those annotated classes by: ```Scanner.getAnnotatedInstances(IExample.class)```, and you will get an array of instances typed by```IExample.class```

Some advanced usage below:
* You can add a string to mark them with different tags, such as: ```@CompileScan(tag = "Class1")```. Then, through method: ```Scanner.getAnnotatedInstances("Class1", XXX.class)``` to acquire those annotated classes' instances
* You can specify those classes' priority, like: ```@CompileScan(priority = 2)```. Then, this class will be arranged at the second place if ```priority = 1``` exists, if not, this class will still placed at the first place
> For more examples, see the code in the project. [Java Example](https://github.com/JoelZhu/CompileScanner/tree/main/app_sample_java) | [Kotlin Example](https://github.com/JoelZhu/CompileScanner/tree/main/app_sample_kotlin) | [Multi Modules Usage Example](https://github.com/JoelZhu/CompileScanner/tree/main/app_sample_multimodule_app)

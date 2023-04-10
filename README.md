# CompileScanner
Classes scanner for Android applications, which supply interfaces to get annotated classes for developers to design decoupling code.

![version](https://img.shields.io/maven-central/v/io.github.joelzhu/scanner-runtime?color=blue&label=version&style=for-the-badge)
![license](https://img.shields.io/github/license/JoelZhu/CompileScanner?style=for-the-badge)
![build status](https://img.shields.io/github/actions/workflow/status/JoelZhu/CompileScanner/android.yml?branch=main&style=for-the-badge)
![top language](https://img.shields.io/github/languages/top/JoelZhu/CompileScanner?color=orange&style=for-the-badge)
![last commit](https://img.shields.io/github/last-commit/JoelZhu/CompileScanner?color=pink&label=commited&style=for-the-badge)

### ✨ Overall
<a href="#%EF%B8%8F-deploy">Deploy</a> | <a href="#%EF%B8%8F-usage">Usage</a> | <a href="#-troubles">Troubles</a>

### 🛠️ Deploy
To deploy the library as two steps:
1. declare in project's build.gradle as below:
```groovy
dependencies {
    classpath "io.github.joelzhu:scanner-plugin:[RELEASED_VERSION]"
}
```
2. declare in application's build.gradle as below:
```groovy
plugins {
    id 'com.joelzhu.scanner'
}

dependencies {
    implementation "io.github.joelzhu:scanner-runtime:[RELEASED_VERSION]"
}
```
> There's no obvious differences between using Java and Kotlin.

### 🗒️ Usage
Quick guide: <a href="#simple-usage">Simple usage</a> | <a href="#advanced-usage-default">Advanced usage: default</a> | <a href="#advanced-usage-priority">Advanced usage: priority</a> | <a href="#description-of-fields">Description of fields</a>

##### Simple usage
For simple usage, coding as below:
```java
@CompileScan
public class ExampleClass implements IExample {}
```
And you will got the classes or array of instances when you call ```Scanner.class```'s public method.

##### Advanced usage: default
You can use the ```CompileScanner``` as a compilation Proxy. For example, you have a class A as below:
```java
@CompileScan(tag = "ProxySample", isDefault = true)
public class ProxyA implements IProxy {
    @Override
    public String printName() {
        return "I'm A.";
    }
}
```
and you have another class B as below:
```java
@CompileScan(tag = "ProxySample")
public class ProxyB implements IProxy {
    @Override
    public String printName() {
        return "I'm B.";
    }
}
```
If you call the method like: ```Scanner.getAnnotatedInstances(new Options.Builder("ProxySample").create(), IProxy.class);```, you will got the array of ```Class``` which implements ```IProxy.class``` in it.
And you call ```IProxy```'s ```printName()```, you will got the string: ```I'm B.```.
This may help you implement pattern - Proxy easily.

##### Advanced usage: priority
You can set priority of the classes with the same ```tag```. For example:
If class ```First``` has the higher priority of the class ```Second```, you can annotated them as below:
```java
@CompileScan(priority = 1)
public class First {}
```
```java
@CompileScan(priority = 2)
public class Second {}
```
And you will got the array, which put the ```First``` above the ```Second```.

##### Description of fields
| Fields     | Description                                             |
|------------|---------------------------------------------------------|
| tag        | Mark the annotated classes as different groups          |
| isDefault  | Differ the classes with default one or not              |
| priority   | Indicate the priority of each classes with the same tag |

> For more examples, see the code in the project. [Java Example](https://github.com/JoelZhu/CompileScanner/tree/main/app_sample_java) | [Kotlin Example](https://github.com/JoelZhu/CompileScanner/tree/main/app_sample_kotlin) | [Multi Modules Usage Example](https://github.com/JoelZhu/CompileScanner/tree/main/app_sample_multimodule_app)

### ❗ Troubles
If you got compile exceptions or something unexcepted, add the options below in application's build.gradle to open the compile log.
```groovy
android {
}

Scanner {
    enableLog = true
}

dependencies {
}
```
Then, when you compile failed, paste the full compile log into an new issue, and assign to me, i will try my best to resolve the problem as fast as you wish.

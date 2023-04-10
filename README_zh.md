# CompileScanner
提供给Android应用的类扫描器，能够帮助开发者设计实现出解耦的代码。

![version](https://img.shields.io/maven-central/v/io.github.joelzhu/scanner-runtime?color=blue&label=version&style=for-the-badge)
![license](https://img.shields.io/github/license/JoelZhu/CompileScanner?style=for-the-badge)
![build status](https://img.shields.io/github/actions/workflow/status/JoelZhu/CompileScanner/android.yml?branch=main&style=for-the-badge)
![top language](https://img.shields.io/github/languages/top/JoelZhu/CompileScanner?color=orange&style=for-the-badge)
![last commit](https://img.shields.io/github/last-commit/JoelZhu/CompileScanner?color=pink&label=commited&style=for-the-badge)

### ✨ 总览
<a href="#%EF%B8%8F-部署">部署</a> | <a href="#%EF%B8%8F-用法">用法</a> | <a href="#-问题">问题</a> | <a href="https://github.com/JoelZhu/CompileScanner/blob/main/README.md">English</a>

### 🛠️ 部署
分两步部署库：
1. 在工程的 build.gradle 中如下声明：
```groovy
dependencies {
    classpath "io.github.joelzhu:scanner-plugin:[RELEASED_VERSION]"
}
```
2. 在app module的 build.gradle 中如下声明：
```groovy
plugins {
    id 'com.joelzhu.scanner'
}

dependencies {
    implementation "io.github.joelzhu:scanner-runtime:[RELEASED_VERSION]"
}
```
> 无论是使用Java还是Kotlin编程，部署上面没有明显的差异。

### 🗒️ 用法
快速指南: <a href="#基础用法">基础用法</a> | <a href="#高阶用法default">高阶用法：default</a> | <a href="#高阶用法priority">高阶用法：priority</a> | <a href="#成员的说明">成员的说明</a>

##### 基础用法
下面是基础的使用方式：
```java
@CompileScan
public class ExampleClass implements IExample {}
```
然后，你可以通过调用```Scanner.class```的公共方法，来获取类或者类实例。

##### 高阶用法：default
你可以使用 ```CompileScanner``` 作为一个编译时的代理。举个例子，你有如下的一个A类:
```java
@CompileScan(tag = "ProxySample", isDefault = true)
public class ProxyA implements IProxy {
    @Override
    public String printName() {
        return "I'm A.";
    }
}
```
然后有另外一个B类：
```java
@CompileScan(tag = "ProxySample")
public class ProxyB implements IProxy {
    @Override
    public String printName() {
        return "I'm B.";
    }
}
```
当你如下方式请求结果时:
```java
final String tag = "ProxySample";
final IProxy[] proxys = Scanner.getAnnotatedInstances(new Options.Builder(tag).create(), IProxy.class);
```
你会得到一个实现了 ```IProxy.class``` 的类实例数组，就是上面的 ```proxys```。
当你调用 ```IProxy``` 的 ```printName()``` 方法时，例如： ```proxys[0].printName();```，你将会得到一个 ```I'm B.``` 的字符串输出。
这可能会帮助你很轻松的实现代理模式。

##### 高阶用法：priority
你可以设置拥有相同 ```tag``` 的类的优先级。  
例如： 假设 ```First.class``` 比 ```Second.class``` 拥有更高的优先级，你可以如下方式添加注解：
```java
@CompileScan(priority = 1)
public class First {}
```
```java
@CompileScan(priority = 2)
public class Second {}
```
然后你会得到一个数组，数组里面会把 ```First.class``` 按照优先级排在 ```Second.class``` 前面。
> 越大的数字，相比小的数字，拥有越低的优先级。

##### 成员的说明
| 成员       | 说明                     |
|------------|--------------------------|
| tag        | 标记不同分组的类          |
| isDefault  | 用于区分类是否为默认      |
| priority   | 标记拥有相同tag的类优先级 |

> 更多的代码样例，参照 [Java样例](https://github.com/JoelZhu/CompileScanner/tree/main/app_sample_java) | [Kotlin样例](https://github.com/JoelZhu/CompileScanner/tree/main/app_sample_kotlin) | [多module样例](https://github.com/JoelZhu/CompileScanner/tree/main/app_sample_multimodule_app)

### ❗ 问题
如果你遇到了编译错误，或者在使用过程中遇到了与期望不符的情况，在app module的 build.gradle 中添加如下代码，来打开日志。
```groovy
android {
}

Scanner {
    enableLog = true
}

dependencies {
}
```
然后，在出现问题的时候，将完整的编译日志贴到新的issue中，指派给我，我会尽力并且快的解决这个问题。

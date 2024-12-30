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
1. 在项目的```build.gradle```中如下声明：
```groovy
dependencies {
    classpath "io.github.joelzhu:scanner-plugin:[RELEASED_VERSION]"
}
```
2. 在application工程的```build.gradle```中如下声明：
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
快速指南: <a href="#基础用法">基础用法</a> | <a href="#高阶用法-tag">高阶用法：tag</a> | <a href="#高阶用法-group">高阶用法：group</a> | <a href="#高阶用法default">高阶用法：default</a> | <a href="#高阶用法priority">高阶用法：priority</a> | <a href="#属性说明">属性说明</a>

##### 基础用法
下面是基础的使用方式：
```java
@CompileScan
public class ExampleClass implements IExample {}
```
你有很多类似上面这种类，他们全都注解了 ```CompileScan```，然后，你不需要去一个一个调用他们，你完全可以很轻松的获取到他们。调用 ```Scanner.getAnnotatedClasses();``` 方法或者 ```Scanner.getAnnotatedInstances(IExample.class);``` 方法，你将会得到一堆类，或者是一堆 ```IExample``` 的实例化数组。

##### 高阶用法: tag
如果你想用类自有属性去标记这个类本身，那么你可以使用 ```tag``` 标记，比如说，你有三个类如下所示：
```java
@CompileScan(tag = "A")
public class ExampleAClass implements IExample {}
```
```java
@CompileScan(tag = "B")
public class ExampleBClass implements IExample {}
```
```java
@CompileScan(tag = "C")
public class ExampleCClass implements IExample {}
```
通过调用：```final IExample[] examples = Scanner.getAnnotatedInstances(new Options.Builder().tag("A").create(), IExample.class);``` 方法，你可以很轻松的获得到 ```ExampleAClass```。

##### 高阶用法: group
考虑到单独一个 ```tag``` 可能无法满足需求，我们提供了另外一个属性： ```group``` 来帮助你更好地使用这个库。比如说你拥有两个类，他们都是属于 ```Life``` 的，但是你需要根据不同的使用场景来区分这两个类，那么你可以如下实现：
```java
@CompileScan(tag = "Life", group = "Work")
public class Work implements ILife {}
```
```java
@CompileScan(tag = "Life", group = "Rest")
public class Sleep implements ILife {}
```
然后，你可以获取属于 ```work``` 属性的类，通过这种调用： ```final ILife[] works = Scanner.getAnnotatedInstances(new Options.Builder().tag("Life").group("Work").create(), ILife.class);``` 这个方法。

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
> 越大的数字，相比小数字而言，拥有越低的优先级。

##### 属性说明
| 属性       | 说明                        |
|------------|-----------------------------|
| tag        | 拥有区分不同的类             |
| group      | 用于区分相同tag类的不同属性  |
| isDefault  | 用于区分类是否为默认         |
| priority   | 标记拥有相同tag的类优先级    |

> 更多的代码样例，参照 [Java样例](https://github.com/JoelZhu/CompileScanner/tree/main/app_sample_java) | [Kotlin样例](https://github.com/JoelZhu/CompileScanner/tree/main/app_sample_kotlin) | [多module样例](https://github.com/JoelZhu/CompileScanner/tree/main/app_sample_multimodule_app)

### ❗ 问题
如果你遇到了编译错误，或者在使用过程中遇到了与期望不符的情况，在application工程的```build.gradle```中添加如下代码，来打开日志。
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

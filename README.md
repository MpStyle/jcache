# jCache

Lazy and naive cache using in memory SQLite database

[![Build Status](https://travis-ci.org/MpStyle/jcache.svg?branch=master)](https://travis-ci.org/MpStyle/jcache)

## Installation

### Maven
```xml
<repositories>
    ...
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
    ...
</repositories>
...
<dependency>
    <groupId>com.github.MpStyle</groupId>
    <artifactId>jcache</artifactId>
    <version>v1.1.0</version>
</dependency>
```

### Gradle
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

...

dependencies {
    compile 'com.github.MpStyle:jcache:v1.1.0'
}

```

## Usages

```java
Cache cache = new Cache();
cache.add("a", "b");

[...]

String value = cache.get("a");
```

Or:

```
Cache cache = new Cache();
CacheItem item = new CacheItem();

item.setKey("a");
item.setTtl(3);
item.setValue("b");

cache.add(item);

[...]

String value = cache.get("a");

```

To clear the cache:

```

cache.clear();

```
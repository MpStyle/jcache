# jCache

Lazy and naive cache using in memory SQLite or H2 database

[![Build Status](https://travis-ci.org/MpStyle/jcache.svg?branch=master)](https://travis-ci.org/MpStyle/jcache) [![](https://jitpack.io/v/MpStyle/jcache.svg)](https://jitpack.io/#MpStyle/jcache)

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
    <version>v2.0.0</version>
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
    compile 'com.github.MpStyle:jcache:v2.0.0'
}

```

## Usages

Create a connection using SQLite:
```java
Cache cache = CacheBuilder.getSqliteCache();
```

or using H2:
```java
Cache cache = CacheBuilder.getH2Cache();
```

Insert a new item in the cache:
```java
Cache cache = CacheBuilder.getSqliteCache();
cache.add("a", "b");

[...]

String value = cache.get("a");
```

Or:

```java
Cache cache = CacheBuilder.getSqliteCache();
CacheItem item = new CacheItem();

item.setKey("a");
item.setTtl(3);
item.setValue("b");

cache.add(item);

[...]

String value = cache.get("a");
```

To clear the cache:

```java
cache.clear();
```

To delete a specific item
```java
cache.delete("key_of_the_item_to_delete");
```

## Version

### 1.2.0
- Added method exists.
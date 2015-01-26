# GIMSearch
Current Version: 0.11

Google Image Search library. Uses direct http pages, not the API.

## Importing in Android Studio

### Add Repo

Add following to build.gradle for app.

```Gradle
repositories {
    maven {
        url 'http://repo.bahadir.me'
    }
}
```

And this to your dependencies:
```Gradle
compile 'me.bahadir:gimsearch:[VERSION]'
````

## Usage
```Java
IMSearch gimSearch = new GIMSearch();

//Create image list
List<GIMSearch.Image> images = null;
try {
    images = gimSearch.getSinglePage(search);
} catch (GIMSearch.GIMSearchParseException e) {
    e.printStackTrace();
}
```

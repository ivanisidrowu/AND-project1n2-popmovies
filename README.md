# AND-project1n2-popmovies
My Udacity Android Nanodegree project 1&amp;2

## API
I use TMDB API. 
https://www.themoviedb.org/documentation/api

## Configure your API key
Add API key in build.gradle file which is in "app" directory.
```
buildConfigField STRING, "API_KEY", "\"${YOUR_APIKEY}\""
```
## Screenshots
<img src="https://github.com/ivanisidrowu/AND-project1n2-popmovies/blob/master/screenshot/screenshot1.png" width="400" height="712" />
<img src="https://github.com/ivanisidrowu/AND-project1n2-popmovies/blob/master/screenshot/screenshot2.png" width="400" height="712" />
<img src="https://github.com/ivanisidrowu/AND-project1n2-popmovies/blob/master/screenshot/screenshot4.png"/>

## Open source libs I use in this project

Retrofit https://github.com/square/retrofit

RxJava https://github.com/ReactiveX/RxJava

RxAndroid https://github.com/ReactiveX/RxAndroid

Dagger2 https://github.com/google/dagger

Glide https://github.com/bumptech/glide

Butterknife https://github.com/JakeWharton/butterknife

EventBus https://github.com/greenrobot/EventBus

Realm for Android https://realm.io/news/realm-for-android/

CustomLinearLayoutManager https://github.com/serso/android-linear-layout-manager/blob/master/lib/src/main/java/org/solovyev/android/views/llm/LinearLayoutManager.java

## Plugin
Retrolamda https://github.com/evant/gradle-retrolambda

## Configure retrolamda
Change jdk location in build.gradle under "app" directory.
```
retrolambda {
        jdk '${JAVA8_HOME_PATH}'
        oldJdk '${JAVA7_HOME_PATH}'
        javaVersion JavaVersion.VERSION_1_7
        jvmArgs '-noverify'
        defaultMethods false
        incremental true
}
```

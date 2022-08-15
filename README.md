# AdvancedMarqueeTextView
## Introduction
⚠️**Warning**⚠️

This library is heavily reliant on using *Java Reflections* to access private methods within the Android API. This is obviously a rather 'hacky' solution and might cause the library to not work in future releases of Android.
## Implementation
1. Add the jitpack.io repository in your project-level build.gradle-file.
```gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
2. Please add the library to your module-level build.gradle-file. Because this library works with reflections and use of that feature has been made more restricted in recent Android versions, you need to implement another library which enables all reflections. We recommend [FreeReflection](https://github.com/tiann/FreeReflection#freereflection).
```gradle
dependencies {
    ...
    implementation 'com.github.tiann:FreeReflection:3.1.0'
    implementation 'com.github.JonathanZopf:AdvancedMarqueeTextView:0.1'
}
```
3. (When using [FreeReflection](https://github.com/tiann/FreeReflection#freereflection)). Override the ```attachBaseContext(Context)```method in your activity with the folowing:
```java
@Override
protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    Reflection.unseal(base);
}
```

4. Implement TextView with marquee (as ```AdvancedMarqueeTextView```) in your layout. Example:
```xml
    <com.illubytes.advancedMarqueeTextView.AdvancedMarqueeTextView
        ...
        android:ellipsize="marquee"
        android:marqueeRepeatLimit="marquee_forever"
        android:padding="10dp"
        android:scrollHorizontally="true"
        android:singleLine="true"/>
```
5. Get your ```AdvancedMarqueeTextView``` in your activity and call the ```init(onStarted, minimumTimeStartedMs, isDebug)``` method in there. Example:
```java
AdvancedMarqueeTextView stockBanner = findViewById(R.id.tv_stocks_banner);
stockBanner.init(tv -> tv.setText(getStocksText()), AdvancedMarqueeTextView.DEFAULT_MINIMUM_TIME_STARTED, true);
```

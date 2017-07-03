
# The MIT License (MIT) Copyright (c) 2017 Smart&Soft

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

# Background Detector

A simple component you can integrate into your Android app in order to detect if the app goes to background or foreground

This version of the library has no additional dependencies.

## Usage

### From the Smart&Soft Nexus

/!\ Library releases are **NOT** available on Maven Central /!\

**Gradle**

Because the library is available only on the Smart&Soft Nexus, you have to add the following lines to your `build.gradle` project file :

```groovy
allprojects
{
  repositories
  {
    maven
    {
      credentials
      {
        username nexusUsername
        password nexusPassword
      }
      
      url nexusRepositoryUrl
    }
  }
}
```

Then, you can add the following line into the `dependencies` bloc of your `build.gradle` module file :

```groovy
compile ("com.smartnsoft:backgrounddetector:1.2")
```

**Maven**

```xml
<dependency>
  <groupId>com.smartnsoft</groupId>
  <artifactId>backgrounddetector</artifactId>
  <version>1.2</version>
</dependency>
```

### Without droid4me

You need the create a customn `Application` class for your application and register a listener that will listen to the activies life cycle throught the implementation of the `ActivityLifecycleCallbacks` interface.

```java
public final class CustomApplication
    extends Application
    implements ActivityLifecycleCallbacks
{

  @Override
  public void onCreate()
  {
    super.onCreate();
    registerActivityLifecycleCallbacks(this);
  }

  @Override
  public void onTerminate()
  {
    super.onTerminate();
    unregisterActivityLifecycleCallbacks(this);
  }

  @Override
  public void onActivityCreated(Activity activity, Bundle savedInstanceState)
  {

  }

  @Override
  public void onActivityStarted(Activity activity)
  {

  }

  @Override
  public void onActivityResumed(Activity activity)
  {

  }

  @Override
  public void onActivityPaused(Activity activity)
  {

  }

  @Override
  public void onActivityStopped(Activity activity)
  {

  }

  @Override
  public void onActivitySaveInstanceState(Activity activity, Bundle outState)
  {

  }

  @Override
  public void onActivityDestroyed(Activity activity)
  {

  }
}
```

Do not forget to update your `AndroidManifest.xml` file in order to specify that tour application use a custom `Application` class :

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest 
  xmlns:android="http://schemas.android.com/apk/res/android"
  package="com.smartnsoft.myapplication"
>

  <application
    android:name=".CustomApplication"
    android:allowBackup="true"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:supportsRtl="true"
    android:theme="@style/AppTheme"
  >
    <!-- activities declaration -->
  </application>

</manifest>
```

Your custom `Application` class can also implement the `OnVisibilityChangedListener` to be notify when the application goes to background or foreground :

```java
public final class CustomApplication
    extends Application
    implements ActivityLifecycleCallbacks, OnVisibilityChangedListener
{

  //...
 
  @Override
  public void onAppGoesToBackground(Context context)
  {

  }

  @Override
  public void onAppGoesToForeground(Context context)
  {

  }
}
```

Then, you have to declare a `BackgroundDetectorHandler` and notify it each time your application detects that an `Activity` is resumed or paused. This `BackgroundDetectorHandler` takes a `BackgroundDetectorCallback` with which one you can tune some parameters like :

* the default visibility in order to be notified or not the first time the app goes to foreground ;
* the `OnVisibilityChangedListener`.

```java
public final class CustomApplication
    extends Application
    implements ActivityLifecycleCallbacks, OnVisibilityChangedListener
{

  private BackgroundDetectorHandler backgroundDetectorHandler;

  @Override
  public void onCreate()
  {
    super.onCreate();
    registerActivityLifecycleCallbacks(this);
    backgroundDetectorHandler = new BackgroundDetectorHandler(new BackgroundDetectorCallback(BackgroundDetectorHandler.ON_ACTIVITY_RESUMED, this));
  }

  //...

  @Override
  public void onActivityResumed(Activity activity)
  {
    backgroundDetectorHandler.onActivityResumed(activity);
  }

  @Override
  public void onActivityPaused(Activity activity)
  {
    backgroundDetectorHandler.onActivityPaused(activity);
  }
  
  @Override
  public void onAppGoesToBackground(Context context)
  {
    Log.d("CustomApplication", "onAppGoesToBackground");
  }

  @Override
  public void onAppGoesToForeground(Context context)
  {
    Log.d("CustomApplication", "onAppGoesToForeground");
  }
  
}

```

### With droid4me

When you use the droid4me framework, you do not have to listen for the activities life cycle through the `ActivityLifecycleCallbacks` interface. In fact, you can directly use your `ApplicationNameInterceptor` class that should implements of the `ActivityController.Interceptor` interface.

In this class, you can directly declare a `BackgroundDetectorHandler` that takes a `BackgroundDetectorCallback` with which one you can tune some parameters like :

* the default visibility in order to be notified or not the first time the app goes to foreground ;
* the `OnVisibilityChangedListener`.

**Note** : In this exemple, the class directly implements the `OnVisibilityChangedListener` interface.

```java
public final class ApplicationNameInterceptor
    implements ActivityController.Interceptor, OnVisibilityChangedListener
{

  private BackgroundDetectorHandler backgroundDetectorHandler;

  public ApplicationNameInterceptor()
  {
    backgroundDetectorHandler = new BackgroundDetectorHandler(new BackgroundDetectorCallback(BackgroundDetectorHandler.ON_ACTIVITY_RESUMED, this));
  }

  @Override
  public void onAppGoesToBackground(Context context)
  {

  }

  @Override
  public void onAppGoesToForeground(Context context)
  {

  }

  @SuppressWarnings("unchecked")
  @Override
  public void onLifeCycleEvent(final Activity activity, Object component, InterceptorEvent interceptorEvent)
  {
    if (component != null)
    {
      // It's a Fragment
      //...
    }
    else
    {
      // It's an Activity
      //...
    }
  }

}
```

Finally, you can notify your `BackgroundDetectorHandler` instance each time your interceptor detects that an `Activity` is resumed or paused :

```java
public final class ApplicationNameInterceptor
    implements ActivityController.Interceptor, OnVisibilityChangedListener
{

  private BackgroundDetectorHandler backgroundDetectorHandler;

  public ApplicationNameInterceptor()
  {
    backgroundDetectorHandler = new BackgroundDetectorHandler(new BackgroundDetectorCallback(BackgroundDetectorHandler.ON_ACTIVITY_RESUMED, this));
  }

  @Override
  public void onAppGoesToBackground(Context context)
  {
    Log.d("ApplicationNameInterceptor", "onAppGoesToBackground");
  }

  @Override
  public void onAppGoesToForeground(Context context)
  {
    Log.d("ApplicationNameInterceptor", "onAppGoesToForeground");
  }

  @SuppressWarnings("unchecked")
  @Override
  public void onLifeCycleEvent(final Activity activity, Object component, InterceptorEvent interceptorEvent)
  {
    if (component != null)
    {
      // It's a Fragment
      //...
    }
    else
    {
      // It's an Activity
      if(interceptorEvent == InterceptorEvent.onStart)
      {
        backgroundDetectorHandler.onActivityResumed(activity);
      }

      if(interceptorEvent == InterceptorEvent.onStop)
      {
        backgroundDetectorHandler.onActivityPaused(activity);
      }
    }
  }

}
```

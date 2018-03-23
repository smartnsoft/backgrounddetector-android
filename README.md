[ ![Download](https://api.bintray.com/packages/smartnsoft/maven/backgrounddetector/images/download.svg) ](https://bintray.com/smartnsoft/maven/backgrounddetector/_latestVersion)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![TeamCity status](https://ci.smartnsoft.com/app/rest/builds/buildType(id:smartnsoft_backgrounddetector_android)/statusIcon)](https://ci.smartnsoft.com/viewType.html?buildTypeId=smartnsoft_backgrounddetector_android)

# Background Detector

A simple component you can integrate into your Android app in order to detect if the app goes to background or foreground

This version of the library has no additional dependencies.

## Usage

### 1. Without droid4me

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

### 2. With droid4me

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

## Download

To add Background Detector to your project, include the following in your **app module** `build.gradle` file:

```groovy
implementation ("com.smartnsoft:backgrounddetector:${latest.version}")
```

## More

Check [this article](https://blog.rolandl.fr/2015-11-11-android-detecter-si-une-application-passe-au-premier-plan-ou-en-arriere-plan.html) (in french) if you want to learn more how the library works.

## License

This library is available under the MIT license. See the LICENSE file for more info.

## Author

This library was proudly made by [Smart&Soft](https://smartnsoft.com/), Paris FRANCE

package com.smartnsoft.backgrounddetector.sample;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.smartnsoft.backgrounddetector.BackgroundDetectorCallback;
import com.smartnsoft.backgrounddetector.BackgroundDetectorHandler;
import com.smartnsoft.backgrounddetector.BackgroundDetectorHandler.OnVisibilityChangedListener;

/**
 * @author Ludovic Roland
 * @since 2018.03.22
 */
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

  @Override
  public void onActivityCreated(Activity activity, Bundle savedInstanceState)
  {

  }

  @Override
  public void onTerminate()
  {
    super.onTerminate();
    unregisterActivityLifecycleCallbacks(this);
  }

  @Override
  public void onActivityStarted(Activity activity)
  {

  }

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

  @Override
  public void onAppGoesToBackground(Context context)
  {
    Toast.makeText(this, "onAppGoesToBackground", Toast.LENGTH_LONG).show();
  }

  @Override
  public void onAppGoesToForeground(Context context)
  {
    Toast.makeText(this, "onAppGoesToForeground", Toast.LENGTH_LONG).show();
  }

}

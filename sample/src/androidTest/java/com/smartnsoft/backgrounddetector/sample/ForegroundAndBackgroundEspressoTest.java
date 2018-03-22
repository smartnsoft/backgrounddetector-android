package com.smartnsoft.backgrounddetector.sample;

import java.io.File;

import android.Manifest;
import android.app.Activity;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;

/**
 * @author Ludovic Roland
 * @since 2018.03.22
 */
@RunWith(AndroidJUnit4.class)
public final class ForegroundAndBackgroundEspressoTest
{

  private final ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule(MainActivity.class);

  private final GrantPermissionRule grantPermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);

  @Rule
  public final RuleChain ruleChain = RuleChain.outerRule(grantPermissionRule).around(activityTestRule);

  private UiDevice uiDevice;

  private Activity activity;

  @Before
  public void init()
  {
    uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    activity = activityTestRule.getActivity();
  }

  @Test
  public void testBackgroundAndForeground()
      throws RemoteException, UiObjectNotFoundException
  {
    sendAppToBackground();
    sendAppToForeground();
  }

  private void sendAppToForeground()
      throws RemoteException, UiObjectNotFoundException
  {
    uiDevice.pressHome();
    uiDevice.pressRecentApps();
    uiDevice.findObject(new UiSelector().text(activity.getString(activity.getApplicationInfo().labelRes))).click();

    captureScreenshot();
  }

  private void sendAppToBackground()
  {
    uiDevice.pressHome();
    captureScreenshot();
  }

  private void captureScreenshot()
  {
    SystemClock.sleep(500); // Wait for screen to change
    final File imageDir = new File("/sdcard/Screenshots/");

    if (imageDir.exists() == false)
    {
      final boolean mkdir = imageDir.mkdir();
    }

    uiDevice.takeScreenshot(new File("/sdcard/Screenshots/" + System.currentTimeMillis() + ".png"), 0.5f, 25);
  }

}

// The MIT License (MIT)
//
// Copyright (c) 2017 Smart&Soft
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

package com.smartnsoft.backgrounddetector;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

/**
 * The handler to use in order to count the activities
 *
 * @author Ludovic Roland
 * @since 2015.12.08
 */
public final class BackgroundDetectorHandler
    extends Handler
{

  /**
   * Interface definition for a callbackto be invoked when app goes to foreground or background
   */
  public interface OnVisibilityChangedListener
  {

    /**
     * Called when the app goes to foreground
     *
     * @param context the context passed to the {@link BackgroundDetectorHandler#onActivityPaused(Context)} or {@link BackgroundDetectorHandler#onActivityResumed(Context)} (Context)} methods.
     */
    void onAppGoesToBackground(@Nullable Context context);

    /**
     * Called when the app goes to background
     *
     * @param context the context passed to the {@link BackgroundDetectorHandler#onActivityPaused(Context)} or {@link BackgroundDetectorHandler#onActivityResumed(Context)} (Context)} methods.
     */
    void onAppGoesToForeground(@Nullable Context context);
  }

  /**
   * Flag used to say that an activity has been resumed
   */
  public static final int ON_ACTIVITY_RESUMED = 0;

  /**
   * Flag used to say that an activity has been paused
   */
  public static final int ON_ACTIVITY_PAUSED = ON_ACTIVITY_RESUMED + 1;

  private static final int VISIBILITY_DELAY_IN_MS = 300;

  private int activityCounter = 0;

  /**
   * {@inheritDoc}
   */
  public BackgroundDetectorHandler(@Nullable BackgroundDetectorCallback callback)
  {
    super(callback);
  }

  /**
   * Calling this method signals the library that an activity is made resumed.
   * The library keeps a counter and when at least one page of the application becomes visible,
   * the {@link OnVisibilityChangedListener#onAppGoesToForeground(Context)} method is called.
   *
   * @param context a context that can be used later in the {@link OnVisibilityChangedListener#onAppGoesToForeground(Context)} method
   */
  public void onActivityResumed(@Nullable Context context)
  {
    activityCounter++;

    if (activityCounter > 0)
    {
      removeMessages(BackgroundDetectorHandler.ON_ACTIVITY_PAUSED);

      final Message message = Message.obtain();
      message.what = BackgroundDetectorHandler.ON_ACTIVITY_RESUMED;
      message.obj = context;

      sendMessageDelayed(message, BackgroundDetectorHandler.VISIBILITY_DELAY_IN_MS);
    }
  }

  /**
   * Calling this method signals the library that an activity is made paused.
   * The library keeps a counter and when all pages of the application become invisible, the
   * the {@link OnVisibilityChangedListener#onAppGoesToBackground(Context)} (Context)} method is called.
   *
   * @param context a context that can be used later in the {@link OnVisibilityChangedListener#onAppGoesToBackground(Context)} method
   */
  public void onActivityPaused(Context context)
  {
    activityCounter--;

    if (activityCounter <= 0)
    {
      activityCounter = 0;

      removeMessages(BackgroundDetectorHandler.ON_ACTIVITY_RESUMED);

      final Message message = Message.obtain();
      message.what = BackgroundDetectorHandler.ON_ACTIVITY_PAUSED;
      message.obj = context;

      sendMessageDelayed(message, BackgroundDetectorHandler.VISIBILITY_DELAY_IN_MS);
    }
  }

}
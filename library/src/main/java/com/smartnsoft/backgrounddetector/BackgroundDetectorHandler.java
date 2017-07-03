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

/**
 * @author Ludovic Roland
 * @since 2015.12.08
 */
public final class BackgroundDetectorHandler
    extends Handler
{

  public interface OnVisibilityChangedListener
  {

    void onAppGoesToBackground(Context context);

    void onAppGoesToForeground(Context context);
  }

  public static final int ON_ACTIVITY_RESUMED = 0;

  public static final int ON_ACTIVITY_PAUSED = ON_ACTIVITY_RESUMED + 1;

  private static final int VISIBILITY_DELAY_IN_MS = 300;

  private int activityCounter = 0;

  public BackgroundDetectorHandler(BackgroundDetectorCallback callback)
  {
    super(callback);
  }

  public void onActivityResumed(Context context)
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
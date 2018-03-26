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

import com.smartnsoft.backgrounddetector.BackgroundDetectorHandler.OnVisibilityChangedListener;

/**
 * Callback interface use when instantiating the {@link BackgroundDetectorHandler}.
 *
 * @author Ludovic Roland
 * @since 2015.12.08
 */
public final class BackgroundDetectorCallback
    implements Handler.Callback
{

  private int previousVisibility;

  private OnVisibilityChangedListener onVisibilityChangedListener;

  public BackgroundDetectorCallback()
  {
    this(BackgroundDetectorHandler.ON_ACTIVITY_RESUMED);
  }

  public BackgroundDetectorCallback(int defaultVisibility)
  {
    this(defaultVisibility, null);
  }

  public BackgroundDetectorCallback(@Nullable OnVisibilityChangedListener onVisibilityChangedListener)
  {
    this(BackgroundDetectorHandler.ON_ACTIVITY_RESUMED, onVisibilityChangedListener);
  }

  public BackgroundDetectorCallback(int defaultVisibility,
      @Nullable OnVisibilityChangedListener onVisibilityChangedListener)
  {
    previousVisibility = defaultVisibility;
    this.onVisibilityChangedListener = onVisibilityChangedListener;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean handleMessage(Message message)
  {
    if (previousVisibility != message.what)
    {
      previousVisibility = message.what;

      if (onVisibilityChangedListener != null)
      {
        if (message.what == BackgroundDetectorHandler.ON_ACTIVITY_RESUMED)
        {
          onVisibilityChangedListener.onAppGoesToForeground((Context) message.obj);
        }
        else
        {
          onVisibilityChangedListener.onAppGoesToBackground((Context) message.obj);
        }
      }
    }

    return true;
  }

  @Nullable
  public OnVisibilityChangedListener getOnVisibilityChangedListener()
  {
    return onVisibilityChangedListener;
  }

  /**
   * Set a new {@link OnVisibilityChangedListener} which will be called when the app goes to foreground of background.
   *
   * @param onVisibilityChangedListener the listener to set
   */
  public void registerOnVisibilityChangedListener(@Nullable OnVisibilityChangedListener onVisibilityChangedListener)
  {
    this.onVisibilityChangedListener = onVisibilityChangedListener;
  }

  /**
   * Remove the {@link OnVisibilityChangedListener} object that was previously registered with {@link #registerOnVisibilityChangedListener(OnVisibilityChangedListener)}
   */
  public void unregisterOnVisibilityChangedListener()
  {
    onVisibilityChangedListener = null;
  }

}
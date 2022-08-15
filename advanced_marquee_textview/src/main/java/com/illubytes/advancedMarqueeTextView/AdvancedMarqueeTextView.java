package com.illubytes.advancedMarqueeTextView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * TextView with marquee. Gives the possibility to set listeners when scrolling began
 */
@SuppressLint("AppCompatCustomView")
public class AdvancedMarqueeTextView extends TextView {
    public static final long DEFAULT_MINIMUM_TIME_STARTED = 100;
    private static final String TAG = "AdvancedMarqueeTextView";
    private OnMarqueeBannerEvents bannerEvents;
    private long lastTimeTextSet;
    private long minimumTimeStartedMs = DEFAULT_MINIMUM_TIME_STARTED;
    private boolean debug = false;

    /*
    Base class constructors
     */
    public AdvancedMarqueeTextView(Context context) {
        super(context);
    }

    public AdvancedMarqueeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AdvancedMarqueeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AdvancedMarqueeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Byte marqueeStatus = getMarqueeStatus();
        if (debug) {
            Log.d(TAG, "Marquee status: " + marqueeStatus);
        }
        if (marqueeStatus != null) {
                if (marqueeStatus == 1) {
            if (lastTimeTextSet + minimumTimeStartedMs < System.currentTimeMillis()) {
                if (bannerEvents != null) {
                    if (debug) {
                        Log.d(TAG, "Marquee is finished! Now invoking onStarted.");
                    }
                    bannerEvents.onStarted(this);
                } else {
                    Log.d(TAG, "Marquee is finished, however you have not specified what to do in that case. Please specify that using init() or setOnMarqueeBannerEvents");
                }
                lastTimeTextSet = System.currentTimeMillis();
            } else {
                Log.d(TAG, String.format("Marquee reported status 1 (started) but the timeout of %s ms has prevented from executing onStarted() again.", minimumTimeStartedMs));
            }
        }
    } else {
            Log.d(TAG, "marqueeStatus is null! This likely means there was an exception within getMarqueeStatus().");
        }
        super.onDraw(canvas);
    }

    @Nullable
    private Byte getMarqueeStatus() {
        try {
            //Get mMarquee from TextView
            Field fieldMarquee = getClass().getSuperclass().getDeclaredField("mMarquee");
            if (!fieldMarquee.isAccessible()) {
                fieldMarquee.setAccessible(true);
            }
            Object marqueeObject = fieldMarquee.get(this);

            //Get byte mStatus from mMarquee
            Field fieldStatus = marqueeObject.getClass().getDeclaredField("mStatus");
            if (!fieldStatus.isAccessible()) {
                fieldStatus.setAccessible(true);
            }
            Object statusObject = fieldStatus.get(marqueeObject);

            //Cast and return status
            return (Byte) statusObject;
        } catch (Throwable e) {
            Log.e(TAG, "Error while trying to determine marquee status. This is likely due to issues with reflection which is necessary for this library to work. Please report this issue on github (https://github.com/JonathanZopf/AdvancedMarqueeTextView).");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Initializes AdvancedMarqueeTextView with necessary information. Not absolutely necessary but definitely recommended to use this method because it's the most simple.
     * @param onMarqueeBannerEvents Specifies what to do when banner has started scrolling. Implementation of OnMarqueeBannerEvents functional interface.
     * @param minimumTimeStartedMs Sets timeout for triggering onStarted again. Prevents bug, where in combination with setText() the marquee would be stuck in a loop with status 1 and therefore not move. Should be at least 100ms.
     * @param isDebug Sets debug mode. Debug mode prints useful information regarding this library while running. Should be disabled for production.
     */
    public void init(OnMarqueeBannerEvents onMarqueeBannerEvents, long minimumTimeStartedMs, boolean isDebug) {
        this.bannerEvents = onMarqueeBannerEvents;
        this.minimumTimeStartedMs = minimumTimeStartedMs;
        this.debug = isDebug;
        setSelected(true);
    }

    /**
     * Specifies what to do when banner has started scrolling.
     * @param events Implementation of OnMarqueeBannerEvents functional interface.
     */
    public void setOnMarqueeBannerEvents(OnMarqueeBannerEvents events) {
        bannerEvents = events;
    }

    /**
     * Sets timeout for triggering onStarted again. Prevents bug, where in combination with setText() the marquee would be stuck in a loop with status 1 and therefore not move.
     * @param minimumTimeStartedMs The timeout for being able triggering onStarted again with status 1. Should be at least 100ms.
     */
    public void setMinimumTimeStartedMs(long minimumTimeStartedMs) {
        this.minimumTimeStartedMs = minimumTimeStartedMs;
    }

    /**
     * Sets debug mode. Debug mode prints useful information regarding this library while running. Should be disabled for production.
     * @param isDebug Is debug mode enabled?
     */
    public void setDebug(boolean isDebug) {
        this.debug = isDebug;
    }
}

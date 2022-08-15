package com.illubytes.advancedMarqueeTextView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.Nullable;
import java.lang.reflect.Field;

@SuppressLint("AppCompatCustomView")
public class AdvancedMarqueeTextView extends TextView {
    private long lastTimeTextSet;
    private OnMarqueeBannerEvents bannerEvents;
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
        Log.i("Info", "marquee status: " + marqueeStatus);
        if (marqueeStatus != null && marqueeStatus == 1 && bannerEvents != null && lastTimeTextSet + 1000 < System.currentTimeMillis()) {
            Log.i("Info", "Marquee finished!");
            bannerEvents.onFinished(this);
            lastTimeTextSet = System.currentTimeMillis();
            setSelected(true);
        }
        invalidate();
        super.onDraw(canvas);
    }

    @Nullable
    private Byte getMarqueeStatus() {
        try {
            Field fieldMarquee = getClass().getSuperclass().getDeclaredField("mMarquee");
            if (!fieldMarquee.isAccessible()) {
                fieldMarquee.setAccessible(true);
            }
            Object marqueeObject = fieldMarquee.get(this);

            Field fieldStatus = marqueeObject.getClass().getDeclaredField("mStatus");
            if (!fieldStatus.isAccessible()) {
                fieldStatus.setAccessible(true);
            }
            Object statusObject = fieldStatus.get(marqueeObject);
            return (Byte) statusObject;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
    public void setOnMarqueeBannerEvents(OnMarqueeBannerEvents events) {
        bannerEvents = events;
    }
}

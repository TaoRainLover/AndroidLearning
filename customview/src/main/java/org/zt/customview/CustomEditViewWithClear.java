package org.zt.customview;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.core.content.ContextCompat;

import java.util.Objects;

public class CustomEditViewWithClear extends androidx.appcompat.widget.AppCompatEditText {
    private static final String TAG = "CustomEditViewWithClear";
    // 将清除 icon 的引用设置为 null
    private Drawable iconDrawable = null;
    public CustomEditViewWithClear(Context context) {
        super(context);
    }

    // public CustomEditViewWithClear(Context context, AttributeSet attrs) {
    //     super(context, attrs);
    // }

    public CustomEditViewWithClear(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomEditViewWithClear,
                0, 0);

        try {
            // mShowText = a.getBoolean(R.styleable.PieChart_showText, false);
            // textPos = a.getInteger(R.styleable.PieChart_labelPosition, 0);
            int iconID = a.getResourceId(R.styleable.CustomEditViewWithClear_clearIcon, 0);
            // 判断是否是一个有效的 icon id
            if (iconID != 0) {
                iconDrawable = ContextCompat.getDrawable(context, iconID);
            }
        } finally {
            a.recycle();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event != null && iconDrawable != null) {
            float x = event.getX();
            float y = event.getY();
            int width = getWidth();
            int height = getHeight();
            // 获取图标的固有宽度和高度
            int intrinsicWidth = iconDrawable.getIntrinsicWidth();
            int intrinsicHeight = iconDrawable.getIntrinsicHeight();
            Log.d(TAG, "onTouchEvent: x = " + x);
            Log.d(TAG, "onTouchEvent: y = " + y);
            Log.d(TAG, "onTouchEvent: width = " + width);
            Log.d(TAG, "onTouchEvent: height = " + height);
            Log.d(TAG, "onTouchEvent: intrinsicWidth = " + intrinsicWidth);
            Log.d(TAG, "onTouchEvent: intrinsicHeight = " + intrinsicHeight);
            if (event.getAction() == MotionEvent.ACTION_UP
                    && x > width - intrinsicWidth && x < width
                    && y > height / 2 - intrinsicHeight / 2 && y < height / 2 + intrinsicHeight / 2) {

                // 清除文本
                if (getText() != null) {
                    getText().clear();
                }
            }
        }
        performClick();
        return super.onTouchEvent(event);
    }

    // 监听文本内容变化
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        toggleClearIcon();
    }


    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        toggleClearIcon();
    }

    private void toggleClearIcon() {
        Drawable icon;
        String text = Objects.requireNonNull(getText()).toString();
        // 满足当前获取焦点同时内容不为空时显示清除 icon
        if (isFocused() && !text.isEmpty()) {
            icon = iconDrawable;
        } else {
            icon = null;
        }
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, icon, null);
    }

}

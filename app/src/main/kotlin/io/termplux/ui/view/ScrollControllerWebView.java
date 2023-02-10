package io.termplux.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kongzue.dialogx.interfaces.ScrollController;


public class ScrollControllerWebView extends WebView implements ScrollController {

    private boolean lockScroll;

    public ScrollControllerWebView(@NonNull Context context) {
        super(context);
    }

    public ScrollControllerWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollControllerWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScrollControllerWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    @Deprecated
    public boolean isLockScroll() {
        return lockScroll;
    }

    @Override
    public void lockScroll(boolean lockScroll) {
        this.lockScroll = lockScroll;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (lockScroll) return false;
        return super.onTouchEvent(event);
    }

    @Override
    public int getScrollDistance() {
        return getScrollY();
    }

    @Override
    public boolean isCanScroll() {
        return true;
    }
}

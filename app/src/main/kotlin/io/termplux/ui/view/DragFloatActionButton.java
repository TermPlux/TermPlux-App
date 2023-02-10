package io.termplux.ui.view;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DragFloatActionButton extends FloatingActionButton {

    private int parentHeight;
    private int parentWidth;

    public DragFloatActionButton(Context context) {
        super(context);
    }

    public DragFloatActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragFloatActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private int lastX;
    private int lastY;
    private boolean isDrag;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                setPressed(true);
                isDrag = false;
                getParent().requestDisallowInterceptTouchEvent(true);
                lastX = rawX;
                lastY = rawY;
                ViewGroup parent;
                if (getParent() != null) {
                    parent = (ViewGroup) getParent();
                    parentHeight = parent.getHeight();
                    parentWidth = parent.getWidth();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (parentHeight <= 0 || parentWidth == 0) {
                    isDrag = false;
                    break;
                } else {
                    isDrag = true;
                }
                int dx = rawX - lastX;
                int dy = rawY - lastY;
                int distance = (int) Math.sqrt(dx * dx + dy * dy);
                if (distance == 0) {
                    isDrag = false;
                    break;
                }
                float x = getX() + dx;
                float y = getY() + dy;
                x = x < 0 ? 0 : x > parentWidth - getWidth() ? parentWidth - getWidth() : x;
                y = getY() < 0 ? 0 : getY() + getHeight() > parentHeight ? parentHeight - getHeight() : y;
                setX(x);
                setY(y);
                lastX = rawX;
                lastY = rawY;
                Log.i("aa", "isDrag=" + isDrag + "getX=" + getX() + ";getY=" + getY() + ";parentWidth=" + parentWidth);
                break;
            case MotionEvent.ACTION_UP:
                if (isNotDrag()) {
                    setPressed(false);
                    if (rawX >= parentWidth / 2) {
                        animate().setInterpolator(new DecelerateInterpolator())
                                .setDuration(500)
                                .xBy(parentWidth - getWidth() - getX())
                                .start();
                    } else {
                        ObjectAnimator oa = ObjectAnimator.ofFloat(this, "x", getX(), 0);
                        oa.setInterpolator(new DecelerateInterpolator());
                        oa.setDuration(500);
                        oa.start();
                    }
                }
                break;
        }
        return isNotDrag() || super.onTouchEvent(event);
    }

    private boolean isNotDrag() {
        return isDrag || (getX() != 0 && (getX() != parentWidth - getWidth()));
    }
}

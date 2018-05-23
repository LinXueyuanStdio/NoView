package com.example.yhao.floatwindow.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/5/23
 * @discription null
 * @usage null
 */
public abstract class BaseFooUI extends BaseFrameLayout {
    private float touchingX = -1.0f;
    private float touchingY = -1.0f;

    public BaseFooUI(Context context) {
        super(context);
    }

    public BaseFooUI(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public BaseFooUI(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            this.touchingX = -1.0f;
            this.touchingY = -1.0f;
        } else {
            this.touchingX = motionEvent.getX();
            this.touchingY = motionEvent.getY();
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public float getTouchingX() {
        return this.touchingX;
    }

    public float getTouchingY() {
        return this.touchingY;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return super.onTouchEvent(motionEvent);
    }

}

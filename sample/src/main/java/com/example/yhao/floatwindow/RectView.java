package com.example.yhao.floatwindow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/5/23
 * @discription null
 * @usage null
 */
public class RectView extends View {
    private WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, 2010, 24, -2);
    private Paint paint = null;
    private Rect rect = null;

    public RectView(Context context) {
        super(context);
    }

    public RectView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public RectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void refreshRect(Rect rect) {
        this.rect = rect;
        invalidate();
    }

    public void removeViewImmediate(WindowManager windowManager) {
        windowManager.removeViewImmediate(this);
    }

    public void addView(WindowManager windowManager, Rect rect) {
        windowManager.addView(this, this.layoutParams);
        this.rect = rect;
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.paint == null) {
            this.paint = new Paint();
            this.paint.setColor(Color.parseColor("#ff651df2"));//TODO config
            this.paint.setStrokeWidth((float) 4);//TODO config
            this.paint.setStyle(Paint.Style.STROKE);
        }
        if (this.rect != null) {
            if (this.rect.right > getWidth()) {
                this.rect.right = getWidth();
            }
            if (this.rect.bottom > getHeight()) {
                this.rect.bottom = getHeight();
            }
            canvas.drawRect(this.rect, this.paint);
        }
    }
}


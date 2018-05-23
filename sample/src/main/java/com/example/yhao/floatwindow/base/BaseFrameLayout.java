package com.example.yhao.floatwindow.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/5/23
 * @discription null
 * @usage null
 */
public abstract class BaseFrameLayout extends FrameLayout {
    private Paint paint;
    private Path path = new Path();

    public BaseFrameLayout(Context context) {
        super(context);
    }

    public BaseFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public BaseFrameLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }


    protected abstract void drawCanvas(Canvas canvas);

    protected abstract boolean isDayMode();

    protected abstract int getCurrentNightModeColor();


    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawCanvas(canvas);
        int currentNightModeColor = getCurrentNightModeColor();
        if (currentNightModeColor == 0) {
            return;
        }
        if (!isDayMode()) {
            canvas.drawColor(currentNightModeColor);
            return;
        }
        if (this.paint == null) {
            this.paint = new Paint();
            this.paint.setColor(currentNightModeColor);
        } else if (currentNightModeColor != this.paint.getColor()) {
            this.paint.setColor(currentNightModeColor);
        }
        drawPath(canvas, this.paint, this.path);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!isDayMode() && getCurrentNightModeColor() != 0) {
            this.path = getPath(this);
        }
    }

    public static void drawPath(Canvas canvas, Paint paint, Path path) {
        canvas.drawPath(path, paint);
    }

    public static Path getPath(ViewGroup viewGroup) {
        Path path = new Path();
        path.setFillType(Path.FillType.WINDING);
        addRect2Path(path, viewGroup);
        return path;
    }

    private static void addRect2Path(Path path, ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt.getVisibility() == View.VISIBLE && (childAt instanceof ViewGroup)) {
                if ("T4NM".equals(childAt.getTag())) {
                    Rect rect = new Rect();
                    childAt.getGlobalVisibleRect(rect);
                    rect.top += childAt.getPaddingTop();
                    rect.left += childAt.getPaddingLeft();
                    rect.right -= childAt.getPaddingRight();
                    rect.bottom -= childAt.getPaddingBottom();
                    path.addRect(new RectF(rect), Path.Direction.CW);
                } else {
                    addRect2Path(path, (ViewGroup) childAt);
                }
            }
        }
    }

}

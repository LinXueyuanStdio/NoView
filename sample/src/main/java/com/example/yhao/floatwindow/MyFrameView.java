package com.example.yhao.floatwindow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.yhao.fixedfloatwindow.R;
import com.example.yhao.floatwindow.base.BaseApplication;
import com.example.yhao.floatwindow.base.BaseFooUI;
import com.yhao.floatwindow.FloatWindow;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/5/23
 * @discription null
 * @usage null
 */
public class MyFrameView extends BaseFooUI {
    private int distHorizontial;
    private int distVertical;
    private Rect rect = null;
    private Paint paint;
    private RectView rectView;
    private ImageView adjust_mainui = null;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    public MyFrameView(Context context) {
        super(context);
    }

    public MyFrameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MyFrameView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initLayoutParams();
        initAdjustMainUiSize();
    }

    private void initLayoutParams() {
        //1111 1010 0101 0000 0100 0000 0000 0010 0000
        this.layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE, 17039392, -2);
        this.layoutParams.gravity = 51;
        this.windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
    }

    private void initAdjustMainUiSize() {
        this.adjust_mainui = (ImageView) findViewById(R.id.resize_icon);
        if (this.adjust_mainui != null) {
            this.adjust_mainui.setOnTouchListener(new OnTouchListener() {
                int a = -1;
                int b = -1;
                long currentTimeMillis = 0;
                boolean onResize = false;
                private int[] locationOnScreen = null;
                private int rawX;
                private int rawY;
                private int width;
                private int height;

                @Override
                public boolean onTouch(View v, MotionEvent motionEvent) {

                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                        int[] wndSizeLimit = getWndSizeLimit();
                        this.a = wndSizeLimit[0];
                        this.b = wndSizeLimit[1];
                        if (rectView == null) {
                            rectView = new RectView(BaseApplication.getInstant());
                            rect = new Rect();
                            locationOnScreen = new int[2];
                        }
                        getLocationOnScreen(locationOnScreen);
                        if (locationOnScreen[1] > 0) {
                            wndSizeLimit = locationOnScreen;
                            wndSizeLimit[1] = wndSizeLimit[1] - getStatusBarHeight(true);
                        }
                        this.width = getWidth();
                        this.height = getHeight();
                        rect.set(locationOnScreen[0], locationOnScreen[1],
                                locationOnScreen[0] + this.width,
                                locationOnScreen[1] + this.height);
                        this.rawX = (int) motionEvent.getRawX();
                        this.rawY = (int) motionEvent.getRawY();

                        rectView.refreshRect(rect);
                        rectView.setVisibility(View.VISIBLE);
                        rectView.addView(windowManager, rect);

                        this.currentTimeMillis = System.currentTimeMillis();
                        onResize = false;
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                        distHorizontial = ((int) motionEvent.getRawX()) - this.rawX;
                        distVertical = ((int) motionEvent.getRawY()) - this.rawY;
                        getLocationOnScreen(locationOnScreen);

                        // TODO 边界检测

                        rect.left = locationOnScreen[0];
                        rect.top = (int) (locationOnScreen[1] + distVertical - getStatusBarHeight());
                        rect.right = (int) (locationOnScreen[0] + width + distHorizontial);
                        rect.bottom = locationOnScreen[1] + height - getStatusBarHeight();

                        rectView.refreshRect(rect);
                        onResize = true;
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_CANCEL
                            || motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        rectView.removeViewImmediate(windowManager);
                        rectView.setVisibility(View.GONE);
                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            if (onResize) {
                                invalidate();
                                int x = rect.left;
                                int y = rect.top;
                                int width = rect.width();
                                int height = rect.height();
                                FloatWindow.get().resize(x, y, width, height);
//                            } else if (onFullScreenClickListener != null) {
                                //TODO 单击，有监听用监听
//                                onFullScreenClickListener.onClick(adjust_mainui);
                            } else if (System.currentTimeMillis() - this.currentTimeMillis < 500) {
                                //TODO 单击
                            }
                        }
                        this.a = 0;
                    }
                    return true;
                }
            });
        }
    }

    @Override
    protected void drawCanvas(Canvas canvas) {
        if (!isMatchParent()) {
            try {
                if (this.paint == null) {
                    this.paint = new Paint();
                    this.paint.setStrokeWidth((float) 4);
                    this.paint.setStyle(Paint.Style.STROKE);
                }
                if (setupColor()) {
                    this.paint.setColor(Color.parseColor("#ff0288d1"));
                } else {
                    this.paint.setColor(Color.parseColor("#ffd8d8d8"));
                }
                canvas.drawRect(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), this.paint);
            } catch (Exception e) {
            }
        }
    }

    @Override
    protected boolean isDayMode() {
        return false;
    }

    @Override
    protected int getCurrentNightModeColor() {
        return Color.parseColor("#2015a24d");
    }

    public boolean isMatchParent() {
        return this.layoutParams.width == WindowManager.LayoutParams.MATCH_PARENT
                && this.layoutParams.height == WindowManager.LayoutParams.MATCH_PARENT;
    }

    public boolean setupColor() {
        if (!isVisible()) {
            return false;
        }
        return (((WindowManager.LayoutParams) getLayoutParams()).flags & 8) == 0;
    }

    public boolean isVisible() {
        return getVisibility() == View.VISIBLE;
    }

    public int[] getWndSizeLimit() {
        Point b = getBottomRightPoint(false);
        int min = (((Math.min(b.y, b.x) * 2) / 3) * 9) / 16;
        return new int[]{(Math.min(b.y, b.x) * 2) / 3, min};
    }

    public Point getBottomRightPoint(boolean considerStatusBar) {
        Point point = new Point();
        point.x = (int) (getWidth() + getX());
        point.y = (int) (considerStatusBar
                        ? getHeight() + this.getY() - getStatusBarHeight()
                        : getHeight() + this.getY());
        return point;
    }

    /**
     * 获取状态栏的高度
     * @return 状态栏的高度
     */
    private int getStatusBarHeight() {
        Context context = BaseApplication.getInstant();
        // 反射手机运行的类：android.R.dimen.status_bar_height.
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            String heightStr = clazz.getField("status_bar_height").get(object).toString();
            int height = Integer.parseInt(heightStr);
            //dp--->px
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    public int getStatusBarHeight(boolean recompute) {
        return getStatusBarHeight();
    }
}

package org.yooran.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;

import org.yooran.demo.R;

/**
 * 自定义SlidingMenu
 * Created by ${YooranChen} on 15-1-5.
 */
public class SlidingMenu extends HorizontalScrollView {

    public SlidingMenu(Context context) {
        this(context, null);
    }


    /**
     * 当使用了自定义属性时，会调用此构造方法
     */
    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        把dp转化为px
        mMenuRightPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 80, context.getResources().getDisplayMetrics());
        //获取定义的自定义属性
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SlingdingMenu, defStyleAttr, 0);
        int indexCount = array.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.SlingdingMenu_rightPadding:
                    mMenuRightPadding = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 60, context.getResources().getDisplayMetrics()));//默认50
                    break;
            }
        }

        array.recycle();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;//获取屏幕宽度
    }

    /**
     * 未使用自定义属性是调用
     */
    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }


    private LinearLayout mWrapper;
    private ViewGroup mMenu;//菜单栏
    private ViewGroup mContent;//内容

    private int mScreenWidth;
    private int mMenuRightPadding = 50;//屏幕右侧边距>>单位DP
    private int mMenuWidth;
    private boolean once = false;
    private boolean isOpen = false;

    /**
     * 决定子view的宽和高，
     * 及自身的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWrapper = (LinearLayout) getChildAt(0);
        mMenu = (ViewGroup) mWrapper.getChildAt(0);
        mContent = (ViewGroup) mWrapper.getChildAt(1);
        if (!once) {
            once = true;
            mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth - mMenuRightPadding;
            mContent.getLayoutParams().width = mScreenWidth;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 决定子view放置的位置
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //将menu隐藏，将content显示在屏幕中间
        if (changed) {
            this.scrollTo(mMenuWidth, 0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            //手指抬起
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();//隐藏在左边的宽度
                if (scrollX >= mMenuWidth / 2) {
                    this.smoothScrollTo(mMenuWidth, 0);
                    isOpen = false;
                } else {
                    this.smoothScrollTo(0, 0);
                    isOpen = true;
                }
                return true;
        }


        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        //l->getScrollX();
        float scale = l * 1.0f / mMenuWidth;//1-0

        //内容区域1.0-0.7的缩放
        float rightScale = 0.7f + (0.3f * scale);
        float leftScale = 1.0f - scale * 0.3f;
        float leftAlpha = 1 - scale * 0.4f;
        //地用属性动画，设置translationX;
        ViewHelper.setTranslationX(mMenu, (float) (scale * mMenuWidth * 0.8));//
        ViewHelper.setScaleX(mMenu, leftScale);
        ViewHelper.setScaleY(mMenu, leftScale);
        ViewHelper.setAlpha(mMenu, leftAlpha);


        //设置content缩放的中心点
        ViewHelper.setPivotX(mContent, 0);
        ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
        ViewHelper.setScaleX(mContent, rightScale);
        ViewHelper.setScaleY(mContent, rightScale);
    }

    /**
     * 菜单控制
     */
    public void toggle() {
        if (isOpen) {
            this.smoothScrollTo(mMenuWidth, 0);
            isOpen = false;
        } else {
            this.smoothScrollTo(0, 0);
            isOpen = true;
        }
    }
}

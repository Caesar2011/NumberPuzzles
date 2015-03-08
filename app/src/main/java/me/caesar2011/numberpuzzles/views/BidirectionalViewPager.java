package me.caesar2011.numberpuzzles.views;

/**
 * Created by Schulä on 21.02.2015.
 */

import android.content.Context;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Uses a combination of a PageTransformer and onTouchEvent to create the
 * illusion of a vertically scrolling ViewPager.
 *
 * Requires API 11+
 *
 */
public class BidirectionalViewPager extends ViewPager {

    private final int DRAG_NONE = 0;
    private final int DRAG_RIGHT = 1;
    private final int DRAG_DOWN = 2;
    private final int DRAG_LEFT = 3;
    private final int DRAG_UP = 4;

    private final double DIST_TO_ACTIVATION = 0.075;

    private float startDragX;
    private float startDragY;
    private int dragDirection = this.DRAG_NONE;

    private float screenDPIx;
    private float screenDPIy;

    private int currViewPos = -1;
    private boolean currViewPosBlocked = false;

    private int positionX = 0;
    private int positionY = 0;
    private int positionPos = 0;

    private float scrollOffsetPix = 0;
    private int scrollOffsetPos = 0;

    private float lastValidX;
    private float lastValidY;

    public BidirectionalViewPager(Context context) {
        super(context);
        init();
    }

    public BidirectionalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private int getClientWidth() {
        return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int getClientHeight() {
        return getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
    }

    private BidirectionalViewPager getThis() {
        return this;
    }

    private void init() {
        // The majority of the magic happens here
        //setPageTransformer(true, new VerticalPageTransformer());
        // The easiest way to get rid of the overscroll drawing that happens on the left and right
        setOverScrollMode(OVER_SCROLL_NEVER);


        DisplayMetrics dm = new DisplayMetrics();
        WindowManager winManager = (WindowManager) (getContext().getSystemService(Context.WINDOW_SERVICE));
        winManager.getDefaultDisplay().getMetrics(dm);
        screenDPIx = dm.xdpi;
        screenDPIy = dm.ydpi;
        //screenSizeX = this.width
        //screenSizeY = dm.heightPixels;

        setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                //Log.v("::onPageScrolled", "i: "+i+" - v: "+v+" - i2: "+i2);
                scrollOffsetPix = i2;
                scrollOffsetPos = i;


                //BidirectionalFragment fragment = (BidirectionalFragment) getAdapter().instantiateItem(getThis(), i);
                //fragment.setPosition(x, y);
            }

            @Override
            public void onPageSelected(int i) {
                if (dragDirection==DRAG_RIGHT || dragDirection==DRAG_LEFT) {
                    positionX += i-currViewPos;
                } else if (dragDirection==DRAG_DOWN || dragDirection==DRAG_UP) {
                    positionY += i-currViewPos;
                }
                positionPos = i;
                setCurrentItem(positionX, positionY, true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (i==ViewPager.SCROLL_STATE_IDLE) {
                    dragDirection = DRAG_NONE;
                    currViewPosBlocked = false;
                } else if (i==ViewPager.SCROLL_STATE_SETTLING) {
                } else if (i==ViewPager.SCROLL_STATE_DRAGGING && !currViewPosBlocked) {
                    currViewPos = getCurrentItem();
                    currViewPosBlocked = true;
                }
            }
        });
    }

    @Override
    public BidirectionalPagerAdapter getAdapter() {
        return (BidirectionalPagerAdapter) super.getAdapter();
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        if (adapter instanceof BidirectionalPagerAdapter)
            super.setAdapter(adapter);
    }

    @Override
    public void setCurrentItem(int item) {
        setCurrentItem(item, true);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        setCurrentItem(item, 0, smoothScroll);
    }

    public void setCurrentItem(int posX, int posY, boolean smoothScroll) {
        positionX = Math.min(Math.max(0, posX), getAdapter().getCountX());
        positionY = Math.min(Math.max(0, posY), getAdapter().getCountY(positionX));
        super.setCurrentItem(positionX+positionY, smoothScroll);
        BidirectionalFragment fragment = (BidirectionalFragment) getAdapter().instantiateItem(this, positionX+positionY);
        fragment.setPosition(positionX, positionY);
    }

    private int getDirection(float distPixelX, float distPixelY) {
        float distInchX = distPixelX / screenDPIx;
        float distInchY = distPixelY / screenDPIy;
        if ((dragDirection == this.DRAG_NONE) && (Math.sqrt(Math.pow(distInchX, 2) + Math.pow(distInchY, 2)) > DIST_TO_ACTIVATION)) {
            if (Math.abs(distInchY) > Math.abs(distInchX)) {
                if (distInchY < 0)
                    return this.DRAG_DOWN;
                else
                    return this.DRAG_UP;
            } else {
                if (distInchX < 0)
                    return this.DRAG_RIGHT;
                else
                    return this.DRAG_LEFT;
            }
        }
        if ((dragDirection == this.DRAG_UP) || (dragDirection == this.DRAG_DOWN)) {
            if (distInchY < 0)
                return this.DRAG_DOWN;
            else
                return this.DRAG_UP;
        }
        if ((dragDirection == this.DRAG_RIGHT) || (dragDirection == this.DRAG_LEFT)) {
            if (distInchX < 0)
                return this.DRAG_RIGHT;
            else
                return this.DRAG_LEFT;
        }
        return this.DRAG_NONE;
    }

    public class VerticalPageTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(View view, float position) {
            long lPos = (long) position;
            float alpha = 0;
            if (lPos == 0) alpha = (float) (1-Math.pow(Math.abs(position), 2));
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(alpha);

            } else if (position <= 1) { // [-1,1]
                view.setAlpha(alpha);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                //set Y position to swipe in from top
                view.setTranslationY(position * pageHeight);
            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(alpha);
            }
        }
    }

    /**
     * Swaps the X and Y coordinates of your touch event
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //swap the x and y coords of the touch event

        if (ev.getAction()==MotionEvent.ACTION_DOWN) {
            startDragX = ev.getX();
            startDragY = ev.getY();
        } else if (ev.getAction()==MotionEvent.ACTION_MOVE) {
            // Measure distance and direction
            // Determine and apply PageTransformer

            int newDragDir = getDirection(ev.getX()-startDragX, ev.getY()-startDragY);
            if (dragDirection == this.DRAG_NONE) {
                if ((newDragDir == this.DRAG_UP) || (newDragDir == this.DRAG_DOWN)) {
                    MotionEvent ev2 = MotionEvent.obtain(ev);
                    ev2.setAction(MotionEvent.ACTION_UP);
                    super.onTouchEvent(ev2);
                    ev2.setAction(MotionEvent.ACTION_DOWN);
                    ev2.setLocation(startDragY, startDragX);
                    super.onInterceptTouchEvent(ev2);/**/
                    setPageTransformer(true, new VerticalPageTransformer());
                    super.onTouchEvent(ev2);
                    ev2.recycle();
                } else if ((newDragDir == this.DRAG_RIGHT) || (newDragDir == this.DRAG_LEFT)) {
                    setPageTransformer(true, null);
                }
            }
            dragDirection = newDragDir;


        }

        // No else! Value can get changed
        if (dragDirection != this.DRAG_NONE) {
            if ((dragDirection == this.DRAG_UP) && ((scrollOffsetPos-positionX) < 0)) {
                ev.setLocation(
                        Math.round(lastValidX),
                        Math.round(lastValidY)
                );
            } else if ((dragDirection == this.DRAG_LEFT) && ((scrollOffsetPos-positionY) < 0)) {
                ev.setLocation(
                        Math.round(lastValidX),
                        Math.round(lastValidY)
                );
            } else {
                lastValidX = ev.getX();
                lastValidY = ev.getY();
            }
            if ((dragDirection == this.DRAG_UP) || (dragDirection == this.DRAG_DOWN)) {
                ev.setLocation(
                        Math.round(ev.getY()),
                        Math.round(ev.getX())
                );
            }
            return super.onTouchEvent(ev);
        } else {
            return true;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
       boolean x = super.onInterceptTouchEvent(ev);
       return x;
    }
}

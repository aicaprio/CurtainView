/*
 * Copyright 2017 aicaprio
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.aicaprio.curtainview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;


public class CurtainView extends FrameLayout implements ICurtainViewBase {

    private CurtainGravity mCGravity = CurtainGravity.getDefault();


    private CurtainStatus mCStatus = CurtainStatus.getDefault();

    private ReboundMode mReboundMode = ReboundMode.getDefault();

    private Context mContext;

    private MarginLayoutParams mLayoutParams;

    private int mWidth, mHeight, miLeftM, miTopM, miRightM, miBottomM;

    private int mTotalValue, mFixedValue, mMaxFloatingValue;

    private int mTouchSlop, mBoundaryValue;

    private float mInitialMotionX, mInitialMotionY, mLastMotionX, mLastMotionY;

    private boolean afterSizeChanged, afterInitializeMargins;

    private boolean mIsPulling, mNoChildrenConsumed, mIsComputingScrollOffset;

    private Scroller mScroller;

    private OnPullingListener mOnPullingListener;

    private AutoScrollingListener mAutoScrollingListener;

    private int mScrollDuration = DEFAULT_SCROLL_DURATION;

    public CurtainView(Context context) {
        super(context);
        init(context);
    }

    public CurtainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CurtainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context) {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mTouchSlop = viewConfiguration.getScaledTouchSlop();
        mScroller = new Scroller(context);
        mContext = context;
    }

    private void init(Context context, AttributeSet attrs) {
        init(context);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CurtainView);

        mCGravity = CurtainGravity.mapIntToValue(typedArray.getInt(R.styleable
                .CurtainView_curtainGravity, 0));

        mCStatus = CurtainStatus.mapIntToValue(typedArray.getInt(R.styleable
                .CurtainView_curtainStatus, 0));

        mReboundMode = ReboundMode.mapIntToValue(typedArray.getInt(R.styleable
                .CurtainView_reboundMode, 0));

        mScrollDuration = typedArray.getInt(R.styleable.CurtainView_scrollDuration,
                DEFAULT_SCROLL_DURATION);

        mFixedValue = (int) typedArray.getDimension(R.styleable.CurtainView_fixedValue, 0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        afterSizeChanged = true;
        refreshParams();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void refreshParams() {
        if (!afterSizeChanged) {
            return;
        }
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params instanceof MarginLayoutParams) {
            mLayoutParams = (MarginLayoutParams) params;

            initMargins();

            refreshWidthAndHeight();

            refreshFixedAndFloatingValue();

            refreshBoundaryValue();

            refreshUIByStatus();
        }
    }

    private void initMargins() {
        if (!afterInitializeMargins) {
            miLeftM = mLayoutParams.leftMargin;
            miTopM = mLayoutParams.topMargin;
            miRightM = mLayoutParams.rightMargin;
            miBottomM = mLayoutParams.bottomMargin;

            miLeftM = miLeftM < 0 ? 0 : miLeftM;
            miRightM = miRightM < 0 ? 0 : miRightM;
            miTopM = miTopM < 0 ? 0 : miTopM;
            miBottomM = miBottomM < 0 ? 0 : miBottomM;

            afterInitializeMargins = true;
        }
    }

    private void refreshWidthAndHeight() {
        mWidth = getWidth();
        mHeight = getHeight();

        if (mLayoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            mLayoutParams.width = mWidth;
            setLayoutParams(mLayoutParams);
        }

        if (mLayoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
            mLayoutParams.height = mHeight;
            setLayoutParams(mLayoutParams);
        }
    }

    private void refreshFixedAndFloatingValue() {
        if (!afterSizeChanged) {
            return;
        }
        switch (mCGravity) {
            case LEFT:
            case RIGHT:
                mTotalValue = mWidth;
                break;
            case TOP:
            case BOTTOM:
                mTotalValue = mHeight;
                break;
        }
        if (mFixedValue == 0 || mFixedValue >= mTotalValue) {
            mFixedValue = (int) (mTotalValue * FIXED_RATE);
        }
        mMaxFloatingValue = mTotalValue - mFixedValue;
    }

    private void refreshBoundaryValue() {
        if (!afterSizeChanged) {
            return;
        }
        switch (mReboundMode) {
            case ALWAYS_BACK:
                mBoundaryValue = Integer.MAX_VALUE;
                break;
            case HALF:
                mBoundaryValue = (int) (mMaxFloatingValue / 2f);
                break;
        }
    }

    private void refreshUIByStatus() {
        if (!afterSizeChanged) {
            return;
        }
        if (mCStatus == CurtainStatus.OPENED) {
            setToBeOpened();
        } else {
            setToBeClosed();
        }
    }

    public void toggleStatus() {
        if (mIsPulling || mIsComputingScrollOffset) {
            return;
        }

        mIsComputingScrollOffset = true;

        boolean cFlag = (mCGravity == CurtainGravity.LEFT || mCGravity == CurtainGravity.TOP);
        if (mCStatus == CurtainStatus.OPENED) {
            int distance = cFlag ? -mMaxFloatingValue : mMaxFloatingValue;
            makeScroll(0, distance);
        } else {
            makeScroll(mFixedValue, mMaxFloatingValue);
        }
    }

    private void refreshStatus() {
        switch (mCGravity) {
            case LEFT:
                mCStatus = getStatus(mLayoutParams.leftMargin, miLeftM);
                break;
            case TOP:
                mCStatus = getStatus(mLayoutParams.topMargin, miTopM);
                break;
            case RIGHT:
                mCStatus = getStatus(mLayoutParams.rightMargin, miRightM);
                break;
            case BOTTOM:
                mCStatus = getStatus(mLayoutParams.bottomMargin, miBottomM);
                break;
        }
    }

    private CurtainStatus getStatus(int currMargin, int initMargin) {
        return currMargin == initMargin ? CurtainStatus.OPENED : CurtainStatus.CLOSED;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!permitsPull()) {
            return false;
        }

        final int action = event.getAction();

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            resetEventControllers();
            return false;
        }

        if (mIsPulling && action != MotionEvent.ACTION_DOWN) {
            return true;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mLastMotionY = mInitialMotionY = event.getRawY();
                mLastMotionX = mInitialMotionX = event.getRawX();
                mIsPulling = false;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                float y = event.getRawY();
                float x = event.getRawX();

                if (inspectMovement(x, y)) {
                    mLastMotionY = y;
                    mLastMotionX = x;
                    mIsPulling = true;
                }
                break;
            }
        }

        return mIsPulling;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsComputingScrollOffset || !permitsPull()) {
            return false;
        }

        final int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN && event.getEdgeFlags() != 0) {
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mLastMotionY = mInitialMotionY = event.getRawY();
                mLastMotionX = mInitialMotionX = event.getRawX();
                mNoChildrenConsumed = true;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (mIsPulling) {
                    mLastMotionY = event.getRawY();
                    mLastMotionX = event.getRawX();
                    pullEvent();
                } else {
                    if (mNoChildrenConsumed) {
                        float y = event.getRawY();
                        float x = event.getRawX();

                        if (inspectMovement(x, y)) {
                            mLastMotionY = y;
                            mLastMotionX = x;
                            mIsPulling = true;
                        }
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (mIsPulling) {
                    actionUpEvent();
                    return true;
                } else {
                    resetEventControllers();
                }
                break;
            }
        }
        return true;
    }

    private void resetEventControllers() {
        mIsPulling = mNoChildrenConsumed = false;
    }

    private void resetAllControllers() {
        mIsComputingScrollOffset = false;
        resetEventControllers();
    }


    /**
     * @param x MotionX
     * @param y MotionY
     * @return inspect whether the movement is a qualified pull event
     */
    private boolean inspectMovement(float x, float y) {
        float[] motionDifferences = getMotionDifferences(mInitialMotionX, mInitialMotionY, x, y);
        float absDiff = Math.abs(motionDifferences[0]);
        float absOppositeDiff = Math.abs(motionDifferences[1]);

        return absDiff > mTouchSlop && absDiff > absOppositeDiff;
    }

    private float[] getMotionDifferences(float startX, float startY, float currX, float currY) {
        float diff, oppositeDiff, startValue, currValue;
        float[] differences = new float[4];

        switch (mCGravity) {
            case LEFT:
            case RIGHT:
            default:
                diff = currX - startX;
                oppositeDiff = currY - startY;
                startValue = startX;
                currValue = currX;
                break;
            case TOP:
            case BOTTOM:
                diff = currY - startY;
                oppositeDiff = currX - startX;
                startValue = startY;
                currValue = currY;
                break;
        }

        differences[0] = diff;
        differences[1] = oppositeDiff;
        differences[2] = startValue;
        differences[3] = currValue;
        return differences;
    }

    private void pullEvent() {
        updateMargins();
        refreshSelf();
    }

    private void refreshSelf() {
        postInvalidate();
        requestLayout();
    }

    private void actionUpEvent() {
        mIsComputingScrollOffset = true;

        int diff = updateMargins();
        int startValue, distance, mixedDiff;
        boolean cFlag = (mCGravity == CurtainGravity.LEFT || mCGravity == CurtainGravity.TOP);

        if (mCStatus == CurtainStatus.OPENED) {
            if (Math.abs(diff) <= mBoundaryValue) {
                distance = -diff;
            } else {
                distance = cFlag ? -mMaxFloatingValue - diff : mMaxFloatingValue - diff;
            }
            makeScroll(diff, distance);
        } else {
            mixedDiff = cFlag ? -diff : diff;
            startValue = mFixedValue - mixedDiff;
            distance = Math.abs(diff) <= mBoundaryValue ? mixedDiff : mMaxFloatingValue + mixedDiff;
            makeScroll(startValue, distance);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int changedValue = computeChangedValue();
            setLayoutMargins(changedValue);

            if (mScroller.isFinished()) {
                refreshStatus();
                resetAllControllers();
                scrollingListenerOnScrollFinished();
            } else {
                int[] values = getScrollerValues();
                scrollingListenerOnScrolling(values[0], values[1], values[2], values[3]);
            }
        }
        refreshSelf();
    }

    private int updateMargins() {
        float[] motionDifferences = getMotionDifferences(mInitialMotionX, mInitialMotionY,
                mLastMotionX, mLastMotionY);
        int diff = (int) motionDifferences[0];
        int rawStart = (int) motionDifferences[2];

        int mixedDiff, changedValue;
        boolean cFlag = (mCGravity == CurtainGravity.LEFT || mCGravity == CurtainGravity.TOP);

        if (mCStatus == CurtainStatus.OPENED) {
            mixedDiff = cFlag ? -diff : diff;
            changedValue = -mixedDiff;
        } else {
            mixedDiff = cFlag ? diff : -diff;
            changedValue = mixedDiff - mMaxFloatingValue;
        }

        if (mixedDiff <= 0) {
            if (mCStatus == CurtainStatus.OPENED) {
                setToBeOpened();
            } else {
                setToBeClosed();
            }
            diff = 0;
            pullingListenerOnPulling(rawStart, diff);
            return diff;
        }
        if (mixedDiff >= mMaxFloatingValue) {
            if (mCStatus == CurtainStatus.OPENED) {
                setToBeClosed();
            } else {
                setToBeOpened();
            }
            diff = (diff == mixedDiff ? mMaxFloatingValue : -mMaxFloatingValue);
            pullingListenerOnPulling(rawStart, diff);
            return diff;

        }
        setLayoutMargins(changedValue);
        pullingListenerOnPulling(rawStart, diff);
        return diff;
    }

    private int computeChangedValue() {
        int changedValue = 0;
        if (mCStatus == CurtainStatus.OPENED) {
            switch (mCGravity) {
                case LEFT:
                    changedValue = mScroller.getCurrX();
                    break;
                case TOP:
                    changedValue = mScroller.getCurrY();
                    break;
                case RIGHT:
                    changedValue = -mScroller.getCurrX();
                    break;
                case BOTTOM:
                    changedValue = -mScroller.getCurrY();
                    break;
            }
        } else {
            if (mCGravity == CurtainGravity.LEFT || mCGravity == CurtainGravity.RIGHT) {
                changedValue = mScroller.getCurrX() - mTotalValue;
            } else {
                changedValue = mScroller.getCurrY() - mTotalValue;
            }
        }
        return changedValue;
    }

    private void makeScroll(int startValue, int distance) {
        if (Math.abs(distance) == 0) {
            if (mReboundMode != ReboundMode.ALWAYS_BACK) {
                refreshStatus();
            }
            resetAllControllers();
            scrollingListenerOnScrollFinished();
            return;
        }
        switch (mCGravity) {
            case LEFT:
            case RIGHT:
                mScroller.startScroll(startValue, 0, distance, 0, mScrollDuration);
                break;
            case TOP:
            case BOTTOM:
                mScroller.startScroll(0, startValue, 0, distance, mScrollDuration);
                break;
        }
    }

    private int[] getScrollerValues() {
        int[] scrollerValues = new int[4];
        int startValue, currValue, finalValue, currVelocity;

        switch (mCGravity) {
            case LEFT:
            case RIGHT:
            default:
                startValue = mScroller.getStartX();
                currValue = mScroller.getCurrX();
                finalValue = mScroller.getFinalX();
                break;
            case TOP:
            case BOTTOM:
                startValue = mScroller.getStartY();
                currValue = mScroller.getCurrY();
                finalValue = mScroller.getFinalY();
                break;
        }
        currVelocity = (int) mScroller.getCurrVelocity();

        scrollerValues[0] = currValue;
        scrollerValues[1] = currVelocity;
        scrollerValues[2] = startValue;
        scrollerValues[3] = finalValue;

        return scrollerValues;
    }

    private void setLayoutMargins(int left, int top, int right, int bottom) {
        mLayoutParams.setMargins(left, top, right, bottom);
        setLayoutParams(mLayoutParams);
    }

    private void setLayoutMargins(int changedValue) {
        int left = miLeftM, top = miTopM, right = miRightM, bottom = miBottomM;
        switch (mCGravity) {
            case LEFT:
                left += changedValue;
                break;
            case TOP:
                top += changedValue;
                break;
            case RIGHT:
                right += changedValue;
                break;
            case BOTTOM:
                bottom += changedValue;
                break;
        }
        setLayoutMargins(left, top, right, bottom);
    }

    private void setToBeOpened() {
        setLayoutMargins(miLeftM, miTopM, miRightM, miBottomM);
    }

    private void setToBeClosed() {
        setLayoutMargins(-mMaxFloatingValue);
    }

    private void pullingListenerOnPulling(int rawStart, int diff) {
        if (mOnPullingListener != null) {
            mOnPullingListener.onPulling(rawStart, diff, mCGravity, mCStatus);
        }
    }

    private void scrollingListenerOnScrolling(int currValue, int currVelocity, int startValue,
                                              int finalValue) {
        if (mAutoScrollingListener != null) {
            mAutoScrollingListener.onScrolling(currValue, currVelocity, startValue, finalValue);
        }
    }

    private void scrollingListenerOnScrollFinished() {
        if (mAutoScrollingListener != null) {
            mAutoScrollingListener.onScrollFinished();
        }
    }

    @Override
    public CurtainGravity getCurtainGravity() {
        return mCGravity;
    }

    @Override
    public CurtainStatus getCurtainStatus() {
        return mCStatus;
    }

    @Override
    public ReboundMode getReboundMode() {
        return mReboundMode;
    }

    @Override
    public int getFixedValue() {
        return mFixedValue;
    }

    @Override
    public int getMaxFloatingValue() {
        return mMaxFloatingValue;
    }

    @Override
    public int getTotalValue() {
        return mTotalValue;
    }

    @Override
    public int getScrollDuration() {
        return mScrollDuration;
    }

    @Override
    public boolean permitsPull() {
        return mLayoutParams != null && (mWidth + mHeight) > 0;
    }

    @Override
    public boolean isPulling() {
        return mIsPulling;
    }

    @Override
    public boolean isAutoScrolling() {
        return mIsComputingScrollOffset;
    }

    @Override
    public void setCurtainGravityAndFixedValue(CurtainGravity curtainGravity, int fixedValue) {
        if (curtainGravity != null) {
            mCGravity = curtainGravity;
        }
        mFixedValue = fixedValue < 0 ? 0 : fixedValue;
        refreshFixedAndFloatingValue();
        refreshUIByStatus();
    }

    @Override
    public void setCurtainStatus(CurtainStatus curtainStatus) {
        if (curtainStatus == null || mCStatus == curtainStatus) {
            return;
        }
        mCStatus = curtainStatus;
        refreshUIByStatus();
    }

    @Override
    public void setReboundMode(ReboundMode reboundMode) {
        if (reboundMode == null || mReboundMode == reboundMode) {
            return;
        }
        mReboundMode = reboundMode;
        refreshBoundaryValue();
    }

    @Override
    public void setScrollDuration(int scrollDuration) {
        if (scrollDuration > 0) {
            mScrollDuration = scrollDuration;
        }
    }

    @Override
    public void setScrollerInterpolator(Interpolator interpolator) {
        if (interpolator != null) {
            mScroller = new Scroller(mContext, interpolator);
        }
    }

    @Override
    public void setOnPullingListener(OnPullingListener onPullingListener) {
        mOnPullingListener = onPullingListener;
    }

    @Override
    public void setAutoScrollingListener(AutoScrollingListener autoScrollingListener) {
        mAutoScrollingListener = autoScrollingListener;
    }
}

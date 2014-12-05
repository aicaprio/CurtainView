package com.movitech.aicaprio;

import android.view.animation.Interpolator;


public interface ICurtainViewBase {

    public static final int DEFAULT_SCROLL_DURATION = 1000;

    public static final float FIXED_RATE = (float) 1 / 3;

    /**
     * @return CurtainGravity describe the side of a CurtainView that to be fixed,
     * which the optimize side is to be moved.
     * For example, if you set CurtainGravity to be LEFT,that means the left side of the
     * CurtainView is fixed,and you can move the CurtainView horizontally within a scope from the
     * right side.
     * Be sure to distinguish with the attribute:"android:layout_gravity".
     */
    public CurtainGravity getCurtainGravity();

    /**
     * @return The Status of CurtainView,can be OPENED or CLOSED
     */
    public CurtainStatus getCurtainStatus();

    /**
     * @return The ReboundMode of CurtainView,which determines where the CurtainView move to when
     * you lift your finger after certain movement.
     */
    public ReboundMode getReboundMode();

    /**
     * @return The minimum width or height that the CurtainView appears on
     * screen.
     */
    public int getFixedValue();

    /**
     * @return The ceiling movement of a CurtainView.
     */
    public int getMaxFloatingValue();

    /**
     * @return Can be the width or height of the CurtainView,depends on current CurtainGravity.
     * And : fixedValue + maxFloatingValue = totalValue.
     */
    public int getTotalValue();

    /**
     * @return The scroll duration that takes the CurtainView moving to destination when you lift
     * your finger.
     */
    public int getScrollDuration();

    /**
     * @return Whether the CurtainView is enabled to be pulled.
     */
    public boolean permitsPull();

    public boolean isPulling();

    public boolean isAutoScrolling();

    /**
     * Note that:
     * The "fixedValue" can be meaningful when the "curtainGravity" is sure.
     * Most of times,we need to change the other one when either of the  two parameters has changed.
     * For easily operating,two setter methods combine to be one.
     *
     * @param curtainGravity If there is no need to change the CurtainGravity,put null instead.
     * @param fixedValue     If fixedValue <=0,a default value (see also:FIXED_RATE) will
     *                       be set.
     */
    public void setCurtainGravityAndFixedValue(CurtainGravity curtainGravity, int fixedValue);

    public void setCurtainStatus(CurtainStatus curtainStatus);

    public void setReboundMode(ReboundMode reboundMode);

    public void setScrollDuration(int scrollDuration);

    /**
     * Note that:
     * Bug occurs wen using some interpolator(eg: OvershootInterpolator) in follow situation:
     * The fixedValue is small and the whole CurtainView has scrolled out of screen during
     * scrolling,then the "computeScroll()" has no callbacks which leads CurtainView disappeared.
     *
     * @param interpolator Interpolator for the Scroller which process the movement after you
     *                     lift your finger off the CurtainView.
     */
    public void setScrollerInterpolator(Interpolator interpolator);

    public void setOnPullingListener(OnPullingListener onPullingListener);

    public void setAutoScrollingListener(AutoScrollingListener autoScrollingListener);

    public static enum CurtainGravity {

        LEFT(0x0),

        TOP(0x1),

        RIGHT(0x2),

        BOTTOM(0x3);

        private int mIntValue;

        CurtainGravity(int i) {
            mIntValue = i;
        }

        public static CurtainGravity mapIntToValue(int i) {
            for (CurtainGravity curtainGravity : CurtainGravity.values()) {
                if (i == curtainGravity.getIntValue()) {
                    return curtainGravity;
                }
            }
            return getDefault();
        }

        private int getIntValue() {
            return mIntValue;
        }

        public static CurtainGravity getDefault() {
            return LEFT;
        }

    }

    public static enum CurtainStatus {

        OPENED(0x0),

        CLOSED(0x1);

        private int mIntValue;

        CurtainStatus(int i) {
            mIntValue = i;
        }

        public static CurtainStatus mapIntToValue(int i) {
            for (CurtainStatus curtainStatus : CurtainStatus.values()) {
                if (i == curtainStatus.getIntValue()) {
                    return curtainStatus;
                }
            }
            return getDefault();
        }

        private int getIntValue() {
            return mIntValue;
        }

        public static CurtainStatus getDefault() {
            return OPENED;
        }
    }

    public static enum ReboundMode {

        ALWAYS_BACK(0x0),

        HALF(0x1);

        private int mIntValue;

        ReboundMode(int i) {
            mIntValue = i;
        }

        public static ReboundMode mapIntToValue(int i) {
            for (ReboundMode reboundMode : ReboundMode.values()) {
                if (i == reboundMode.getIntValue()) {
                    return reboundMode;
                }
            }
            return getDefault();
        }

        private int getIntValue() {
            return mIntValue;
        }

        public static ReboundMode getDefault() {
            return ALWAYS_BACK;
        }
    }

    /**
     * A monitor with the pulling event
     */
    public interface OnPullingListener {
        public void onPulling(int rawStart, int diff, CurtainGravity cGravity,
                              CurtainStatus cStatus);
    }

    /**
     * When the finger lift off the CurtainView,An auto scrolling will happens.
     * See also:ReboundMode
     */
    public interface AutoScrollingListener {
        public void onScrolling(int currValue, int currVelocity, int startValue, int finalValue);

        public void onScrollFinished();
    }
}

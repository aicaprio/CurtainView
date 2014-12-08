package com.aicaprio.curtainview;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.movitech.aicaprio.CurtainView;
import com.movitech.aicaprio.ICurtainViewBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SampleActivity extends Activity {
    private CurtainView mCurtainView;
    private Button btnOpened, btnClosed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showSample1();
//        showSample2();
    }

    private void showSample1() {
        setContentView(R.layout.activity_sample);
        mCurtainView = (CurtainView) findViewById(R.id.cv1);
        final Button btn1 = (Button) findViewById(R.id.btn1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurtainView.isAutoScrolling()) {
                    showToast();
                } else {
                    mCurtainView.toggleStatus();
                }
            }
        });

        mCurtainView.setAutoScrollingListener(new ICurtainViewBase.AutoScrollingListener() {
            @Override
            public void onScrolling(int currValue, int currVelocity, int startValue,
                                    int finalValue) {
            }

            @Override
            public void onScrollFinished() {
                boolean isOpened = mCurtainView.getCurtainStatus() == ICurtainViewBase.CurtainStatus
                        .OPENED;
                Button checkedBtn = mCurtainView.getCurtainStatus() == ICurtainViewBase
                        .CurtainStatus.OPENED ? btnOpened : btnClosed;
                setChecked(checkedBtn);
            }
        });

        findViewById(R.id.btnCGravityLeft).setOnClickListener(mOnGravityChangedListener);
        findViewById(R.id.btnCGravityTop).setOnClickListener(mOnGravityChangedListener);
        findViewById(R.id.btnCGravityRight).setOnClickListener(mOnGravityChangedListener);
        findViewById(R.id.btnCGravityBottom).setOnClickListener(mOnGravityChangedListener);
        findViewById(R.id.btnAlwaysBack).setOnClickListener(mOnReboundModeChangedListener);
        findViewById(R.id.btnHalf).setOnClickListener(mOnReboundModeChangedListener);

        btnOpened = (Button) findViewById(R.id.btnOpened);
        btnClosed = (Button) findViewById(R.id.btnClosed);
    }

    private void setChecked(final View view) {
        ViewGroup parent = (ViewGroup) view.getParent();

        for (int i = 0; i < parent.getChildCount(); i++) {
            View currView = parent.getChildAt(i);
            if (currView.getId() == view.getId()) {
                currView.setBackgroundResource(R.color.color1);
            } else {
                currView.setBackgroundResource(R.color.color2);
            }
        }
    }

    private void setLayoutGravity(int layoutGravity) {
        /**
         *  CurtainView can not just used in FrameLayout,but also in LinearLayout or
         *  RelativeLayout whose LayoutParams is subclass of MarginLayoutParams.
         *  When CurtainGravity is set to be RIGHT or BOTTOM ,be sure that the CurtainView is
         *  actually RIGHT or BOTTOM to it's parent.
         *  eg:when you use CurtainView under RelativeLayout and the CurtainGravity is BOTTOM,
         *  you should set the attribute: android:layout_alignParentBottom="true"
         * In this case, CurtainView's parent is FrameLayout,we do like this when we change
         * the CurtainGravity.
         */
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mCurtainView
                .getLayoutParams();
        layoutParams.gravity = layoutGravity;
        mCurtainView.setLayoutParams(layoutParams);
    }

    private View.OnClickListener mOnGravityChangedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCurtainView.isAutoScrolling()) {
                showToast();
            } else {
                ICurtainViewBase.CurtainGravity cGravity;
                switch (v.getId()) {
                    default:
                    case R.id.btnCGravityLeft:
                        cGravity = ICurtainViewBase.CurtainGravity.LEFT;
                        setLayoutGravity(Gravity.LEFT);
                        break;
                    case R.id.btnCGravityTop:
                        cGravity = ICurtainViewBase.CurtainGravity.TOP;
                        setLayoutGravity(Gravity.TOP);
                        break;
                    case R.id.btnCGravityRight:
                        cGravity = ICurtainViewBase.CurtainGravity.RIGHT;
                        setLayoutGravity(Gravity.RIGHT);
                        break;
                    case R.id.btnCGravityBottom:
                        cGravity = ICurtainViewBase.CurtainGravity.BOTTOM;
                        setLayoutGravity(Gravity.BOTTOM);
                        break;
                }
                mCurtainView.setCurtainGravityAndFixedValue(cGravity, 0);
                setChecked(v);
            }
        }
    };

    private View.OnClickListener mOnReboundModeChangedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCurtainView.isAutoScrolling()) {
                showToast();
            } else {
                ICurtainViewBase.ReboundMode reboundMode;
                switch (v.getId()) {
                    default:
                    case R.id.btnAlwaysBack:
                        reboundMode = ICurtainViewBase.ReboundMode.ALWAYS_BACK;
                        break;
                    case R.id.btnHalf:
                        reboundMode = ICurtainViewBase.ReboundMode.HALF;
                        break;
                }
                mCurtainView.setReboundMode(reboundMode);
                setChecked(v);
            }
        }
    };

    private void showSample2() {
        setContentView(R.layout.activity_sample2);
        final CurtainView curtainView = (CurtainView) findViewById(R.id.cv1);
        final ImageView iv1 = (ImageView) findViewById(R.id.iv1);
        final GridView gv1 = (GridView) findViewById(R.id.gv1);

        final List<Map<String, Integer>> data = getData();
        gv1.setAdapter(new SimpleAdapter(this, data, R.layout.item_numbers,
                new String[]{TEXT_TAG}, new int[]{R.id.item_tvNum}));
        gv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showToast(data.get(position).get(TEXT_TAG) + " clicked");
            }
        });

        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curtainView.toggleStatus();
            }
        });

        curtainView.setScrollerInterpolator(new BounceInterpolator());
        curtainView.post(new Runnable() {
            @Override
            public void run() {
                curtainView.setCurtainGravityAndFixedValue(null, iv1.getHeight());
            }
        });
    }

    private List<Map<String, Integer>> getData() {
        ArrayList<Map<String, Integer>> data = new ArrayList<Map<String, Integer>>();
        int length = 8;
        for (int i = 0; i < length; i++) {
            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put(TEXT_TAG, i + 1);
            data.add(map);
        }
        return data;
    }

    public static final String TEXT_TAG = "num";

    private void showToast(CharSequence cs) {
        Toast.makeText(this, cs, Toast.LENGTH_SHORT).show();
    }

    private void showToast() {
        showToast("Scrolling not end");
    }

}

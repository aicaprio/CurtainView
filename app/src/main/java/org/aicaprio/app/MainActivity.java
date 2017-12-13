package org.aicaprio.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.aicaprio.curtainview.CurtainView;
import org.aicaprio.curtainview.ICurtainViewBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private CurtainView mCurtainView;
    private RadioGroup rgCGravity, rgReboundMode;
    private RadioButton rbOpened, rbClosed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showSample1();
        // showSample2();
    }

    private void showSample1() {
        setContentView(R.layout.activity_sample);
        mCurtainView = (CurtainView) findViewById(R.id.cb1);
        final Button btn1 = (Button) findViewById(R.id.btn1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurtainView.isAutoScrolling()) {
                    showToast("Scrolling is not end...");
                } else {
                    mCurtainView.toggleStatus();
                }
            }
        });

        mCurtainView
                .setAutoScrollingListener(new ICurtainViewBase.AutoScrollingListener() {
                    @Override
                    public void onScrolling(int currValue, int currVelocity,
                                            int startValue, int finalValue) {
                    }

                    @Override
                    public void onScrollFinished() {
                        boolean isOpened = mCurtainView.getCurtainStatus() == ICurtainViewBase.CurtainStatus.OPENED;
                        rbOpened.setChecked(isOpened);
                        rbClosed.setChecked(!isOpened);
                    }
                });

        rgCGravity = (RadioGroup) findViewById(R.id.rgCurtainGravity);
        rgReboundMode = (RadioGroup) findViewById(R.id.rgReboundMode);

        rgCGravity.setOnCheckedChangeListener(mOnCheckedChangeListener);
        rgReboundMode.setOnCheckedChangeListener(mOnCheckedChangeListener);

        rbOpened = (RadioButton) findViewById(R.id.rbOpened);
        rbClosed = (RadioButton) findViewById(R.id.rbClosed);

    }

    private void setLayoutGravity(int layoutGravity) {
        /**
         * CurtainView can not just used in FrameLayout,but also in LinearLayout
         * or RelativeLayout whose LayoutParams is subclass of
         * MarginLayoutParams. When CurtainGravity is set to be RIGHT or BOTTOM
         * ,be sure that the CurtainView is actually RIGHT or BOTTOM to it's
         * parent. eg:when you use CurtainView under RelativeLayout and the
         * CurtainGravity is BOTTOM, you should set the attribute:
         * android:layout_alignParentBottom="true" In this case, CurtainView's
         * parent is FrameLayout,we do like this when we change the
         * CurtainGravity.
         */
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mCurtainView
                .getLayoutParams();
        layoutParams.gravity = layoutGravity;
        mCurtainView.setLayoutParams(layoutParams);
    }

    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            ICurtainViewBase.CurtainGravity cGravity;
            if (group == rgCGravity) {
                switch (checkedId) {
                    default:
                    case R.id.rbCGravityLeft:
                        cGravity = ICurtainViewBase.CurtainGravity.LEFT;
                        setLayoutGravity(Gravity.LEFT);
                        break;
                    case R.id.rbCGravityTop:
                        cGravity = ICurtainViewBase.CurtainGravity.TOP;
                        setLayoutGravity(Gravity.TOP);
                        break;
                    case R.id.rbCGravityRight:
                        cGravity = ICurtainViewBase.CurtainGravity.RIGHT;
                        setLayoutGravity(Gravity.RIGHT);
                        break;
                    case R.id.rbCGravityBottom:
                        cGravity = ICurtainViewBase.CurtainGravity.BOTTOM;
                        setLayoutGravity(Gravity.BOTTOM);
                        break;
                }
                mCurtainView.setCurtainGravityAndFixedValue(cGravity, 0);
            } else if (group == rgReboundMode) {
                ICurtainViewBase.ReboundMode reboundMode;
                switch (checkedId) {
                    default:
                    case R.id.rbAlwaysBack:
                        reboundMode = ICurtainViewBase.ReboundMode.ALWAYS_BACK;
                        break;
                    case R.id.rbHalf:
                        reboundMode = ICurtainViewBase.ReboundMode.HALF;
                        break;
                }
                mCurtainView.setReboundMode(reboundMode);
            }
        }
    };

    private void showSample2() {
        setContentView(R.layout.activity_sample2);
        final CurtainView curtainView = (CurtainView) findViewById(R.id.cb1);
        final ImageView iv1 = (ImageView) findViewById(R.id.iv1);
        final GridView gv1 = (GridView) findViewById(R.id.gv1);

        final List<Map<String, Integer>> data = getData();
        gv1.setAdapter(new SimpleAdapter(this, data, R.layout.item_numbers,
                new String[] { TEXT_TAG }, new int[] { R.id.item_tvNum }));
        gv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
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
                curtainView.setCurtainGravityAndFixedValue(null,
                        iv1.getHeight());
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

}

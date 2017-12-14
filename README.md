CurtainView
===========

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-CurtainView-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1244)

The CurtainView is much like a layer on the top level of your layout. It's also a container which can wrap other Views.

And it will not block the actions of the child view unless you make a standard pull event.

The MotionEvent handling is base on chrisbanes' [library](https://github.com/chrisbanes/Android-PullToRefresh).

![image](https://github.com/aicaprio/CurtainView/blob/master/imgs/ezgif-5-4fc93a6397.gif)   

![image](https://github.com/aicaprio/CurtainView/blob/master/imgs/ezgif-5-f6189eb790.gif)

How to Use
===========
Some simple configs:

    <com.movitech.aicaprio.CurtainView
        xmlns:curtainView="http://schemas.android.com/apk/res-auto"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_gravity="top"
        curtainView:curtainGravity="top"
        curtainView:curtainStatus="closed"
        curtainView:reboundMode="half"
        curtainView:scrollDuration="1300" >

        <ImageView
            android:id="@id/iv1"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_gravity="bottom"
            android:src="@drawable/ic_pull" />
    </com.movitech.aicaprio.CurtainView>
    
"curtainView:curtainGravity": The visual gravity of CurtainView. 

"curtainView:curtainStatus": Whether the CurtainView is opend or closed.

"curtainView:reboundMode": After lift the finger off the CurtainView,how will it scroll to.

"curtainView:scrollDuration:": The scrolling duration after lift the finger off CurtainView.
    
There is one noteworthy attribute:"curtainView:fixedValue",which determines the minimum hegiht or width appears on scrren,and it's default value is one third of the CurtainView's width or height(depends on current gravity).

Usually we set this in codes.For example in upper case, I want only the ImageView appears when the CurtainView is closed,so we just need to set the fixedValue to be the ImageView's height:
        
	curtainView.post(new Runnable() {
	@Override
		public void run() {
			curtainView.setCurtainGravityAndFixedValue(null,imageView.getHeight());
		}
	});



   
    


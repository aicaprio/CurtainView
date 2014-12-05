CurtainView
===========

The CurtainView is much like a layer on the top level of your layout. It's also a container which can wrap other Views.

And it will not block the actions of the child view unless you make a standard pull event.

The MotionEvent handling is base on chrisbanes' library : https://github.com/chrisbanes/Android-PullToRefresh.

![image](https://github.com/aicaprio/CurtainView/blob/master/preview/p1.gif)   

![image](https://github.com/aicaprio/CurtainView/blob/master/preview/p2.gif)

How to Use
===========
Just some simple configs:

    <com.movitech.aicaprio.CurtainView
        xmlns:curtainView="http://schemas.android.com/apk/res-auto"
        android:id="@+id/cb1"
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
    
There is one noteworthy attribute:"curtainView:fixedValue",which determines the minimum hegiht or width appears on scrren,and it's default value is one third of the CurtainView's width or height(depends on current gravity).

Usually we set this in codes.For example in upper case, I want a only the ImageView to be shown when the CurtainView is closed,so we just need to set the fixedValue to be the ImageView's height:
        
 curtainView.post(new Runnable() {
	@Override
	public void run() {
		curtainView.setCurtainGravityAndFixedValue(null,imageView.getHeight());
	}
});



   
    


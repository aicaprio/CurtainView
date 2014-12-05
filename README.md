CurtainView
===========

The CurtainView is much like a layer on the top level of your layout. It's also a container which can wrap other Views.

And it will not block the actions of the child view unless you make a standard pull event.

The MotionEvent handling is base on chrisbanes' library : https://github.com/chrisbanes/Android-PullToRefresh.

![image](https://github.com/aicaprio/CurtainView/blob/master/preview/p1.gif)   

![image](https://github.com/aicaprio/CurtainView/blob/master/preview/p2.gif)

How to Use
===========
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
        
        



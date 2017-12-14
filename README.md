CurtainView
===========

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-CurtainView-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1244)

This view is much like a layer on the top level of your layout. It's also a container which can wrap other Views.

And it will not block the actions of the child view unless you make a standard pull event.

MotionEvent handling is base on [Chris Banes' library](https://github.com/chrisbanes/Android-PullToRefresh).

![image](https://github.com/aicaprio/CurtainView/blob/master/imgs/ezgif-5-4fc93a6397.gif)   

![image](https://github.com/aicaprio/CurtainView/blob/master/imgs/ezgif-5-f6189eb790.gif)


Download
--------

```groovy
dependencies {
  compile 'org.aicaprio:curtainview:1.0.1'
}
```

Usage
--------
Just a few configs:
```java
<org.aicaprio.curtainview.CurtainView
    xmlns:cv="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:layout_gravity="top"
    cv:curtainGravity="top"
    cv:curtainStatus="closed"
    cv:reboundMode="half"
    cv:scrollDuration="1000">

    <ImageView
        android:id="@id/iv1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom"
        android:src="@drawable/ic_pull"/>

</org.aicaprio.curtainview.CurtainView>
    
 ```
 
 ###### Attributes
    
> "curtainView:curtainGravity": The visual gravity of CurtainView. 

"curtainView:curtainStatus": Whether the CurtainView is opend or closed.

"curtainView:reboundMode": After lift the finger off the CurtainView,how will it scroll to.

"curtainView:scrollDuration:": The scrolling duration after lift the finger off CurtainView.
    
There is one noteworthy attribute:"curtainView:fixedValue",which determines the minimum hegiht or width appears on scrren,and it's default value is one third of the CurtainView's width or height(depends on current gravity).

Usually we set this in codes.For example in upper case, I want only the ImageView appears when the CurtainView is closed,so we just need to set the fixedValue to be the ImageView's height:

```java
        
curtainView.post(new Runnable() {
    @Override
    public void run() {
        curtainView.setCurtainGravityAndFixedValue(null,imageView.getHeight());
    }
});

```java


   
    


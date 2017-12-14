CurtainView
===========

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-CurtainView-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1244)

Like [DrawerLayout](https://github.com/aosp-mirror/platform_frameworks_support/blob/master/core-ui/src/main/java/android/support/v4/widget/DrawerLayout.java) , but can layer both horizontally and vertically .

![image](https://github.com/aicaprio/CurtainView/blob/master/imgs/ezgif-5-4fc93a6397.gif)   

![image](https://github.com/aicaprio/CurtainView/blob/master/imgs/ezgif-5-f6189eb790.gif)


Download
--------

Add the library to your module's `build.gradle`:

```groovy
dependencies {
  compile 'org.aicaprio:curtainview:1.0.1'
}
```

Usage
--------

Just few configs:

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
 
 #### Attributes Desc
    
* `curtainGravity`  :  **the visual gravity of CurtainView**. 

* `curtainStatus`  :  **whether the CurtainView is opend or closed.**

* `reboundMode`  :  **how will it scroll when lift fingers off the CurtainView**

* `scrollDuration`  :  **scrolling duration after lift the finger off CurtainView.**

    
* Here is an attribute need to be noticed : `fixedValue` ,

**which defines the minimum width or height( depends on the gravity ) appears on the screen , the default value is one third of the CurtainView's width or height( depends on the gravity )**.

Usually we set this attribute in your java codes , for example: 

I'd like `iv1` completely showing when CurtainView is closed , so we just need to set the `fixedValue` to be `iv1's` height:

```java       
curtainView.post(
    () -> mCurtainView.setCurtainGravityAndFixedValue(null, mCurtainView.getHeight())
);
```


   
    


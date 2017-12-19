CurtainView
===========

[![license](http://img.shields.io/badge/license-Apache2.0-brightgreen.svg?style=flat)](https://github.com/alibaba/atlas/blob/master/LICENSE) 
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

#### Just few configs :

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
 
 #### Attributes Desc :
    
* `curtainGravity`  :  **visual gravity of CurtainView**. 

* `curtainStatus`  :  **whether the CurtainView is opend or closed.**

* `reboundMode`  :  **how will it scrolls when fingers off CurtainView**

* `scrollDuration`  :  **scrolling duration after fingers off CurtainView.**

*** 

Here is an attribute need to be noticed : `fixedValue` ,

**which defines the minimum width or height( depends on the gravity ) appears on the screen , the default value is one third of the CurtainView's width or height( depends on the gravity )**.

Usually we set this attribute in your java codes , for example: 

We'd like `iv1` completely showing when CurtainView is closed , so we just need to set the `fixedValue` to be `iv1's` height:

```java       
mCurtainView.post(
    () -> mCurtainView.setCurtainGravityAndFixedValue(null, mCurtainView.getHeight())
);
```

License
--------
```
Copyright 2014 aicaprio

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
   
    


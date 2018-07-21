# Throbber
[![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)

Simple throbber for Android apps.

### Preview
![Preview animation](https://raw.githubusercontent.com/dyvoker/throbber/master/sample/throbber_preview.gif)

### Usage ([sample](https://github.com/dyvoker/throbber/tree/master/sample))
Add dependency:
```gradle
dependencies {
    implementation 'com.github.dyvoker:throbber:1.0'
}
```

#### Simple (xml)
```xml
<com.github.dyvoker.throbber.ThrobberView
    android:layout_width="64dp"
    android:layout_height="64dp"
    />
```

#### Custom (xml)
```xml
<com.github.dyvoker.throbber.ThrobberView
    android:layout_width="64dp"
    android:layout_height="64dp"
    auto:throbber_barColor="@color/colorPrimary"
    auto:throbber_barWidth="4dp"
    auto:throbber_showCircleBackground="true"
    auto:throbber_circleBackgroundColor="@android:color/white"
    auto:throbber_shadowColor="@color/defaultShadowColor"
    auto:throbber_shadowOffsetX="0dp"
    auto:throbber_shadowOffsetY="2dp"
    auto:throbber_shadowRadius="2dp"    
    />
```

#### Custom (programmatically)
```java
ThrobberView throbberView = new ThrobberView(this);
CircleDrawable circleDrawable = new CircleDrawable(Color.WHITE, 0x80000000, 2, 0, 2);
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
    throbberView.setBackground(circleDrawable);
} else {
    throbberView.setBackgroundDrawable(circleDrawable);
}
FrameLayout throbberContainer = findViewById(R.id.throbber_container);
throbberContainer.addView(throbberView);
ViewGroup.LayoutParams layoutParams = throbberView.getLayoutParams();
layoutParams.width = (int) DpUtils.dpToPx(64);
layoutParams.height = (int) DpUtils.dpToPx(64);
throbberView.setLayoutParams(layoutParams);
```

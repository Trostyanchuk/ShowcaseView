# ShowcaseView

ShowcaseView is a simple UI element that could be added to application to focus on important control with short overview or explanation how it should work.

You can download and build sample application or start using view with parameters as show in example below:

```java
 showcaseView = new ShowcaseView(this)
    .addTargetView(targetView)
    .setShowcaseEventListener(this)
    .setShowcaseParams(params);
    showcaseView.show(MainActivity.this);
```

With custom parameters:

```java
 ShowcaseParams params = new ShowcaseParams()
    .setButtonText(getString(R.string.toolbar_btn_text))
    .setMessageText(getString(R.string.toolbar_message_text))
    .setButtonTypeface(TypefaceUtil.getTypeface(MainActivity.this)
    .getString(R.string.font_medium)))
    .setMessageTypeface(TypefaceUtil.getTypeface(MainActivity.this, getString(R.string.font_light)))
    .setButtonTextColorRes(android.R.color.black)
    .setMessageTextColorRes(android.R.color.holo_blue_dark)
    .setBackgroundColorRes(R.color.red_dark)
    .setCirclesColorRes(R.color.red_dark)
    .setBackgroundAlpha(0.8f);
```

## Next updates:
 1. Add multiple views support
 2. Update README.md with video and full documentation
 3. Publish sample app

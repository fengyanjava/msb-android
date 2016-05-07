# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\android-sdk21/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# -dontoptimize
# -dontpreverify

-keepattributes *Annotation*
-keepattributes Signature
-ignorewarnings

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

-keep class com.mianshibang.main.model.** {*;}
-keep class com.mianshibang.main.R {*;}
-keep class com.mianshibang.main.R$* {*;}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * {
    public void onEvent*();
    public void onEvent*(**);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# supportv4
-keep class android.support.v4.** {*;}

# umeng
-keep class com.umeng.analytics.** {*;}
-keep class u.aly.** {*;}
-keep class org.android.agoo.** {*;}
-keep class com.umeng.message.** {*;}

# wechat
-keep class com.tencent.mm.** {*;}
-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}

# qq share
-keep class com.tencent.** {*;}

# picasso
-keep class com.squareup.picasso.** {*;}

# nineoldandroids
-keep class com.nineoldandroids.** {*;}

# gson
-keep class com.google.gson.** {*;}

# weibo sdk
-keep class com.sina.** {*;}

# volley
-keep class com.android.volley.** {*;}

# volley
-keep class com.handmark.pulltorefresh.** {*;}

# spinnerwheel
-keep class antistatic.spinnerwheel.** {*;}

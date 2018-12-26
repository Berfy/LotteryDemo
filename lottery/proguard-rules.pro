#进行除算法指令,字段,类合并的所有优化;
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
#执行优化2次
-optimizationpasses 2
#优化时允许访问并修改有修饰符的类和类的成员.
-allowaccessmodification
-dontoptimize
-ignorewarnings
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-keepattributes *JavascriptInterface*,*Annotation*, Exceptions, Signature, Deprecated, SourceFile, SourceDir, LineNumberTable, LocalVariableTable, LocalVariableTypeTable, Synthetic, EnclosingMethod, RuntimeVisibleAnnotations, RuntimeInvisibleAnnotations, RuntimeVisibleParameterAnnotations, RuntimeInvisibleParameterAnnotations, AnnotationDefault, InnerClasses,ProtoContract,ProtoMember

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}

-keep public abstract interface com.asqw.android.Listener{
public protected <methods>;
}
# 保留本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#内部类
-keepclassmembers class * implements java.io.Serializable$* { *; }

-keep class **.R$* {
 *;
}

-keepclassmembers class * {
    void *(**On*Event);
}

#model
-keep class cn.zcgames.lottery.personal.model.** { *; }

#event
-keep class cn.zcgames.lottery.event.** { *; }

#model
-keep class cn.zcgames.lottery.bean.** { *; }
-keep class cn.zcgames.lottery.home.bean.** { *; }
-keep class cn.zcgames.lottery.home.trend.bean.** { *; }
-keep class cn.berfy.sdk.mvpbase.model.** { *; }

#Glide的混淆处理
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#greenrobot
-keep class org.greenrobot.** { *; }
-dontwarn org.greenrobot.**
#EventBus
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

#hamcrest
-keep class org.hamcrest.** { *; }
-dontwarn org.hamcrest.**

#apache
-keep class org.apache.** { *; }
-dontwarn org.apache.**

#nineoldandroids
-keep class com.nineoldandroids.** { *; }
-dontwarn com.nineoldandroids.**

# OkHttp3
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**

# picasso
-keep class com.squareup.picasso.**{*;}
-dontwarn com.squareup.picasso.**

# rxpermissions
-keep class com.tbruyelle.rxpermissions.**{*;}
-dontwarn com.tbruyelle.rxpermissions.**

# Gson
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-dontwarn com.google.gson.stream.**
-dontwarn sun.misc.Unsafe
#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#1em0nsOft
-keep class net.lemonsoft.lemonbubble.** { *; }
-dontwarn net.lemonsoft.lemonbubble.**

#rxjava
-keep class io.reactivex.** { *; }
-dontwarn io.reactivex.**

#glide
-keep class jp.wasabeef.glide.** { *; }
-dontwarn jp.wasabeef.glide.**

#permissions
-keep class com.hjq.permissions.** { *; }
-dontwarn com.hjq.permissions.**

#umeng
-keep class com.umeng.** { *; }
-dontwarn com.umeng.**
-keep class com.tencent.** { *; }
-dontwarn com.tencent.**

#Jpush
-dontpreverify

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

#==================gson && protobuf==========================
-dontwarn com.google.**
-keep class com.google.gson.** {*;}
-keep class com.google.protobuf.** {*;}
#---------------------mqtt-------------------------
-dontwarn org.eclipse.paho.**
-keep class org.eclipse.paho.** {*;}
#---------------------mqtt-------------------------

#---------------------------------webview------------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keep public class android.Net.http.SslError{*;}
-keep public class android.webkit.WebViewClient{*;}
-keep public class android.webkit.WebChromeClient{*;}
-keep public interface android.webkit.WebChromeClient$CustomViewCallback {*;}
-keep public interface android.webkit.ValueCallback {*;}
-keep class * implements android.webkit.WebChromeClient {*;}
#---------------------------------webview------------------------------------







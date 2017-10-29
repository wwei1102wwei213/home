# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\AndroidStudio\sdk/tools/proguard/proguard-android.txt
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

##########################################################################################

#指定代码的压缩级别
    -optimizationpasses 5

    #包明不混合大小写
    -dontusemixedcaseclassnames

    #不去忽略非公共的库类
    -dontskipnonpubliclibraryclasses

     #优化  不优化输入的类文件
    -dontoptimize

     #预校验
    -dontpreverify

     #混淆时是否记录日志
    -verbose

     # 混淆时所采用的算法
    -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

    #保护注解
    -keepattributes *Annotation*
    #阿里云
    -dontwarn com.alibaba.sdk.android.oss.**
    -dontwarn com.apache.http.**
    -dontwarn org.apache.http.**
    -dontwarn java.nio.file.**
    -dontwarn org.codehaus.mojo.**
    -dontwarn javax.lang.model.**


    -keep class butterknife.** { *; }
    -dontwarn butterknife.internal.**
    -keep class **$$ViewBinder { *; }

    -keepclasseswithmembernames class * {
        @butterknife.* <fields>;
    }

    -keepclasseswithmembernames class * {
        @butterknife.* <methods>;
    }


    # 保持哪些类不被混淆
    -keep class android.** { *; }
    -keep class android.os.** { *; }
    -keep class android.util.** { *; }
    -keep public class * extends android.view
    -keep public class * extends android.app.Fragment
    -keep public class * extends android.support.v4.app.Fragment
    -keep public class * extends android.app.Dialog
    -keep public class * extends android.app.DialogFragment
    -keep public class * extends android.support.v4.app.DialogFragment
    -keep public class * extends android.app.Activity
    -keep public class * extends android.app.Application
    -keep public class * extends android.app.Service
    -keep public class * extends android.content.BroadcastReceiver
    -keep public class * extends android.content.ContentProvider
    -keep public class * extends android.app.backup.BackupAgentHelper
    -keep public class * extends android.preference.Preference
    -keep public class com.android.vending.licensing.ILicensingService


    #忽略警告
    -ignorewarning

    ##记录生成的日志数据,gradle build时在本项目根目录输出##

    #apk 包内所有 class 的内部结构
    -dump class_files.txt
    #未混淆的类和成员
    -printseeds seeds.txt
    #列出从 apk 中删除的代码
    -printusage unused.txt
    #混淆前后的映射
    -printmapping mapping.txt

    ########记录生成的日志数据，gradle build时 在本项目根目录输出-end######

    #如果不想混淆 keep 掉
    -keep class com.lippi.recorder.iirfilterdesigner.** {*; }

    #忽略警告
    -dontwarn com.lippi.recorder.utils**
    #保留一个完整的包
    -keep class com.lippi.recorder.utils.** {
        *;
     }

    -keep class  com.lippi.recorder.utils.AudioRecorder{*;}

    #如果引用了v4或者v7包
    -dontwarn android.support.**

    -keep public class * extends android.view.View {
        public <init>(android.content.Context);
        public <init>(android.content.Context, android.util.AttributeSet);
        public <init>(android.content.Context, android.util.AttributeSet, int);
        public void set*(...);
    }

    #保持 native 方法不被混淆
    -keepclasseswithmembernames class * {
        native <methods>;
    }

    #保持自定义控件类不被混淆
    -keepclasseswithmembers class * {
        public <init>(android.content.Context, android.util.AttributeSet);
    }

    #保持自定义控件类不被混淆
    -keepclassmembers class * extends android.app.Activity {
       public void *(android.view.View);
    }

    #保持 Parcelable 不被混淆
    -keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
    }

    #保持 Serializable 不被混淆
    -keepnames class * implements java.io.Serializable

    #保持 Serializable 不被混淆并且enum 类也不被混淆
    -keepclassmembers class * implements java.io.Serializable {
        static final long serialVersionUID;
        private static final java.io.ObjectStreamField[] serialPersistentFields;
        !static !transient <fields>;
        !private <fields>;
        !private <methods>;
        private void writeObject(java.io.ObjectOutputStream);
        private void readObject(java.io.ObjectInputStream);
        java.lang.Object writeReplace();
        java.lang.Object readResolve();
    }

    -keepclassmembers class * {
        public void *ButtonClicked(android.view.View);
    }

    #不混淆资源类
    -keepclassmembers class **.R$* {
        public static <fields>;
    }

    #避免混淆泛型 如果混淆报错建议关掉
    #–keepattributes Signature



    # glide的混淆
    -keep public class * implements com.bumptech.glide.module.GlideModule
    -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
        **[] $VALUES;
        public *;
    }
    -keep class com.bumptech.** { *; }

    -keep class **.R$* { *; }  #保持R文件不被混淆，否则，你的反射是获取不到资源id的

    -keep class **.Webview2JsInterface { *; }  #保护WebView对HTML页面的API不被混淆
    -keepclassmembers class * extends android.webkit.WebViewClient {  #如果你的项目中用到了webview的复杂操作 ，最好加入
         public void *(android.webkit.WebView,java.lang.String,android.graphics.Bitmap);
         public boolean *(android.webkit.WebView,java.lang.String);
    }
    -keepclassmembers class * extends android.webkit.WebChromeClient {  #如果你的项目中用到了webview的复杂操作 ，最好加入
         public void *(android.webkit.WebView,java.lang.String);
    }

    #webview
    -dontwarn android.webkit.WebView
    #友盟
    -dontwarn com.umeng.**

    -keep class com.umeng.** { *; }
#    -keep class com.umeng.message.** { *; }

    -keepclassmembers class * {
       public <init>(org.json.JSONObject);
    }

    -keep public class [com.zeyuan.kyq].R$*{
            public static final int *;
    }

    -keepclassmembers enum * {
        public static **[] values();
        public static ** valueOf(java.lang.String);
    }



    # keep 所有的 javabean
#    -keep class com.zeyuan.kyq.presenter.**{*;}
#    -keep class com.zeyuan.kyq.utils.**{*;}
    -dontwarn com.apache.http.**
#    -dontwarn com.zeyuan.kyq.**
    -keep class com.zeyuan.kyq.** { *; }
    -keep class com.zeyuan.kyq.bean.** { *; }
    -keep class com.zeyuan.kyq.http.** { *; }
    -keep class com.zeyuan.kyq.http.bean.** { *; }
    -keep class com.zeyuan.kyq.app.** { *; }
    -keep class com.zeyuan.kyq.adapter.** { *; }
    -keep class com.zeyuan.kyq.biz.** { *; }
    -keep class com.zeyuan.kyq.view.** { *; }
    -keep class com.zeyuan.kyq.biz.forcallback.** { *; }
    -keep class com.zeyuan.kyq.utils.** { *; }
    -keep class com.zeyuan.kyq.utils.BlurUtil.** { *; }
    -keep class com.zeyuan.kyq.utils.PayHttp.** { *; }
    -keep class com.zeyuan.kyq.utils.Secret.** { *; }
    -keep class com.zeyuan.kyq.db.** { *; }
    -keep class com.zeyuan.kyq.application.** { *; }
    -keep class com.zeyuan.kyq.filedownloader.** { *; }
    -keep class com.zeyuan.kyq.page.** { *; }
    -keep class com.zeyuan.kyq.page.kangfu.** { *; }
    -keep class com.zeyuan.kyq.page.sideeffect.** { *; }
    -keep class com.zeyuan.kyq.presenter.** { *; }
    -keep class com.zeyuan.kyq.entity.** { *; }
    -keep class com.zeyuan.kyq.widget.** { *; }
    -keep class com.zeyuan.kyq.widget.CustomView.** { *; }
    -keep class com.zeyuan.kyq.R.** { *; }

    #映客混淆
    -dontwarn com.morgoo.**
    -keep class com.morgoo.droidplugin.**{ *; }
    -keep class com.morgoo.helper.**{ *; }
    -dontwarn com.loopj.android.http.**
    -dontwarn com.meelive.ingkee.sdk.**
    -keep class com.loopj.android.http.**{ *; }
    -keep class com.meelive.ingkee.sdk.**{ *; }

    # http client
    -keep class org.apache.http.** {*; }
    -keep class com.squareup.okhttp.** { *; }
    -keep class com.google.gson.** { *; }
    -dontwarn com.ta.**
    -dontwarn com.cat.**
    -keep class com.ta.** { *; }
    -keep class com.cat.** { *; }
    -keepattributes SourceFile,LineNumberTable


    # Gson uses generic type information stored in a class file when working with fields. Proguard
    # removes such information by default, so configure it to keep all of it.
    -keepattributes Signature

    # Gson specific classes
    -keep class sun.misc.Unsafe { *; }
    -keep class com.google.gson.stream.** { *; }

    # Application classes that will be serialized/deserialized over Gson
    -keep class com.google.gson.examples.android.model.** { *; }

    -keepclasseswithmembers class * {
        public <init>(android.content.Context, android.util.AttributeSet);  #保持自定义控件类不被混淆，指定格式的构造方法不去混淆
    }

    -keepclasseswithmembers class * {
        public <init>(android.content.Context, android.util.AttributeSet, int);
    }


    -keep class * extends android.support.v4.**{*;}
    -keep class * extends android.support.v7.**{*;}
    -keep class android.support.v7.** { *; }
    -keep interface android.support.v7.app.** { *; }


    #友盟
    -dontwarn com.ut.mini.**
    -dontwarn okio.**
    -dontwarn com.xiaomi.**
    -dontwarn com.squareup.wire.**
    -dontwarn android.support.v4.**
    -dontwarn android.support.v7.**
    -dontwarn android.app.**

    -keepattributes *Annotation*

    -keep class android.support.v4.** { *; }
    -keep interface android.support.v4.app.** { *; }

    -keep class android.app.** { *; }
    -keep interface android.app.** { *; }

    -keep class okio.** {*;}
    -keep class com.squareup.wire.** {*;}

    -keep class com.umeng.message.protobuffer.* {
    	 public <fields>;
             public <methods>;
    }

    -keep class com.umeng.message.* {
    	 public <fields>;
             public <methods>;
    }

    -keep class com.umeng.analytics.*{
        public <fields>;
             public <methods>;
    }

    -keep class org.android.agoo.impl.* {
    	 public <fields>;
             public <methods>;
    }

    -keep class org.android.agoo.service.* {*;}

    -keep class org.android.spdy.**{*;}

    -keep public class **.R$*{
        public static final int *;
    }
    #如果compileSdkVersion为23，请添加以下混淆代码
    -dontwarn org.apache.http.**
    -dontwarn android.webkit.**
    -keep class org.apache.http.** { *; }
    -keep class org.apache.commons.codec.** { *; }
    -keep class org.apache.commons.logging.** { *; }
    -keep class android.net.compatibility.** { *; }
    -keep class android.net.http.** { *; }



    #腾讯和微信的混淆
    -keep class com.tencent.** { *; }
    -dontwarn com.tencent.mm.**
    -keep class com.tencent.mm.** { *; }
    -keep class com.tencent.wxop.** { *; }
    -keep class com.tencent.tauth.** { *; }

    -dontwarn java.util.**
    -dontwarn java.io.**
    -dontwarn java.lang.**

    #阿里百川混淆设置
    -keep class com.alibaba.sdk.android.feedback.impl.FeedbackServiceImpl {*;}
    -keep class com.alibaba.sdk.android.feedback.impl.FeedbackAPI {*;}
    -keep class com.alibaba.sdk.android.feedback.util.IWxCallback {*;}
    -keep class com.alibaba.sdk.android.feedback.util.IUnreadCountCallback{*;}
    -keep class com.alibaba.sdk.android.feedback.FeedbackService{*;}
    -keep public class com.alibaba.mtl.log.model.LogField {public *;}
    -keep class com.taobao.securityjni.**{*;}
    -keep class com.taobao.wireless.security.**{*;}
    -keep class com.ut.secbody.**{*;}
    -keep class com.taobao.dp.**{*;}
    -keep class com.alibaba.wireless.security.**{*;}
    -keep class com.ta.utdid2.device.**{*;}

    #友盟推送
    -dontwarn com.taobao.**
    -dontwarn anet.channel.**
    -dontwarn anetwork.channel.**
    -dontwarn org.android.**
    -dontwarn org.apache.thrift.**
    -dontwarn com.xiaomi.**
    -dontwarn com.huawei.**

    -keepattributes *Annotation*

    -keep class com.taobao.** {*;}
    -keep class org.android.** {*;}
    -keep class anet.channel.** {*;}
    -keep class com.umeng.** {*;}
    -keep class com.xiaomi.** {*;}
    -keep class com.huawei.** {*;}
    -keep class org.apache.thrift.** {*;}

    -keep class com.alibaba.sdk.android.**{*;}
    -keep class com.ut.**{*;}
    -keep class com.ta.**{*;}

    -keep public class **.R$*{
       public static final int *;
    }

    #权限框架混淆代码
    -keepclassmembers class ** {
        @com.yanzhenjie.permission.PermissionYes <methods>;
    }
    -keepclassmembers class ** {
        @com.yanzhenjie.permission.PermissionNo <methods>;
    }

    # 有赞sdk混淆 Resource class R, suggest to keep class R in package of{applicationId} only.
    -keep class **.R$* {*;}
    -keep class **.R{*;}
    # Youzan SDK
    -dontwarn com.youzan.sdk.***
    -keep class com.youzan.sdk.**{*;}
    # OkHttp
    -dontwarn okio.**
    -dontwarn com.squareup.okhttp.**
    -keep class okio.**{*;}
    -keep class com.squareup.okhttp.** { *; }
    -keep interface com.squareup.okhttp.** { *; }
    -dontwarn java.nio.file.*
    -dontwarn javax.annotation.**
    -dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
    # Image Loader
    -keep class com.squareup.picasso.Picasso
    -keep class com.android.volley.toolbox.Volley
    -keep class com.bumptech.glide.Glide
    -keep class com.nostra13.universalimageloader.core.ImageLoader
    -keep class com.facebook.drawee.backends.pipeline.Fresco


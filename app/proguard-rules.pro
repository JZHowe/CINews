# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Android_SDK\Android_SDK/tools/proguard/proguard-android.txt
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

    # 保持哪些类不被混淆
    -keep public class * extends android.app.Fragment
    -keep public class * extends android.app.Activity
    -keep public class * extends android.app.Application
    -keep public class * extends android.app.Service
    -keep public class * extends android.content.BroadcastReceiver
    -keep public class * extends android.content.ContentProvider
    -keep public class * extends android.app.backup.BackupAgentHelper
    -keep public class * extends android.preference.Preference
    -keep public class com.android.vending.licensing.ILicensingService
    #如果有引用v4包可以添加下面这行
    -keep public class * extends android.support.v4.app.Fragment



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

    #如果引用了v4或者v7包
    -dontwarn android.support.**


    #我是以libaray的形式引用了一个图片加载框架,如果不想混淆 keep 掉
    -keep class com.handmark.pulltorefresh.library.** { *; }


    #保存导入的第三方jar不被混淆


    #sharedSDK
    -keep class cn.sharesdk.**{*;}
    -keep class com.sina.**{*;}
    -keep class **.R$* {*;}
    -keep class **.R{*;}
    -dontwarn cn.sharesdk.**
    -dontwarn **.R$*

    #volley  com.mcxiaoke.volley
    -keep class com.mcxiaoke.volley.**{*;}
    -keep class com.android.volle.**{*;}
    -keep class com.jju.yuxin.cinews.volleyutils.**{*;}
    #百度
    -keep class com.baidu.**{*;}
    #极光
    -keep class cn.jpush.**{*;}
    #ksoup
    -keep class org.**{*;}
    #腾讯
    -keep class com.tencent.**{*;}
    -keep class cn.sharesdk.tentcent.qq.**{*;}
    -keep class cn.sharesdk.tentcent.qqzone.**{*;}
    -keep class cn.sharesdk.tentcent.weibo.**{*;}
    #litepal
    -keep class org.litepal.**{*;}

    #mob
    -keep class com.mob.commons.**{*;}
    -keep class com.mob.tools.**{*;}
    -keep class cn.sharesdk.framework.**{*;}

    #alipay
    -keep class cn.sharesdk.alipay.friends.**{*;}
    -keep class cn.sharesdk.alipay.utils.**{*;}
    -keep class cn.sharesdk.alipay.moments.**{*;}

    #豆瓣
    -keep class cn.sharesdk.douban.**{*;}

    #Flickr
    -keep class cn.sharesdk.flickr.**{*;}

    #人人
    -keep class cn.sharesdk.renren.**{*;}

    #短信
    -keep class cn.sharesdk.system.text.**{*;}

    #新浪微博
    -keep class cn.sharesdk.sina.weibo.**{*;}
    -keep class com.sina.**{*;}

    #微信
    -keep class cn.sharesdk.wechat.friends.**{*;}
    -keep class cn.sharesdk.wechat.utils.**{*;}
    -keep class cn.sharesdk.wechat.favorite.**{*;}
    -keep class cn.sharesdk.wechat.moments.**{*;}


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

    #不混淆资源类
    -keepclassmembers class **.R$* {
        public static <fields>;
        }
    #避免混淆泛型 如果混淆报错建议关掉
    #–keepattributes Signature
      #如果用用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错。
        #gson
        #-libraryjars libs/gson-2.2.2.jar
        -keepattributes Signature
        # Gson specific classes
        -keep class sun.misc.Unsafe { *; }
        # Application classes that will be serialized/deserialized over Gson
        -keep class com.google.gson.examples.android.model.** { *; }

        #如果你使用了webview
        # webview + js
        -keepattributes *JavascriptInterface*
        # keep 使用 webview 的类
        -keepclassmembers class  com.veidy.activity.WebViewActivity {
           public *;
        }
        # keep 使用 webview 的类的所有的内部类
        -keepclassmembers  class  com.veidy.activity.WebViewActivity$*{
            *;
        }
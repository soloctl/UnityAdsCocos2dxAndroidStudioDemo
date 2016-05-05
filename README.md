This demo project shows how to integrate the Unity Ads SDK into a Cocos2Dx Android game

*Asume you already have a Cocos2d-x and Android native development environment. If you aren't, [Cocos2d-x](http://www.cocos2d-x.org) and [Android NDK](http://developer.android.com/ndk/index.html) resources are for your references.

# How to integrate Unity Ads into your Cocos2dx Android project

## Operating on UniytAds dashboard

### Create a Game Project on the [Unity Ads Dashboard](https://dashboard.unityads.unity3d.com)
- Log into the [dashboard](https://dashboard.unityads.unity3d.com) using your [UDN Account](https://accounts.unity3d.com/sign-in)
- Create a new game project
- Look for your Android **Game ID** in the project, a 7-digit number that you will use in your integration


## Add Unity Ads Android SDK in your project.

### Import the Unity Ads AAR Framework
  1. Download the Unity SDK from https://github.com/Applifier/unity-ads-sdk
  2. Unzip the project, and locate `unity-ads.aar`. This package contains all binaries and configuration files for Unity Ads SDK.
  3. You start importing Unity Ads by selecting 'File' > 'New' > 'New Module' and choosing 'Import .JAR/.AAR Package'. Select the downloaded aar file from your computer and import the file.
  4. Now you need to add the imported Unity Ads module as a dependency to your own module. Right click your app module, select 'Open Module Settings' and open 'Dependencies' tab. Add a new dependency as 'Module Dependency' and select Unity Ads module.



### Setup Unity Ads in Android layer

Import UnityAds to your **AppActivity**
  1. Import UnityAds package **`UnityAds`** and **`IUnityAdsListener`** in **AppActivity** file   
  
      ```Java
	  import com.unity3d.ads.android.IUnityAdsListener;
	  import com.unity3d.ads.android.UnityAds;
      ```
      
  2. Make your **AppActivity** implements **`IUnityAdsListener`** protocol  

      ```Java
      public class AppActivity extends Cocos2dxActivity implements IUnityAdsListener {
      ```
  3. Implement **`IUnityAdsListener`** methods in **AppActivity**  

      ```Java
      public void onHide(){
          Log.d("[Ads Test Lifecycle]", "onHide");
      }

      public void onShow(){
          Log.d("[Ads Test Lifecycle]", "onShow");
      }

      public void onVideoStarted(){
          Log.d("[Ads Test Lifecycle]", "onVideoStarted");
      }

      public  void onVideoCompleted(String itemKey, boolean skipped){
          Log.d("[Ads Test Lifecycle]", "[onVideoCompleted] with key="+itemKey+", and skipped="+skipped);
      }

      public void onFetchCompleted(){
          Log.d("[Ads Test Lifecycle]", "onFetchCompleted");
      }

      public void onFetchFailed(){
          Log.d("[Ads Test Lifecycle]", "onFetchFailed");
      }
      ```
  4. Initialize **UnityAds** in **`UnityAdsInit`** method.

      ```Java
      @Override
      protected void onCreate(final Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          initAd();
      }

      @Override
      protected void onResume() {
          super.onResume();
          UnityAds.changeActivity(this);
      }

      private void initAd(){
          Log.d("[Ads Test]", "initializing ad");
          UnityAds.setTestMode(true);
          UnityAds.init((Activity) this, "1044314", (IUnityAdsListener) this);
      }
      ```

## Call Android functions from your Cocos2dx game

### Add Java JNI Interface
  1. Create **UnityAdsNDK.java** file.
  2. Add static methods for native to call.
  
    ```Java
    import com.unity3d.ads.android.UnityAds;
    import android.util.Log;

    public class UnityAdsNDK {

        public static boolean canUnityAdsShow() {
            Log.d("[Ads Test]", "canUnityAdsShow");
            return UnityAds.canShow();
        }

        public static void showUnityAds() {
            Log.d("[Ads Test]", "showUnityAds");
            UnityAds.show();
        }
    }
    ```
### Call Unity Ads Methods From C++
  1. You need open the Cocos2d-x project root folder by using Android Studio in order to see the JNI C++ folder. The folder is located at **`{YourCocos2dxProjectRoot}/proj.android-studio/app/jni/ `**, Create the header file and C++ implementation file **UnityAdsBridge.h** and **UnityAdsBridge.cpp**
  2. Move **UnityAdsBridge.h** to Classes folder in order to be called thru class path.
  3. Add the header file and implementation C++ file in **Android.mk**
  
  ```C
	LOCAL_SRC_FILES := hellocpp/main.cpp \
	                   hellocpp/UnityAdsJni.cpp \
	                   ../../../Classes/AppDelegate.cpp \
	                   ../../../Classes/HelloWorldScene.cpp
	
	LOCAL_C_INCLUDES := $(LOCAL_PATH)/../../../Classes \
	                    hellocpp/UnityAdsJni.h
  ```
  
  4. Add below source code in the header file

	```Cpp
	#include <jni.h>
	#include "platform/android/jni/JniHelper.h"
	
	#ifdef __cplusplus
	extern "C" {
	#endif
	    extern bool canShowJni();
	    extern void showJni();
	
	#ifdef __cplusplus
	}
	#endif
	```
  5. Add below implementation in the C++ implementation file.
	
	```Cpp
	#include "UnityAdsBridge.h"
	#include <android/log.h>
	#include "AppDelegate.h"
	#include "cocos2d.h"
	
	#define LOG_TAG    "UnityAdsBridge"
	#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
	
	#define CLASS_NAME "org/cocos2dx/cpp/UnityAdsNDK"
	
	using namespace cocos2d;
	
	#ifdef __cplusplus
	extern "C" {
	#endif
	    bool canShowJni()
	    {
	
	        JniMethodInfo methodInfo;
	
	        if (! JniHelper::getStaticMethodInfo(methodInfo, CLASS_NAME, "canUnityAdsShow", "()Z"))
	        {
	            LOGD("Failed to find static method of canUnityAdsShow");
	            return false;
	        }
	
	        jboolean ans = (jboolean)methodInfo.env->CallStaticBooleanMethod(methodInfo.classID, methodInfo.methodID);
	
	        bool ret = false;
	        if(JNI_TRUE == ans)ret = true;
	        return ret;
	    }
	
	    void showJni()
	    {
	
	        JniMethodInfo methodInfo;
	
	        if (! JniHelper::getStaticMethodInfo(methodInfo, CLASS_NAME, "showUnityAds", "()V"))
	        {
	            LOGD("Failed to find static method of showUnityAds");
	            return;
	        }
	
	        methodInfo.env->CallStaticVoidMethod(methodInfo.classID, methodInfo.methodID);
	
	    }
	
	#ifdef __cplusplus
	}
	#endif
	```
  6. Import **`#include "UnityAdsBridge.h"`** in **HelloWorldScene.h** file.
  7. In **`HelloWorldScene.h`** file, move the menu button to a easy to click place and make it bigger also for easy clicking.
  
  ```Cpp
  	auto closeItem = MenuItemImage::create("coin_1.png", "coin_1.png",
                                           CC_CALLBACK_1(HelloWorld::menuCloseCallback, this));
	closeItem->setPosition(Vec2(visibleSize.width/2 + origin.x, visibleSize.height/2 + origin.y));
  ```
  
  8. Replace end game function to show ads.
  
  ```Cpp
    if(canShowJni()) {
        showJni();
    }
    //Director::getInstance()->end();
  ```



### Reward the player - Call native functions from Android
We just update text from "Hello World" to "Rewarded" this time.

  1. Add reward api in **UnityAdsBridge.h** header file.
  
  ```Cpp
    JNIEXPORT void JNICALL Java_org_cocos2dx_cpp_UnityAdsNDK_reward (JNIEnv *, jobject, jstring s);
    static char* jstringTostring(JNIEnv* env, jstring jstr);
  ```
  2. Add reward implementation in the **UnityAdsBridge.cpp** file.
  
  ```Cpp
    JNIEXPORT void JNICALL Java_org_cocos2dx_cpp_UnityAdsNDK_reward (JNIEnv * env, jobject jobj, jstring zoneid)
    {
        char* ret = NULL;
		ret = jstringTostring(env, zoneid);

        LOGD("placement id = %s", ret);

        auto scene = Director::getInstance()->getRunningScene()->getChildren().at(1);
        if (typeid(*scene) == typeid(HelloWorld)) {
            static_cast<HelloWorld*>(scene)->rewardPlayer(ret);
        } else {
            LOGD("gameScene is still NULL");
        }
    }
	static char* jstringTostring(JNIEnv* env, jstring jstr)
	{
		char* rtn = NULL;

		// convert jstring to byte array
		jclass clsstring = env->FindClass("java/lang/String");
		jstring strencode = env->NewStringUTF("utf-8");
		jmethodID mid = env->GetMethodID(clsstring, "getBytes", "(Ljava/lang/String;)[B");
		jbyteArray barr= (jbyteArray)env->CallObjectMethod(jstr, mid, strencode);
		jsize alen =  env->GetArrayLength(barr);
		jbyte* ba = env->GetByteArrayElements(barr, JNI_FALSE);

		// copy byte array into char[]
		if (alen > 0)
		{
			rtn = new char[alen + 1];
			memcpy(rtn, ba, alen);
			rtn[alen] = 0;
		}
		env->ReleaseByteArrayElements(barr, ba, 0);

		return rtn;
	}
  ```
  
  3. Add native reward api call in Android layer when video completed.
  Add native interface in **`UnityAdsNDK`** class.
  
  ```Java
  public static native void reward(String placementId);
  ```
  Add call of the native interface in **`AppActivity`** class.
  
  ```Java
    public  void onVideoCompleted(String itemKey, boolean skipped){
        UnityAdsNDK.reward("rewardedVideo");
        Log.d("[Ads Test Lifecycle]", "[onVideoCompleted] with key="+itemKey+", and skipped="+skipped);
    }
  ```
  
  4. Update game logic update label text from "Hello World" to "Rewarded".
  Add **`rewardPlayer`** interface in **HelloWorld** C++ header.
  
  ```Cpp
    void rewardPlayer(char* rewardId);
  ```
  Upgrade "Hello World" label to a private field in **HelloWorld** C++ header.
  
  ```Cpp
  private:
    cocos2d::Label* _label;
  ```
  Change the declaration and use of label to use _label in **HelloWorld** C++ class.
  
  ```Cpp
    _label = Label::createWithTTF("Hello World", "fonts/Marker Felt.ttf", 60);
    
    _label->setPosition(Vec2(origin.x + visibleSize.width/2,
                            origin.y + visibleSize.height - _label->getContentSize().height));

    this->addChild(_label, 1);
  ```
  Add implementation of **`rewardPlayer `** function in **HelloWorld** C++ class.
  
  ```Cpp
	void HelloWorld::rewardPlayer(char* rewardId) {
	    _label->setString("Rewarded!");
	}
  ```



Finally, we finished a rewarded video. 

For more information, check out the [Android Integration Guide](http://unityads.unity3d.com/help/monetization/integration-guide-android), the [support Forum](http://forum.unity3d.com/forums/unity-ads.67/), or contact unityads-sales@unity3d.com

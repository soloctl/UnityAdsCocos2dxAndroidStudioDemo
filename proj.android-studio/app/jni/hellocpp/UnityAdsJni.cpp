//
// Created by Solomon Li on 4/25/16.
//

#include "UnityAdsJni.h"
#include <android/log.h>
#include "HelloWorldScene.h"
#include "AppDelegate.h"
#include "cocos2d.h"

#define LOG_TAG    "UnityAdsJni"
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)

#define CLASS_NAME "org/cocos2dx/cpp/TestNDK"

using namespace cocos2d;

#ifdef __cplusplus
extern "C" {
#endif
    JNIEXPORT void JNICALL Java_org_cocos2dx_cpp_TestNDK_reward (JNIEnv * env, jobject jobj, jstring zoneid)
    {
        char* ret = NULL;
		ret = jstringTostring(env, zoneid);

        LOGD("placement id = %s", ret);

        auto scene = Director::getInstance()->getRunningScene()->getChildren().at(1);

        //    if (scene->getName() == "MainScene") {  // これでもよい
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
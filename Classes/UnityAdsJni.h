//
// Created by Solomon Li on 4/25/16.
//

#ifndef MYGAME_UNITYADSJNI_H
#define MYGAME_UNITYADSJNI_H

#include <jni.h>
#include "platform/android/jni/JniHelper.h"

#ifdef __cplusplus
extern "C" {
#endif
    JNIEXPORT void JNICALL Java_org_cocos2dx_cpp_TestNDK_reward (JNIEnv *, jobject, jstring s);
    static char* jstringTostring(JNIEnv* env, jstring jstr);
    extern bool canShowJni();
    extern void showJni();

#ifdef __cplusplus
}
#endif

#endif //MYGAME_UNITYADSJNI_H

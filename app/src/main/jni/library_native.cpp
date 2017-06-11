#include <jni.h>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_arajago6_assessmentapp_HomeActivity_getMsgFromJNI(JNIEnv *env, jobject instance) {

    // TODO


    return env->NewStringUTF("JNI says Hi!");
}
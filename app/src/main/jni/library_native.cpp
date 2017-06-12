#include <iostream>
#include <jni.h>
#include <stdlib.h>
#include "include/library_native.h"

// C/C++ code - For flashing screen with a single color
void on_surface_created() {
    glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
}

void on_surface_changed() {
    // No-op
}

void on_draw_frame() {
    glClear(GL_COLOR_BUFFER_BIT);
}

// C/C++ code - For rendering a triangle with dynamic bg
GLuint loadShader(GLenum shaderType, const char* pSource) {
    GLuint shader = glCreateShader(shaderType);
    if (shader) {
        glShaderSource(shader, 1, &pSource, NULL);
        glCompileShader(shader);
        GLint compiled = 0;
        glGetShaderiv(shader, GL_COMPILE_STATUS, &compiled);
        if (!compiled) {
            GLint infoLen = 0;
            glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &infoLen);
            if (infoLen) {
                char* buf = (char*) malloc(infoLen);
                if (buf) {
                    glGetShaderInfoLog(shader, infoLen, NULL, buf);
                    free(buf);
                }
                glDeleteShader(shader);
                shader = 0;
            }
        }
    }
    return shader;
}

GLuint createProgram(const char* pVertexSource, const char* pFragmentSource) {
    GLuint vertexShader = loadShader(GL_VERTEX_SHADER, pVertexSource);
    if (!vertexShader) {
        return 0;
    }

    GLuint pixelShader = loadShader(GL_FRAGMENT_SHADER, pFragmentSource);
    if (!pixelShader) {
        return 0;
    }

    GLuint program = glCreateProgram();
    if (program) {
        glAttachShader(program, vertexShader);
        glAttachShader(program, pixelShader);
        glLinkProgram(program);
        GLint linkStatus = GL_FALSE;
        glGetProgramiv(program, GL_LINK_STATUS, &linkStatus);
        if (linkStatus != GL_TRUE) {
            GLint bufLength = 0;
            glGetProgramiv(program, GL_INFO_LOG_LENGTH, &bufLength);
            if (bufLength) {
                char* buf = (char*) malloc(bufLength);
                if (buf) {
                    glGetProgramInfoLog(program, bufLength, NULL, buf);
                    free(buf);
                }
            }
            glDeleteProgram(program);
            program = 0;
        }
    }
    return program;
}

void renderFrame() {
    static float grey;
    grey += 0.01f;
    if (grey > 1.0f) {
        grey = 0.0f;
    }
    glClearColor(grey, grey, grey, 1.0f);
    glClear( GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
    glUseProgram(gProgram);
    glVertexAttribPointer(gvPositionHandle, 2, GL_FLOAT, GL_FALSE, 0, gTriangleVertices);
    glEnableVertexAttribArray(gvPositionHandle);
    glDrawArrays(GL_TRIANGLES, 0, 3);
}

// JNI code
extern "C"
JNIEXPORT jstring JNICALL
Java_com_arajago6_assessmentapp_HomeActivity_getMsgFromJNI(JNIEnv *env, jobject instance) {

    return env->NewStringUTF("JNI says Hi!");
}

extern "C"
JNIEXPORT void JNICALL
Java_com_arajago6_assessmentapp_HomeActivity_callJavaNonStaticMethodFromJNI(JNIEnv *env,
                                                                            jobject instance) {

    // Build a String
    jstring jniStr = env->NewStringUTF("This string was sent to the non static method from JNI");
    // Find class that has the target method, find method id and call it using object
    jclass jniClass = env->FindClass("com/arajago6/assessmentapp/HomeActivity");
    jmethodID jniTargetMethodId = env->GetMethodID(jniClass, "javaInstanceMethod", "(Ljava/lang/String;)Ljava/lang/String;");
    jobject jniMethodResult = env->CallObjectMethod(instance, jniTargetMethodId, jniStr);
    // Get a C-style string
    const char* cStr = env->GetStringUTFChars((jstring) jniMethodResult, NULL);
    printf("%s\n", cStr);
    // Clean up
    env->ReleaseStringUTFChars(jniStr, cStr);

}

extern "C"
JNIEXPORT void JNICALL
Java_com_arajago6_assessmentapp_HomeActivity_on_1surface_1created(JNIEnv *env, jclass type) {

    on_surface_created();

}

extern "C"
JNIEXPORT void JNICALL
Java_com_arajago6_assessmentapp_HomeActivity_on_1surface_1changed(JNIEnv *env, jclass type,
                                                                jint width, jint height) {

    on_surface_changed();

}

extern "C"
JNIEXPORT void JNICALL
Java_com_arajago6_assessmentapp_HomeActivity_on_1draw_1frame(JNIEnv * env, jclass type) {

    on_draw_frame();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_arajago6_assessmentapp_HomeActivity_init(JNIEnv *env, jclass type, jint width,
                                                  jint height) {

    gProgram = createProgram(gVertexShader, gFragmentShader);
    gvPositionHandle = glGetAttribLocation(gProgram, "vPosition");
    glViewport(0, 0, width, height);

}

extern "C"
JNIEXPORT void JNICALL
Java_com_arajago6_assessmentapp_HomeActivity_step(JNIEnv *env, jclass type) {

    renderFrame();

}

#include <jni.h>
#include <string>

using namespace std;

void call() {
    printf("fuck");
}

extern "C"
JNIEXPORT jstring JNICALL
Java_io_ecosed_nativelib_NativeLib_stringFromJNI(JNIEnv *env, jobject thiz) {
    call();

    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
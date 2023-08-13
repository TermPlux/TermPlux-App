#include <jni.h>
#include <string>

using namespace std;

void call() {
    printf("fuck");
}

extern "C"
JNIEXPORT jstring JNICALL
Java_io_termplux_framework_library_NativeLib_stringFromJNI(JNIEnv *env, jobject thiz) {
    call();

    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
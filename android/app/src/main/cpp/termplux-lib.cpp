#include <jni.h>
#include <string>

using namespace std;

void call();

extern "C"
JNIEXPORT void JNICALL
Java_io_termplux_activity_MainActivity_fuck(JNIEnv *env, jobject thiz) {
    call();
}

extern "C"
JNIEXPORT void JNICALL
Java_io_termplux_activity_FlutterActivity_fuck(JNIEnv *env, jobject thiz) {
    call();
}

void call() {
    printf("fuck");
}
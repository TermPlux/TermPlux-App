#include <jni.h>
#include <string>

using namespace std;

void call();

extern "C"
JNIEXPORT void JNICALL
Java_io_termplux_activity_MainActivityOld_fuck(JNIEnv *env, jobject thiz) {
    call();
}

void call() {
    printf("fuck");
}
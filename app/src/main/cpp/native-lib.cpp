#include <jni.h>
#include <string>

using namespace std;

extern "C"
JNIEXPORT void JNICALL
Java_io_termplux_activity_MainActivityUtils_fuck(JNIEnv *env, jobject thiz) {
    printf("fuck");
}
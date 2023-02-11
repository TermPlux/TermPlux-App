#include <jni.h>
#include <string>

using namespace std;

void start() {
    system("");
}

void stop() {
    system("am kill com.termux");
}

extern "C"
JNIEXPORT void JNICALL
Java_io_termplux_services_UserService_evnStart(JNIEnv *env, jobject thiz) {
    start();
}

extern "C"
JNIEXPORT void JNICALL
Java_io_termplux_services_UserService_evnStop(JNIEnv *env, jobject thiz) {
    stop();
}
extern "C"
JNIEXPORT void JNICALL
Java_io_termplux_activity_MainActivity_stop(JNIEnv *env, jobject thiz) {
    system("am start com.android.settings/.Settings");
}
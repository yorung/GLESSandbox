#include <jni.h>
#include <gles2/gl2.h>

extern "C" {

JNIEXPORT void JNICALL Java_com_example_glessandbox_NDKSandbox_update(JNIEnv* env, jobject obj)
{
	static int frame;
	frame++;
}

}

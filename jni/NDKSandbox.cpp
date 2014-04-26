#include <jni.h>
#include <gles2/gl2.h>
#include <gles2/gl2ext.h>

extern "C" {

JNIEXPORT void JNICALL Java_com_example_glessandbox_NDKSandbox_update(JNIEnv* env, jobject obj, jint program)
{
	static int frame;
	frame++;

	glUseProgram(program);
	glUniform1i(glGetUniformLocation(program, "sampler1"), 0);
	glUniform1i(glGetUniformLocation(program, "sampler2"), 1);
	glUniform1f(glGetUniformLocation(program, "time"), (float)frame / 60.0f);
}

}

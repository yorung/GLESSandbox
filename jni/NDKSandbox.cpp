#include <jni.h>
#include <gles2/gl2.h>
#include <gles2/gl2ext.h>

#include <vector>

#define dimof(x) (sizeof(x) / sizeof(x[0]))

GLuint vbo;
GLuint gProgram;

struct Vert {
	float x;
	float y;
};

static const Vert vert[] = {
	{-1, -1},
	{1, -1},
	{-1, 1},
	{1, 1},
};

extern "C" {

JNIEXPORT void JNICALL Java_com_example_glessandbox_NDKSandbox_init(JNIEnv* env, jobject obj)
{
	glClearColor(0, 0, 0, 1);

	glGenBuffers(1, &vbo);
	glBindBuffer(GL_ARRAY_BUFFER, vbo);
	glBufferData(GL_ARRAY_BUFFER, sizeof(vert), vert, GL_STATIC_DRAW);

	jclass myview = env->FindClass("com.example.glessandbox.MyView");
	jmethodID method = env->GetStaticMethodID(myview, "createProgram", "(Ljava/lang/String;)I");
	if (method == 0) {
		return;
	}
	gProgram = env->CallStaticIntMethod(myview, method, env->NewStringUTF("water"));

	method = env->GetStaticMethodID(myview, "loadTexture", "(Ljava/lang/String;)I");
	glActiveTexture(GL_TEXTURE0);
	env->CallStaticIntMethod(myview, method, env->NewStringUTF("rose.jpg"));
	glActiveTexture(GL_TEXTURE1);
	env->CallStaticIntMethod(myview, method, env->NewStringUTF("autumn.jpg"));
}

JNIEXPORT void JNICALL Java_com_example_glessandbox_NDKSandbox_update(JNIEnv* env, jobject obj)
{
	static int frame;
	frame++;

	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	glUseProgram(gProgram);
	glUniform1i(glGetUniformLocation(gProgram, "sampler1"), 0);
	glUniform1i(glGetUniformLocation(gProgram, "sampler2"), 1);
	glUniform1f(glGetUniformLocation(gProgram, "time"), (float)frame / 60.0f);

	glBindBuffer(GL_ARRAY_BUFFER, vbo);

	int mPositionHandle = glGetAttribLocation(gProgram, "vPosition");
	glEnableVertexAttribArray(mPositionHandle);
	glVertexAttribPointer(mPositionHandle, 2, GL_FLOAT, GL_FALSE, sizeof(Vert), (void*)0);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, dimof(vert));
	glDisableVertexAttribArray(mPositionHandle);
}

}

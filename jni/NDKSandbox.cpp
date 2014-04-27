#include <jni.h>
#include <gles2/gl2.h>
#include <gles2/gl2ext.h>

#define dimof(x) (sizeof(x) / sizeof(x[0]))

GLuint vbo;

static const float coords[] = {
	-1, -1,
	1, -1,
	-1, 1,
	1, 1,
};

static const int COORDS_PER_VERTEX = 2;
static const int vertexCount = dimof(coords) / COORDS_PER_VERTEX;
static const int vertexStride = COORDS_PER_VERTEX * 4;

static void init()
{
	glGenBuffers(1, &vbo);
	glBindBuffer(GL_ARRAY_BUFFER, vbo);
	glBufferData(GL_ARRAY_BUFFER, sizeof(coords), coords, GL_STATIC_DRAW);
}

extern "C" {

JNIEXPORT void JNICALL Java_com_example_glessandbox_NDKSandbox_update(JNIEnv* env, jobject obj, jint program)
{
	static int frame;
	if (frame == 0) {
		init();
	}
	frame++;

	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	glUseProgram(program);
	glUniform1i(glGetUniformLocation(program, "sampler1"), 0);
	glUniform1i(glGetUniformLocation(program, "sampler2"), 1);
	glUniform1f(glGetUniformLocation(program, "time"), (float)frame / 60.0f);

	glBindBuffer(GL_ARRAY_BUFFER, vbo);

	int mPositionHandle = glGetAttribLocation(program, "vPosition");
	glEnableVertexAttribArray(mPositionHandle);
	glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GL_FLOAT, GL_FALSE, vertexStride, (void*)0);
	glDrawArrays(GL_TRIANGLE_STRIP, 0, vertexCount);
	glDisableVertexAttribArray(mPositionHandle);
}

}

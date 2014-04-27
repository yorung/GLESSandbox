LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := NDKSandbox
LOCAL_SRC_FILES := NDKSandbox.cpp
LOCAL_LDLIBS    := -lGLESv2

LOCAL_C_INCLUDES += D:\android-ndk-r9d/sources/cxx-stl/stlport/stlport

include $(BUILD_SHARED_LIBRARY)

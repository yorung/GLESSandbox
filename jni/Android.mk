LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := NDKSandbox
LOCAL_SRC_FILES := NDKSandbox.cpp

include $(BUILD_SHARED_LIBRARY)

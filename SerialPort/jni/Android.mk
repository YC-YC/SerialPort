LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

#������־ģ��
LOCAL_LDLIBS    := -lm -llog -landroid

LOCAL_MODULE    := SerialPort
LOCAL_SRC_FILES := SerialPort.cpp

include $(BUILD_SHARED_LIBRARY)

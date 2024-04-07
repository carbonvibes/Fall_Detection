LOCAL_PATH := ..
include $(CLEAR_VARS)
LOCAL_MODULE := Child
LOCAL_CFLAGS += -DMODEL=Child -DNUMST=1 -DNCSTATES=0 -DHAVESTDIO -DMODEL_HAS_DYNAMICALLY_LOADED_SFCNS=0 -DCLASSIC_INTERFACE=0 -DALLOCATIONFCN=0 -DTID01EQ=0 -DONESTEPFCN=1 -DTERMFCN=1 -DMULTI_INSTANCE_CODE=0 -DINTEGER_CODE=0 -DMT=0 -DSTACK_SIZE=64 -D__MW_TARGET_USE_HARDWARE_RESOURCES_H__ -DRT -D_USE_TARGET_UDP_ -DPORTABLE_WORDSIZES -DDRIVER_ANDROID_AUDIOCAPTURE -DDRIVER_ANDROID_AUDIOPLAYBACK 
LOCAL_CPPFLAGS := -std=c++11
LOCAL_SRC_FILES := coder_posix_time.c DAHostLib_Network.c DAHostLib_rtw.c ert_main.c Child.c Child_data.c rtGetInf.c rtGetNaN.c rt_nonfinite.c androidinitialize.c driver_android_accelerometer.c driver_android_audiofileread.c driver_android_audiocapture.c driver_android_audioplayback.c audio_engine.cpp audio_player.cpp audio_recorder.cpp driver_android_button.c driver_android_gyroscope.c driver_android_location.c driver_android_datadisplay.c linuxUDP.c 
LOCAL_C_INCLUDES += C:/Users/srira/DOCUME~1/MATLAB/FALL_D~1 C:/PROGRA~1/MATLAB/R2023a/toolbox/eml/EXTERN~1/timefun C:/PROGRA~3/MATLAB/SUPPOR~1/R2023a/toolbox/target/SUPPOR~1/android/include C:/PROGRA~1/MATLAB/R2023a/toolbox/shared/spc/src_ml/extern/include C:/Users/srira/DOCUME~1/MATLAB/FALL_D~1/CHILD_~1 C:/PROGRA~1/MATLAB/R2023a/extern/include C:/PROGRA~1/MATLAB/R2023a/simulink/include C:/PROGRA~1/MATLAB/R2023a/rtw/c/src C:/PROGRA~1/MATLAB/R2023a/rtw/c/src/ext_mode/common C:/PROGRA~1/MATLAB/R2023a/rtw/c/ert 
LOCAL_LDLIBS  +=  -llog -ldl -lOpenSLES -latomic
include $(BUILD_SHARED_LIBRARY)

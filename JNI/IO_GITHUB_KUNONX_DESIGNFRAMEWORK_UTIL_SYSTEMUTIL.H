#include <jni.h>

#ifndef _Included_io_github_kunonx_DesignFramework_util_SystemUtil
#define _Included_io_github_kunonx_DesignFramework_util_SystemUtil
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jstring JNICALL Java_io_github_kunonx_DesignFramework_util_SystemUtil_getOS
  (JNIEnv *, jclass);

JNIEXPORT jstring JNICALL Java_io_github_kunonx_DesignFramework_util_SystemUtil_getSimpleOSType
  (JNIEnv *, jclass);

JNIEXPORT jstring JNICALL Java_io_github_kunonx_DesignFramework_util_SystemUtil_getPlatformType
  (JNIEnv *, jclass);

JNIEXPORT jstring JNICALL Java_io_github_kunonx_DesignFramework_util_SystemUtil_getNativeInterfaceExtensionName
  (JNIEnv *, jclass, jstring);

#ifdef __cplusplus
}
#endif
#endif

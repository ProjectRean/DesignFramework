#include <windows.h>
#include <iostream>
#include "io_github_kunonx_DesignFramework_util_SystemUtil.h"
#include "jni_md.h"
#include <jni.h>

namespace Util
{
	class SystemUtil
	{
	public:
		static BOOL GetWindowsVersion(DWORD& dwMajor, DWORD& dwMinor, DWORD& dwServicePack)
		{
			/*if (!GetWindowsVersion(dwMajor, dwMinor))
				return FALSE;
				*/
			static DWORD dwServicePackCache = ULONG_MAX;
			if (ULONG_MAX != dwServicePackCache)
			{
				dwServicePack = dwServicePackCache;
				return TRUE;
			}

			const int nServicePackMax = 10;
			OSVERSIONINFOEX osvi;
			DWORDLONG dwlConditionMask = 0;

			ZeroMemory(&osvi, sizeof(OSVERSIONINFOEX));
			osvi.dwOSVersionInfoSize = sizeof(OSVERSIONINFOEX);
			VER_SET_CONDITION(dwlConditionMask, VER_SERVICEPACKMAJOR, VER_EQUAL);

			for (int i = 0; i < nServicePackMax; ++i)
			{
				osvi.wServicePackMajor = i;
				if (VerifyVersionInfo(&osvi, VER_SERVICEPACKMAJOR, dwlConditionMask))
				{
					dwServicePack = dwServicePackCache = i;
					return TRUE;
				}
			}
			return FALSE;
		}
		__declspec(deprecated("** this is a deprecated function **")) static std::string GetSimpleOSDenomination()
		{
			OSVERSIONINFO os;
			ZeroMemory(&os, sizeof(OSVERSIONINFOW));
			os.dwOSVersionInfoSize = sizeof(OSVERSIONINFOW);
			GetVersionEx(&os);
			std::string ret = "Windows ";
			if (os.dwMajorVersion == 10)
			{
				ret += "10";
			}
			else if (os.dwMajorVersion == 6)
			{
				if (os.dwMinorVersion == 3)
					ret += "8.1";
				else if (os.dwMinorVersion == 2)
					ret += "8";
				else if (os.dwMinorVersion == 1)
					ret += "7";
				else
					ret += "Vista";
			}
			else if (os.dwMajorVersion == 5)
			{
				if (os.dwMinorVersion == 2)
					ret += "XP SP2";
				else if (os.dwMinorVersion == 1)
					ret += "XP";
			}
			else
			{
				ret = "Unknown OS";
			}
			return ret.c_str();
		}

		JNIEXPORT jstring JNICALL Java_io_github_kunonx_DesignFramework_util_SystemUtil_getOS(JNIEnv *env, jclass clazz)
		{
			std::string os = SystemUtil::GetSimpleOSDenomination();
			std::string *def = new std::string("Unknown OS");
			if (os.compare(*def))
			{
				return env->NewStringUTF("Unknown");
			}
			else
			{
				return env->NewStringUTF(os.c_str());
			}
		}

		JNIEXPORT jstring JNICALL Java_io_github_kunonx_DesignFramework_util_SystemUtil_getSimpleOSType(JNIEnv *env, jclass clazz)
		{
			return env->NewStringUTF("Unknown");
		}

		JNIEXPORT jstring JNICALL Java_io_github_kunonx_DesignFramework_util_SystemUtil_getPlatformType(JNIEnv *env, jclass clazz)
		{
			return env->NewStringUTF("Unknown");
		}

		JNIEXPORT jstring JNICALL Java_io_github_kunonx_DesignFramework_util_SystemUtil_getNativeInterfaceExtensionName(JNIEnv *env, jclass clazz, jstring str)
		{
			return env->NewStringUTF("Unknown");
		}
	};
}

int main()
{
	// Test Methods
	std::string s = Util::SystemUtil::GetSimpleOSDenomination();
	printf(s.c_str());
}
#ifndef __MAIN_H__
#define __MAIN_H__
/* Java native interface */
#include <jni.h>

/* C/C++ libraries */
#include <ctime>
#include <memory>
#include <string>
#include <thread>
#include <fstream>
#include <stdio.h>
#include <direct.h>
#include <iostream>
#include <windows.h>
#include <synchapi.h>
#include <sysinfoapi.h>


/* Application libraries */
#include "libs/hddID.h"
#include "libs/biosuuid.h"
#include "libs/logFile.h"
#include "libs/appOps.h"

/* Java auto-generated libraries */
#include "java_headers/ids_IDsCollector.h"

/* Java exception class path */
#define JAVA_EXCEPTION_PATH "java/lang/Exception"

/*  To use this exported function of dll, include this header
 *  in your project.
 */

#ifdef BUILD_DLL
    #define DLL_EXPORT __declspec(dllexport)
#else
    #define DLL_EXPORT __declspec(dllimport)
#endif


#ifdef __cplusplus
extern "C"
{
#endif

void DLL_EXPORT SomeFunction(const LPCSTR sometext);

#ifdef __cplusplus
}
#endif

#endif // __MAIN_H__

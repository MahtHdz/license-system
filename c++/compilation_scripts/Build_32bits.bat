@ECHO OFF
TITLE App Compilation

ECHO ###########################
ECHO #   STARTING OPERATIONS   #
ECHO ###########################

SET JNILIB_PATH= -I"C:\Program Files\Java\jdk-14.0.2\include" 
SET JNILIB_WIN32_PATH=-I"C:\Program Files\Java\jdk-14.0.2\include\win32" 
SET PROJECT_PATH=C:\Users\maht_\OneDrive\Documentos\GitHub\licenseSystem\c++\
SET MINGW_GCC_PATH="C:\Program Files\CodeBlocks\MinGW\bin\x86_64-w64-mingw32-g++.exe"

SET DLL_NAME=SysIDs
SET FLAGS= -Wall -DBUILD_DLL -g
SET MAIN_OBJ_OUTPUT_PATH=%PROJECT_PATH%obj\32Bits\
SET LIBS_OBJ_OUTPUT_PATH=%PROJECT_PATH%obj\32Bits\libs\
SET DLL_FLAGS= -shared -Wl,--output-def=%PROJECT_PATH%bin\32Bits\lib%DLL_NAME%.def -Wl,--out-implib=%PROJECT_PATH%bin\32Bits\lib%DLL_NAME%.a -Wl,--dll -luser32 -m32

SET FILES[0]=appOps.cpp
SET FILES[1]=biosuuid.cpp
SET FILES[2]=hddID.cpp
SET FILES[3]=logFile.cpp
SET FILES[4]=main.cpp

SET OBJS[0]=appOps.o
SET OBJS[1]=biosuuid.o
SET OBJS[2]=hddID.o
SET OBJS[3]=logFile.o
SET OBJS[4]=main.o

SET OBJS_ABSOLUTE_PATHS[0]=%LIBS_OBJ_OUTPUT_PATH%%OBJS[0]%
SET OBJS_ABSOLUTE_PATHS[1]=%LIBS_OBJ_OUTPUT_PATH%%OBJS[1]%
SET OBJS_ABSOLUTE_PATHS[2]=%LIBS_OBJ_OUTPUT_PATH%%OBJS[2]%
SET OBJS_ABSOLUTE_PATHS[3]=%LIBS_OBJ_OUTPUT_PATH%%OBJS[3]%
SET OBJS_ABSOLUTE_PATHS[4]=%MAIN_OBJ_OUTPUT_PATH%%OBJS[4]%

Setlocal EnableDelayedExpansion
ECHO( 
ECHO BUILDING 32-Bits DLL
ECHO( 
FOR /L %%i IN (0, 1, 4) DO (
    ECHO BUILDING !FILES[%%i]! OBJ...
    IF NOT "!FILES[%%i]!" == "main.cpp" (
        @!MINGW_GCC_PATH!!FLAGS!!JNILIB_PATH!!JNILIB_WIN32_PATH!-c !PROJECT_PATH!libs\!FILES[%%i]! -o !LIBS_OBJ_OUTPUT_PATH!!OBJS[%%i]!
    ) ELSE (
        @!MINGW_GCC_PATH!!FLAGS!!JNILIB_PATH!!JNILIB_WIN32_PATH!-c !PROJECT_PATH!!FILES[%%i]! -o !MAIN_OBJ_OUTPUT_PATH!!OBJS[%%i]!
    )
    ECHO DONE.  
)
ECHO BUILDING DLL...
@%MINGW_GCC_PATH%%DLL_FLAGS% %OBJS_ABSOLUTE_PATHS[0]% %OBJS_ABSOLUTE_PATHS[1]% %OBJS_ABSOLUTE_PATHS[2]% %OBJS_ABSOLUTE_PATHS[3]% %OBJS_ABSOLUTE_PATHS[4]% -o %PROJECT_PATH%bin\32Bits\%DLL_NAME%.dll
ECHO DONE.
ECHO( 
ECHO ###########################
ECHO #   OPERATIONS COMPLETED  #
ECHO ###########################
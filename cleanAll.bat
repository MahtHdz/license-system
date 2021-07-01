@ECHO OFF
Setlocal EnableDelayedExpansion
TITLE CLEAN ALL
CLS
ECHO. 
ECHO ###########################
ECHO #     CLEANING DATA...    #
ECHO ###########################
ECHO.
SET PATHS[0]=c++/bin/64Bits
SET PATHS[1]=../32Bits
SET PATHS[2]=../../obj/64Bits
SET PATHS[3]=libs
SET PATHS[4]=../../32Bits
SET PATHS[5]=libs
SET PATHS[6]=../../../../java/IDService/res

SET FILE2DEL=main.o

:: DELETE MULTIPLE FILES
FOR /L %%i IN (0, 1, 6) DO (
    CD /D !PATHS[%%i]!
    IF %%i EQU 2 ( 
        DEL /q %FILE2DEL% 
    ) ELSE IF %%i EQU 4 ( 
        DEL /q %FILE2DEL% 
    ) ELSE ( 
        DEL /q *.*
    )
)

CD /D ..
ECHO. 
ECHO ###########################
ECHO #   OPERATIONS COMPLETED  #
ECHO ###########################

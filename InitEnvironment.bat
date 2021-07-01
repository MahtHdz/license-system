@ECHO OFF
TITLE INIT_ENV
CLS
ECHO.
:: ###### GENERAL DEFINITIONS ######
SET CURRENT_PATH=%cd%
SET FILENAME=keystoreInfo.json
SET BANNER=res/banner
SET BASE64_PATH=res/TextToBase64/TextToBase64.java
:: ###### KEYSTORE DEFINITIONS ######
SET KEY_ALG=RSA
SET KEY_SIZE=2048
SET KEY_VALIDITY=365
SET KEYTOOL_FLAGS=-genkey -keystore keystore.jks -alias client -keyalg %KEY_ALG% -validity %KEY_VALIDITY% -keysize %KEY_SIZE%

FOR /F "tokens=* delims=" %%x in (%BANNER%) DO echo %%x

ECHO STARTING JOBS...
ECHO.
ECHO ===================
ECHO ^| *** WARNING *** ^|
ECHO ===================
ECHO.
ECHO OBTAINING JAVA_HOME VARIABLE FROM SYSTEM ENVIRONMENT VARIABLES . . .    
:: 
SET "JDK_PATH=%JAVA_HOME%"
:: PATH EXAMPLES:
::  
::
IF NOT DEFINED JDK_PATH (
ECHO.
ECHO PATH NOT FOUND!
ECHO FOR MORE INFORMATION SEE THE COMMENTS ON THIS FILE.
EXIT
) ELSE (
ECHO.
ECHO PATH FOUND!
ECHO %JDK_PATH%
ECHO.
ECHO.
)
SET KEYTOOL_PATH="%JDK_PATH%\keytool.exe"
SET /p KEYSTORE_ALIAS=Set an alias for your keystore: 
ECHO.
ECHO ALIAS: %KEYSTORE_ALIAS%
::
ECHO.
CD /D java/IDService/res
@%KEYTOOL_PATH% %KEYTOOL_FLAGS%
CD /D ../../..
SETLOCAL enabledelayedexpansion
:WHILE
ECHO.
CALL :getPassword KEYSTORE_PASS "Retype your password: "
CALL :getPassword PASS_CONFIRMATION "Confirm your password: "
IF "%KEYSTORE_PASS%" NEQ "%PASS_CONFIRMATION%" (
    GOTO :WHILE
)
javac %BASE64_PATH%
FOR /F "tokens=* USEBACKQ" %%F IN (`java "%BASE64_PATH%" %KEYSTORE_PASS%`) DO (
SET ENCODED_PASS=%%F
)
ECHO %ENCODED_PASS%
ECHO {"alias":"%KEYSTORE_ALIAS%", "pass":"%ENCODED_PASS%"} > java/IDService/res/%FILENAME%
:: java  "%BASE64_PATH%" %KEYSTORE_PASS% >> %FILENAME%
CLS
ECHO.
ECHO.
ECHO ALIAS AND PASSWORD SAVED IN java/IDService/res/%FILENAME%
ECHO.
:ARCH
ECHO.
ECHO SELECT YOUR ARCHITECTURE:
ECHO  1^) 64 BITS
ECHO  2^) 32 BITS
SET /p OP="> "
SET FLAG=0
CLS 
IF "%OP%" EQU "1" (
    @"%CURRENT_PATH%\c++\compilation_scripts\Build_64Bits.bat" %CURRENT_PATH%
    SET FLAG=1
)
IF "%OP%" EQU "2" (
    @"%CURRENT_PATH%\c++\compilation_scripts\Build_32Bits.bat" %CURRENT_PATH%
    SET FLAG=1
)
IF %FLAG% EQU 0 GOTO :ARCH
GOTO :EOF

::############################ PASSWORD HANDLER ############################
::------------------------------------------------------------------------------
:: Masks user input and returns the input as a variable.
:: Password-masking code based on http://www.dostips.com/forum/viewtopic.php?p=33538#p33538
::
:: Arguments: %1 - the variable to store the password in
::            %2 - the prompt to display when receiving input
::------------------------------------------------------------------------------
:getPassword
set "_password="

:: We need a backspace to handle character removal
for /f %%a in ('"prompt;$H&for %%b in (0) do rem"') do set "BS=%%a"

:: Prompt the user 
set /p "=%~2" <nul 

:keyLoop
:: Retrieve a keypress
set "key="
for /f "delims=" %%a in ('xcopy /l /w "%~f0" "%~f0" 2^>nul') do if not defined key set "key=%%a"
set "key=%key:~-1%"

:: If No keypress (enter), then exit
:: If backspace, remove character from password and console
:: Otherwise, add a character to password and go ask for next one
if defined key (
    if "%key%"=="%BS%" (
        if defined _password (
            set "_password=%_password:~0,-1%"
            set /p "=!BS! !BS!"<nul
        )
    ) else (
        set "_password=%_password%%key%"
        set /p "="<nul
    )
    goto :keyLoop
)
echo/

:: Return password to caller
set "%~1=%_password%"
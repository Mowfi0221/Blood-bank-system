@REM ----------------------------------------------------------------------------
@REM Maven Wrapper / Runner for Windows
@REM ----------------------------------------------------------------------------
@echo off
setlocal

if "%JAVA_HOME%"=="" (
    if exist "C:\Program Files\Java\jdk-21" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-21"
    )
)

if exist "C:\Users\Monika\apache-maven-3.9.6\bin\mvn.cmd" (
    "C:\Users\Monika\apache-maven-3.9.6\bin\mvn.cmd" %*
) else (
    mvn %*
)

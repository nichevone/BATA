set "dd=%DATE:~0,2%"

set "ts=%TIME: =0%"
set "hh=%ts:~0,2%"
set "min=%ts:~3,2%"

set "TIME=%dd%.%hh%.%min%
echo %TIME%

set "JAR_PATH=../out/artifacts/VoiceChat_jar"
set "JAR_NAME=VoiceChat_v1.1.1-beta"

jpackage --app-version %TIME% ^
--name %JAR_NAME% ^
--input %JAR_PATH% ^
--main-jar %JAR_NAME%.jar ^
--type exe ^
--win-dir-chooser ^
--icon %JAR_PATH%/icon.ico

pause

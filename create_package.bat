set "dd=%DATE:~0,2%"

set "ts=%TIME: =0%"
set "hh=%ts:~0,2%"
set "min=%ts:~3,2%"

set "time=%dd%.%hh%.%min%
echo %time%

jpackage --app-version %time% ^
--name VoiceChat_v1.1.0-beta ^
--input VoiceChat_jar ^
--main-jar VoiceChat_v1.1.0-beta.jar ^
--type exe ^
--win-dir-chooser ^
--icon VoiceChat_jar/icon.ico

pause
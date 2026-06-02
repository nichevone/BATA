# Date/time: DD.HH.MM (Linux format)
dd=$(date +%d)
hh=$(date +%H)
min=$(date +%M)
TIME="${dd}.${hh}.${min}"
echo "$TIME"

JAR_PATH="../out/artifacts/VoiceChat_jar"
JAR_NAME="VoiceChat_v1.1.1-beta"

jpackage \
  --app-version "$TIME" \
  --name "$JAR_NAME" \
  --input "$JAR_PATH" \
  --main-jar "$JAR_NAME.jar" \
  --type deb \
  --icon "$JAR_PATH/icon.ico"

read -n 1 -s -r -p "Press any key to continue..."
echo ""
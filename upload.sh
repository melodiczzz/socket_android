OUTPUT=$(curl --upload-file ./app/build/outputs/apk/debug/app-debug.apk https://transfer.sh/socket.zip)
CODE=$(echo "$OUTPUT" | sed 's/.*sh//; s/socket.*//' | sed 's/\(^.\)\|\(.$\)//g')
OUTPUT="https://transfer.sh/get/$CODE/socket.zip"
echo $OUTPUT

curl -X POST \
-H "X-Parse-Application-Id: 96IF9A8Dqw4cSdJo4LgdTkdD9s2EziWKyf61hUPk" \
-H "X-Parse-REST-API-Key: NAjHO2RUsJrvCYTpcKhD3koNKYawDCpIHWgPd2nT" \
-H "Content-Type: application/json" \
-d '{"link":"'"$OUTPUT"'", "size": "0", "downloadId" : "false", "finished": "false"}' \
https://parseapi.back4app.com/classes/DownloadList
echo $OUTPUT
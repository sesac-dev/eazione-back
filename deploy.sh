#실행중인 8080컨테이너 탐색
EXIST_GITCHAN=$(sudo docker compose -p docshelper-8080 -f compose-blue.yml ps | grep Up)

#실행중인 8080컨테이너 없으면
if [ -z "$EXIST_GITCHAN" ]; then
        echo "8080 컨테이너 실행"
        sudo docker compose -p docshelper-8080 -f compose-blue.yml up -d --force-recreate
        BEFORE_COLOR="8081"
        AFTER_COLOR="8080"
        BEFORE_PORT=8081
        AFTER_PORT=8080
else
        echo "8081 컨테이너 실행"
        sudo docker compose -p docshelper-8081 -f compose-green.yml up -d --force-recreate
        BEFORE_COLOR="8080"
        AFTER_COLOR="8081"
        BEFORE_PORT=8080
        AFTER_PORT=8081
fi

echo "${AFTER_COLOR} server up(port:${AFTER_PORT})"

# 2
for cnt in `seq 1 10`;
do
    echo "Wait for server ...(${cnt}/10)";
    UP=$(curl -s http://127.0.0.1:${AFTER_PORT}/api/health-check)
    if [ "${UP}" != "OK" ]; then
        sleep 10
        continue
    else
        break
    fi
done

if [ $cnt -eq 10 ]; then
    echo "Server Error!!"
    exit 1
fi

# 3
sudo sed -i "s/${BEFORE_PORT}/${AFTER_PORT}/" /etc/nginx/conf.d/service-url.inc
sudo nginx -s reload
echo "Deploy Completed!!"

# 4
echo "$BEFORE_COLOR server down(port:${BEFORE_PORT})"
sudo docker compose -p ulvan-${BEFORE_COLOR} -f docker-compose.ulvan${BEFORE_COLOR}.yml down

# 5
sudo docker image prune -f
---
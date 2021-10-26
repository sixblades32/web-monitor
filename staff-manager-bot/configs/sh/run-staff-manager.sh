#!/bin/sh

gradle clean build -p web-monitor/staff-manager

export DISCORD_BOT_TOKEN=
export DICTIONARY_SERVICE_URL=http://localhost:8015/api/dictionary
export KAFKA_BOOTSTRAP_SERVERS=localhost:9092
export TWITTER_MONITOR_URL=http://localhost:8030/twitter-monitor
export MONITOR_USER_NAME=admin
export MONITOR_USER_PASSWORD=admin

nohup java -jar web-monitor/staff-manager-bot/build/libs/staff-manager-bot*.jar &

#!/bin/sh

gradle clean build -p web-monitor/staff-manager

export DISCORD_BOT_TOKEN=
export DICTIONARY_SERVICE_URL=http://localhost:8015/api/dictionary
export KAFKA_BOOTSTRAP_SERVERS=localhost:9092

nohup java -jar web-monitor/twitter-monitor/build/libs/staff-manager*.jar &

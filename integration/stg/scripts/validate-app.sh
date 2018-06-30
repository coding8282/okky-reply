#!/usr/bin/env bash

MAX_RETRY=10
CHECK_INTERVAL='5s'
counter=0

while [ true ]
do
    ((counter++))
    if [ "$(curl -s http://localhost:9004)" = 'okky-reply service' ]
    then
	    echo "Service is up and running successfully after $counter attempts!"
        exit 0
    else
        checkRetryExceeded
        echo "Check service is running? - $counter/$MAX_RETRY"
        sleep $CHECK_INTERVAL
    fi
done

function checkRetryExceeded(){
    if [ $counter -gt $MAX_RETRY ]
    then
        echo "It's considered fail to start service."
        exit 1
    fi
}

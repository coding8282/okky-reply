#!/usr/bin/env bash

sudo chmod +x /home/ec2-user/okky-reply-1.0.0.jar
sudo ln -sf /home/ec2-user/okky-reply-1.0.0.jar /etc/init.d/okky-reply
sudo service okky-reply start
sleep 10s
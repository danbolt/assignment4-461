#!/bin/bash

clean='clean'
all='all'

for var in "$@"
do
  if [ "$var" = "$clean" ] ; then
     rm ./*.class
  elif [ "$var" = "$all" ] ; then
     javac -cp ".;custom.jar" ClientBroadcaster.java
     javac -cp ".;custom.jar" ConferenceClient.java
     javac -cp ".;custom.jar" ClientReceiver.java
     javac -cp ".;custom.jar" ReceiverWindow.java
     javac -cp ".;custom.jar" ChatServer.java
     javac -cp ".;custom.jar" ClientConnection.java
     javac -cp ".;custom.jar" ClientInfo.java
  fi

done

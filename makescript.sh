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
  fi

done

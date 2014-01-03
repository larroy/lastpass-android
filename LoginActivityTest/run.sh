#!/bin/bash
ant build
adb push bin/LoginActivityTest.jar /data/local/tmp
adb shell uiautomator runtest LoginActivityTest.jar

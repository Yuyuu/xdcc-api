#!/bin/bash

./gradlew clean stage
foreman start -e dev.env
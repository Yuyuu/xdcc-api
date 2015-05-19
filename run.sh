#!/usr/bin/env bash

./gradlew stage
foreman start -p 8089 -e dev.env

#!/usr/bin/env bash

current_dir="$( cd -- "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"
jar_name="FileServer.jar"
config_name="config.json"

echo "Running at: $current_dir"

java -jar "$current_dir/$jar_name" launch --config "$current_dir/$config_name"

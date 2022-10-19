@echo off

set current_dir=%~dp0
set jar_name="FileServer.jar"
set config_name="config.json"

echo Running at: %current_dir%

java -jar "%current_dir%\%jar_name%" launch --config "%current_dir%\%config_name%"

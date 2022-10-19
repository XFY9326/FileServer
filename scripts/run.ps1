$current_dir = Split-Path -Parent $MyInvocation.MyCommand.Definition
$jar_name = "FileServer.jar"
$config_name="config.json"

echo "Running at: $current_dir"

java -jar "$current_dir\$jar_name" launch --config "$current_dir\$config_name"

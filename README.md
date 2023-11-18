# FileServer
Simple Http file server based on Ktor and supported basic Http auth.  
Might be useful if you want to share or upload simple files in terminal.  
Just for fun.

## Build
```shell
# Only single jar
# Output: build/distributions
./gradlew assembleJar

# Executable script bundle
# Output: build/distributions
./gradlew assemble
```

## Usage
```
java -jar FileServer-<VERSION>.jar [OPTIONS] COMMAND [ARGS]...

Options:
  -h, --help  Show this message and exit

Commands:
  launch          Launch file server
  default-config  Generate default config
```

```shell
# For more details, see help
java -jar FileServer-<VERSION>.jar -h

java -jar FileServer-<VERSION>.jar launch -h

java -jar FileServer-<VERSION>.jar default-config -h
```

There are some simple scripts under /scripts that can also be used to start FileServer.jar.

## Check server
```shell
# Should return 'Server OK!'
curl http://localhost:8080
```

## Access attentions

- Not allow accessing hidden file or directory. 
- Not allow accessing file out of root path.

## Upload files
```shell
# Upload file and create dir
curl --upload-file <FILE> "http://localhost:8080/f/<PATH>/<FILE>"

# Upload multiple files
curl -F "<UPLOAD_FILE_NAME>=@<LOCAL_FILE_PATH>" -F "<UPLOAD_FILE_NAME>=@<LOCAL_FILE_PATH>" "http://localhost:8080/f/<PATH>/"

# Basic Http auth
curl -u <USER>:<PASSWORD> --upload-file <FILE> "http://localhost:8080/f/<PATH>/<FILE>"
```

## Download files
```shell
# Download file
wget "http://localhost:8080/f/<PATH>/<FILE>"
curl -O "http://localhost:8080/f/<PATH>/<FILE>"

# Download multiple files
wget --no-parent -r --reject "index.html*" "http://localhost:8080/f/<PATH>/"

# Basic Http auth
wget --user <USER> --password <PASSWORD> "http://localhost:8080/f/<PATH>/<FILE>"
curl -u <USER>:<PASSWORD> -O "http://localhost:8080/f/<PATH>/<FILE>"
```

## List files
```shell
# Show file names under the <PATH> in text
curl "http://localhost:8080/l/<PATH>"

# Show file url under the <PATH> in text
# Param value doesn't matter
curl "http://localhost:8080/l/<PATH>?url=1"
# Or shorter
curl "http://localhost:8080/l/<PATH>?u=1"

# Only show files under the <PATH> in text
# Param value doesn't matter
curl "http://localhost:8080/l/<PATH>?file=1"
# Or shorter
curl "http://localhost:8080/l/<PATH>?f=1"
```

## Download multiple files with url list
```shell
# You can use 'file' and 'url' together to download multiple files
curl -s "http://localhost:8080/l/<PATH>\?f\=1\&u\=1" | wget -i -
```

## View file list in Browser
Open http://localhost:8080/f/ in browser.  
Support list files, upload files and download file.  

## Config
```shell
# Generate default config file
java -jar FileServer-<VERSION>.jar default-config

# Launch with config
java -jar FileServer-<VERSION>.jar launch --config config.json
```

Default config:  
```json
{
    "host": "0.0.0.0",
    "port": 8080,
    "root": "files",
    "allowAnonymous": true,
    "allowAnonymousDownload": true,
    "users": {
    },
    "callLogging": true,
    "ignoreUploadDownloadIOException": true,
    "noCache": true
}
```
| Field                           | Description                                                                                                   |
|---------------------------------|---------------------------------------------------------------------------------------------------------------|
| host                            | Server host                                                                                                   |
| port                            | Server port                                                                                                   |
| root                            | File server root path                                                                                         |
| allowAnonymous                  | Close basic http auth                                                                                         |
| allowAnonymousDownload          | Download without basic http auth<br/>No effect if allowAnonymous=true<br/>http://\<HOST\>/f/\<PATH\>/\<FILE\> |
| users                           | UserName:String to Password:String dict<br/>Useless if allowAnonymous=true                                    |
| callLogging                     | Output request details in the log                                                                             |
| ignoreUploadDownloadIOException | Ignore IOException thrown by upload and download                                                              |
| noCache                         | Declare not using cache in the Cache control of the http header                                               |

**Attention: Blank or empty UserNames and Passwords are not allowed to use!**

# FileServer
Simple Http file server based on Ktor and supported basic Http auth.  
Might be useful if you want to share or upload simple files in terminal.  
Just for fun.

## Build
```
# Only single jar
# Output: build/distributions
./gradlew assembleJar

# Execuable script bundle
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

```
# For more details, see help
java -jar FileServer-<VERSION>.jar -h

java -jar FileServer-<VERSION>.jar launch -h

java -jar FileServer-<VERSION>.jar default-config -h
```

## Check server
```
# Should return 'Server OK!'
curl http://localhost:8080
```

## Upload file
```
# Upload file and create dir
curl --upload-file <FILE> http://localhost:8080/f/<PATH>/<FILE>

# Basic Http auth
curl -u <USER>:<PASSWORD> --upload-file <FILE> http://localhost:8080/f/<PATH>/<FILE>
```

## Download file
```
# Download file
wget http://localhost:8080/f/<PATH>/<FILE>

# Download multiple files
wget --no-parent -r --reject "index.html*" http://localhost:8080/f/<PATH>/

# Basic Http auth
wget --user <USER> --password <PASSWORD> http://localhost:8080/f/<PATH>/<FILE>
```

## View file
```
# Show file names under <PATH> in text
curl http://localhost:8080/l/<PATH>

# Show file url under <PATH> in text
curl http://localhost:8080/l/<PATH>?url=1
```

## Browser
Open http://localhost:8080/f/ in browser.  
Support list files, upload files and download file.  

## Config
```
# Generate default config file
java -jar FileServer-<VERSION>.jar default-config
```
Default config:  
```json
{
    "host": "0.0.0.0",
    "port": 8080,
    "root": "files",
    "allowAnonymous": true,
    "users": {
    }
}
```
| Field | Description |
| ----- | ----- |
| host | Server host |
| port | Server port |
| root | File server root path |
| allowAnonymous | Close basic http auth |
| users | UserName:String to Password:String dict<br/>Useless if allowAnonymous=true |

**Attention: Blank or empty UserNames and Passwords are not allowed to use!**

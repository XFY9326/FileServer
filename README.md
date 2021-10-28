# FileServer
Simple Http file server based on Ktor and supported basic Http auth.  
Might be useful if you want to share or upload simple files in terminal.  
Just for fun.

## Build
```
# Only single jar
# Output: build/distributions
./gradlew assembleJar

# Build with execuable script
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

# Basic Http auth
wget --user <USER> --password <PASSWORD> http://localhost:8080/f/<PATH>/<FILE>
```

## View file
In terminal:  
```
# Only return file names under <PATH> in text
curl http://localhost:8080/l/<PATH>

# Show file url in text
curl http://localhost:8080/l/<PATH>?url=1
```

In browser:  
Open http://localhost:8080/l/ in browser

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
| allowAnonymous | Don't need basic http auth |
| users | UserName:String to Password:String dict<br/>Useless if allowAnonymous=true |

**Attention: Blank or empty UserNames and Passwords are not allowed to use!**

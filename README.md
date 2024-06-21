# software_production
This is a repository for Software Production

## How to run the project
1. compile all the files with make
```
make
```
2. run the server
```
java -cp :./lib/sqlite-jdbc-3.46.0.0.jar:./lib/slf4j-api-2.0.13.jar server.Server
```
3. run the client
```
java client.Client
```
4. (optional) install dependencies
```
pip install -r server/requirements.txt
```
5. (optional) run the convert.py for converting the voice to text
```
python server/convert.py
```
or
```
python3 server/convert.py
```

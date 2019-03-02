# rss-reader
it's simple rss-reader

# Requirements
  * Java 1.8
  
# application.properties 
The application.properties file contains two properties   
   1. storage.base.path - path to the directory where the rss data will be stored
   2. config.base.path - path to the directory where RSS configurations will be stored  
  
# Building process

Before it please change properties in application.properties

Windows:
  1. Go to folder with sources  
  2. Run **gradlew.bat fatJar**
  
Linux/macOS:
  1. Go to folder with sources  
  2. Run **./gradlew fatJar**

Using gradle
  1. Go to folder with sources  
  2. Run **gradle clean fatJar**

# Running
Go to build/libs folder. Created from build section jar file is here.  
Run  jar using java -jar <jar_file_name>

# Working
The following lines will appear:   
> Welcome to simple RSS reader  
Restoring previously created rss  
*****Available commands*****  
  	add [url] - add new rss chanel  
	  configure [url] - change settings of rss  
	  turn-on [url] - turn on rss chanel  
	  turn-off [url] - turn off rss chanel  
	  on-list - list of running rss  
	  off-list - list of disabled rss  
	  help - get list available commands  
	  exit - close program
Enter command:  

enter command and enjoy

all:
	javac client/*.java
	javac server/*.java

clean:
	rm -f client/*.class
	rm -f server/*.class

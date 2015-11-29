# Võrgurakendused 2.2

# Installation

Check out the project and build the application using **maven** and run the v22.jar at **/target** folder.

	mvn clean package

# Start application

By default, application will run at port 8080. The rest API is available under context path **rest/**. After starting application you can access it at:

	http://localhost:8080/rest/path/to/resource

## Default configuration

	java -jar v22.jar
	
## Custom configuration

The custom configuration file can be placed anywhere visible to the application and may have any name.

	java -jar -Dconfig="/path/to/custom.properties" v22.jar

# Stop application

To stop the application just run the command:

	java -jar v22.jar stop
	
If you started the application using a **custom configuration** file, use the same configuration to stop the application.

	java -jar -Dconfig="/path/to/custom.properties" v22.jar stop


# Configuration

The application can be configured using a custom properties file. It can be placed anywhere visible to the application and may have any name.

The available properties are:

### Database

v22 uses **MariaDB** database. Click [here](https://mariadb.com/kb/en/mariadb/getting-installing-and-upgrading-mariadb/) for how to install it.
In my.ini or my.cnf under [mysqld] add: character-set-server=utf8

* **db.url** - the database url
* **db.username** - the database username
* **db.password** - the database password for given username

### Server

* **server.port** - the port that server starts.
* **command.listener.port** - the port used to listening for commands to be executed on server. Currently only shutdown command is available.

#### Mobile ID

* **mobileID.endpoint** - DigiDocService endpoint address. 
* **mobileID.serviceName** - Name of your service, must be agreed upon with Mobile ID service provider. Maximum length 20 characters. 
* **mobileID.namespace.prefix** - Prefix to use in SOAP messages. Default value is **dig**
* **mobileID.namespace.uri** - DigiDocService WSDL URI. Default value is **http://www.sk.ee/DigiDocService/DigiDocService_2_3.wsdl**
* **mobileID.messageToDisplay** - Extra message that can be displayed on user's phone prior to entering the PIN code. Maximum size is 40 bytes. Default value is **eKoolikott**

# Generating the public private key pair in a keystore for v22

Create a new keystore file:

* A valid certificate is required. A self-signed certificate can be generated with the following commands (don't use self-signed certificates for production environment): 
	* `openssl req -nodes -new -keyout server.pem -newkey rsa:2048 > server.csr`
	* `openssl x509 -req -days 1095 -in server.csr -signkey server.pem -out server.crt`
* Create a pkcs12 file:
	* `openssl pkcs12 -export -in server.crt -inkey server.pem -out server.p12 -name exampleAlias -password pass:examplePassword` Replace **exampleAlias** and **examplePassword**. 
* Create the keystore:
	* `keytool -importkeystore -deststorepass exampleStorePass -destkeypass exampleKeyPass -destkeystore server.keystore -srckeystore server.p12 -srcstoretype PKCS12 -srcstorepass examplePassword -alias exampleAlias` Replace **exampleAlias**, **examplePassword**, **exampleStorePass**, **exampleKeyPass**.
	
Add all keystore configurations to **custom.properties**. From the example keytool command the configuration would be: 
```
	keystore.filename=server.keystore
	keystore.password=exampleStorePass
	keystore.signingEntityID=exampleAlias
	keystore.signingEntityPassword=exampleKeyPass
```

### Exporting the generated public key certificate

To export the public key certificate, which can then be given to the material providers (publishers):
	*`keytool -export -keystore server.keystore -alias exampleAlias -file EkoolikottPublicKeyCert.cer`
	
This EkoolikottPublicKeyCert.cer file can then be used by the material providers to verify, if the user should have the access to the required resource.

# Mobile ID authentication setup

By default **mobileID.endpoint** is set to the Test DigiDocService at `https://www.openxades.org:9443/DigiDocService`. 
Set **mobileID.endpoint** to `https://digidocservice.sk.ee/DigiDocService` to use the real endpoint. 
Set **mobileID.serviceName** to the name that has been agreed upon with the Mobile ID provider. 

Fuse and RHSSO Integration
============================================

Assumptions
---------------

* You have the RHSSO installed and runing on port 8080
* The master realm has a client named "fuse-client"
* The master real has a user with the role "user"


Preparing Fuse
-----------

Getting a fresh installation of JBoss Fuse 6.3, we need to install the keycloak features with the following commands:

* features:addurl mvn:org.keycloak/keycloak-osgi-features/2.5.5.Final-redhat-1/xml/features
* features:install keycloak
* features:install keycloak-jetty9-adapter

Intalling the example
-----------

* Build this project with Maven
* osgi:install mvn:org.rchies.fuse/fuse-keycloak-example/3.0.0

Running the Example
-------------------

* Call the unsecured rest interface and you should be fine with a 200 response code
  `curl http://localhost:8484/rest/unsecured/resource/message`

* Call the secured rest interface and you should receive a 401
  `curl http://localhost:8484/rest/secured/resource/message`
  
Now we are going to get a valid token at RHSSO and call again the secured service

RESULT=`curl --data "grant_type=password&client_id=fuse-client&username=rchies&password=rchies" http://localhost:8080/auth/realms/master/protocol/openid-connect/token`

TOKEN=`echo $RESULT | sed 's/.*access_token":"//g' | sed 's/".*//g'`

curl http://localhost:8484/rest/secured/resource/message -H "Authorization: bearer $TOKEN"

Now yopu should receive a 200 response

1) To build this project use "mvn install" and get "contract-lock-example-8.0.0.jar" in the target folder

2) Create a fabric profile that has feature-camel as its parent

3) Add the following features to the profile: fabric-camel, camel-servlet, camel-core, jms, camel-blueprint, activemq-camel 

4) Add the following artifcats to the profile

    mvn:org.chies.fuse/contract-lock-example/8.0.0 (you will have it in your ~/.m2 after running "mvn install" at the step 1)
    
5) Create a container into the fabric using some mq profile, and start it. The goal is having a broker running with 61616 port.

6) Create two containers into the fabric using the profile created on step 2, and start them

6) After started, call the REST URL below through any REST Client browser plugin. This action is going to put a message on the queue with a property ContractID equals to the last URL parameter, and the route will wait 15s before droping (consuming) the message from the queue. In this meantime, the contract fhandle simulation, if you call again the URL with the same ID, you are going ro receive an exception (ItemLockedException). You can even call the URL related to the othe container (probably using the port 8183)

    http://localhost:8182/contract-lock/rest/contract/111

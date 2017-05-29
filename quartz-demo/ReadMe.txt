1) To build this project use "mvn install" and get "quartz-demo-7.0.0.jar" in the target folder

2) Create a fabric profile that has feature-camel as its parent

3) Add the following features to the profile: fabric-camel, jdbc, camel-sql, camel-core, camel-quartz2, camel-blueprint 

4) Add the following artifcats to the profile

    mvn:mysql/mysql-connector-java/5.1.27 (you need to have it in your ~/.m2)
    mvn:org.apache.servicemix.bundles/org.apache.servicemix.bundles.c3p0/0.9.1.2_1 (you need to have it in your ~/.m2)
    mvn:org.rchies.fuse.quartz/quartz-demo/7.0.0 (you will have it in your ~/.m2 after running "mvn install" at the step 1)
    
5) Create a container into the fabric using the profile created, and start it

6) After started, the container will start the route and print the following log

    "Hello. I've been fired by Quartz2"
  
7) Start a new container with the same profile and be sure it'll not start to print the same log. We are handling a stateful quartz Job here, and we don't want to have a unique Job running at the same time in multiple containers.

8) Stop the first container you created, and so you will see that the second container started will start to print the log, bringing us the job failover







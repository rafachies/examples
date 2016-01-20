Steps to reproduce:

mvn install to generate the three artifacts:

  -- dosgi-interfaces: Holds the service interface
  -- dosgi-services: Holds the service implementation
  -- dosgi-consumer: Holds a camel route that calls the service
  
The idea is to deploy the dosgi-services in one fabric container, and the dosgi-consumer in a separated container. When the dosgi-services bundle is started, the service is registered in zookeper, so it can be called by reference in any other bundle, in any other container.
I order to simulate this:

- Install JBoss Fuse
- Create the fabric
- Create the profile "dosgiservice"
         parent profile: "feature-dosgi"
         features: "spring", "camel-spring" and "fabric-dosgi"
         bundles: dosgi-interfaces and dosgi-services
- Create and start a container called "contanier-dosgi-services" with the profile "dosgiservice"
- Create the profile "dosgiexample"
         parent profile: "feature-camel"
         features: "camel-core", "camel-spring", "fabric-dosgi", "camel-jetty" and "fabric-camel"
         bundles: dosgi-interfaces and dosgi-consumer
- Create and start a container called "contanier-dosgi-example" with the profile "dosgiexample"

A route should be started in the container "contanier-dosgi-example", creating a rest endpoint at http://localhost:9090/dosgi/calculate.
Call this URL on a browser, and check the result.

To prove the service was called remotely, you can just stop the container "contanier-dosgi-services". If you call the REST endpoint again, it'll not work.


Steps to reproduce:

mvn install to generate the three artifacts:

  *dosgi-interfaces: Holds the service interface
  *dosgi-services: Holds the service implementation
  *dosgi-consumer: Holds a camel route that calls the service
  
The idea is to deploy the dosgi-services in one fabric container, and the dosgi-consumer in a separated container. When the dosgi-services bundle is started, the service is registered in zookeper, so it can be called by reference in any other bundle, in any other container.
In order to reproduce:

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

If more than one bundle/container has the service exported, the consumer will create a proxy to one of them, and so using it for all calls. In other words, there is no load balance between the bundles/containers.

If you want to create a new version of the service in a container, you can change the ranking attribute on the osgi:service. The greatest ranking will be called by the consumers.

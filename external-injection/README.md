external-injection
==================

Test case project to test Weld injecting beans from other deployment module on Wildfly/EAP.
What we are going to prove here is that in EAP 6 the injectiont (Weld) between classes in different deployments does not work.

This project generates two artifacts, artifact-jar.jar and artifact-war.war.
To test it, just deploy first artifact-jar.jar, and after deploy artfact-war.war.

In EAP 6 the second deploy will fail because one WAR class can't inject a bean that is deployed  on another JAR

Caused by: org.jboss.weld.exceptions.DeploymentException: WELD-001408 Unsatisfied dependencies for type [Foo] with qualifiers [@Default] at injection point [[field] @Inject private com.redhat.asouza.test.cdi.test_01.test_web.TestWeb.foo]

If we do the same deploy on Wildfly 8.0.0.GA it works normally. We can even test the injection in runtime calling the REST service of WAR:

http://localhost:8080/artifact-war/rest/test/say

... and a message will be logged on console

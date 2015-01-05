package org.rchies.dsconnection;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/ejb")
public class EJBResource {


	@GET
	@Path("/ssl")
	public void callWithSSL() throws Exception {
		System.setProperty("javax.net.ssl.trustStore", "/tmp/truststore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "123456");
		final Properties jndiProperties = new Properties();                   
		jndiProperties.put("java.naming.factory.url.pkgs", "org.jboss.ejb.client.naming");
		jndiProperties.put("org.jboss.ejb.client.scoped.context", "true");
		jndiProperties.put("endpoint.name", "testendpoint");				
		jndiProperties.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "true");
		jndiProperties.put("remote.clusters", "ejb");
		jndiProperties.put("remote.cluster.ejb.connect.options.org.xnio.Options.SSL_ENABLED", "true");
		jndiProperties.put("remote.cluster.ejb.connect.options.org.xnio.Options.SSL_STARTTLS", "true");
		jndiProperties.put("remote.cluster.ejb.username", "ejbuser");
		jndiProperties.put("remote.cluster.ejb.password", "1qaz@WSX");
		jndiProperties.put("remote.cluster.ejb.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
		jndiProperties.put("remote.cluster.ejb.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "false");
		jndiProperties.put("remote.cluster.ejb.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS", "JBOSS-LOCAL-USER");
		jndiProperties.put("remote.connections", "node1");
		jndiProperties.put("remote.connection.node1.host", "localhost");
		jndiProperties.put("remote.connection.node1.port", " 4447");
		jndiProperties.put("remote.connection.node1.username", "ejbuser");
		jndiProperties.put("remote.connection.node1.password", "1qaz@WSX");
		jndiProperties.put("remote.connection.node1.connect.options.org.xnio.Options.SSL_STARTTLS", "true");
		jndiProperties.put("remote.connection.node1.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
		jndiProperties.put("remote.connection.node1.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "false");
		jndiProperties.put("remote.connection.node1.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS", "JBOSS-LOCAL-USER");
		Context context = new InitialContext(jndiProperties);
		
		final Context ejbNamingContext = (Context) context.lookup("ejb:");
		
		IMyEJB ejb = (IMyEJB) ejbNamingContext.lookup("/ejbserver//MyEJB!org.rchies.dsconnection.IMyEJB");
		ejb.go();
		ejb.go();
		ejb.go();
		ejb.go();
		ejb.go();
		ejb.go();
		ejb.go();
		ejb.go();
	}

}


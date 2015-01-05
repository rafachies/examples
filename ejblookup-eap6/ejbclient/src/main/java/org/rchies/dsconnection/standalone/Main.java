package org.rchies.dsconnection.standalone;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.rchies.dsconnection.IMyEJB;

public class Main {	

	public static void main(String[] args) throws Exception {
		System.setProperty("javax.net.ssl.trustStore", "/tmp/truststore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "123456");
		final Hashtable<String, String> jndiProperties = new Hashtable<String, String>();                   
        jndiProperties.put("java.naming.factory.url.pkgs", "org.jboss.ejb.client.naming");
        Context context = new InitialContext(jndiProperties);
        IMyEJB ejb = (IMyEJB) context.lookup("ejb:/ejbserver//MyEJB!org.rchies.dsconnection.IMyEJB");
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

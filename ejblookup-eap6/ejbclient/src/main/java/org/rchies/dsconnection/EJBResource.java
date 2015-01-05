package org.rchies.dsconnection;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/ejb")
public class EJBResource {
	

	@GET
	@Path("/ssl")
	public void callWithSSL() throws Exception {
		final Hashtable<String, String> properties = new Hashtable<String, String>();                   
        properties.put("java.naming.factory.url.pkgs", "org.jboss.ejb.client.naming");
        Context context = new InitialContext(properties);
        IMyEJB ejb = (IMyEJB) context.lookup("ejb:/dsconnection//MyEJB!org.rchies.dsconnection.IMyEJB");
        ejb.go();
	}
	
}


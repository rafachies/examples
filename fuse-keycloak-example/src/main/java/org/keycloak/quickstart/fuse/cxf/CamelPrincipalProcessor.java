package org.keycloak.quickstart.fuse.cxf;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;


/**
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 */
public class CamelPrincipalProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
//		HttpServletRequest req = exchange.getIn().getBody(HttpServletRequest.class);
//		KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) req.getUserPrincipal();
//		if (keycloakPrincipal == null) {
//			exchange.getOut().setBody("Hello anonymous. The URL called is not protected");
//		} else {
//			AccessToken accessToken = keycloakPrincipal.getKeycloakSecurityContext().getToken();
//			String username = accessToken.getPreferredUsername();
//			String fullName = accessToken.getName();
//			exchange.getOut().setBody("Hello " + username + "! Your full name is " + fullName + ".");
//		}
	}
}

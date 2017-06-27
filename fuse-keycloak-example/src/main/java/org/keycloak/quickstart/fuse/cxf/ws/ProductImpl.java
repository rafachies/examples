package org.keycloak.quickstart.fuse.cxf.ws;

import javax.jws.WebService;
import javax.xml.ws.Holder;

@WebService(serviceName = "ProductService", endpointInterface = "org.keycloak.quickstart.fuse.cxf.ws.Product")
public class ProductImpl implements Product {

    public void getProduct(Holder<String> productId, Holder<String> name) {
        
        if (productId.value == null || productId.value.length() == 0) {
            throw new RuntimeException();
        } else if (productId.value.trim().equals("1")) {
            name.value = "IPad";
        } else if (productId.value.trim().equals("2")) {
            name.value = "IPhone";
        } else {
            throw new RuntimeException();
        }
    }

}

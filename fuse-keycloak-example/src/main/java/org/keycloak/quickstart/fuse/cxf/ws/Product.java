package org.keycloak.quickstart.fuse.cxf.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService
public interface Product {

    @RequestWrapper(localName = "GetProduct", className = "GetProduct")
    @ResponseWrapper(localName = "GetProductResponse", className = "GetProductResponse")
    @WebMethod(operationName = "GetProduct")
    public void getProduct(
            @WebParam(mode = WebParam.Mode.INOUT, name = "productId")
            javax.xml.ws.Holder<String> productId,
            @WebParam(mode = WebParam.Mode.OUT, name = "name")
            javax.xml.ws.Holder<String> name
    );
}

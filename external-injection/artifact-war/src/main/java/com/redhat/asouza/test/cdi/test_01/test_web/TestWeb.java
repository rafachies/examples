package com.redhat.asouza.test.cdi.test_01.test_web;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.redhat.asouza.test.cdi.Foo;

@Path("test")
public class TestWeb {

	@Inject
	private Foo foo;
	
	@GET
	@Path("say")
	public void sayIt() {
		foo.sayHello("Web Project");
	}
}

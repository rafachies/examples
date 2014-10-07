package com.redhat.asouza.test.cdi;

import javax.enterprise.inject.Produces;

public class BeanProducer {

	//@Produces
	public Foo createFoo(){
		return new Foo();
	}
}

package org.rchies.dsconnection;

import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.jboss.ejb3.annotation.Clustered;

@Stateless
@Clustered
@Remote(IMyEJB.class)
public class MyEJB implements IMyEJB {

	public void go() {
		System.out.println("yeah! remote ejb was called!");
	}
}

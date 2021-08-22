package org.jboss.cdi.tck.tests.lookup.clientProxy.unproxyable.interceptor;

import jakarta.enterprise.context.Dependent;

@Fish
@Dependent
public class Tuna {

	public String name;

	private Tuna(){
	}

	public Tuna(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
	
}

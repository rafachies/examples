package br.com.redhat.dosgi;


public class CalculatorServiceBean implements CalculatorService {

	public Integer sum(Integer x, Integer y) {
		System.out.println("Hey, I was called by another container. DOSGI works!");
		return x + y;
	}

   
}

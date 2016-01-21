package br.com.redhat.dosgi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalculatorServiceBean implements CalculatorService {

	private Logger logger = LoggerFactory.getLogger(CalculatorServiceBean.class);
	
	public Integer sum(Integer x, Integer y) {
		logger.info("Hey, I was called by another container. DOSGI works!");
		return x + y;
	}

   
}

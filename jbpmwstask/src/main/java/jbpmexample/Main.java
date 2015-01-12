package jbpmexample;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.process.workitem.webservice.WebServiceWorkItemHandler;
import org.jbpm.runtime.manager.impl.RuntimeEnvironmentBuilder;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.manager.context.EmptyContext;


public class Main {

	public static void main(String[] args) {
		new Main().launch();
	}

	public void launch() {
		
		RuntimeEnvironment environment = RuntimeEnvironmentBuilder.Factory.get()
    			.newEmptyBuilder()
                .addAsset(ResourceFactory.newClassPathResource("MyProcess.bpmn2"), ResourceType.BPMN2)
                .get();
		
		RuntimeManagerFactory runtimeManagerFactory = RuntimeManagerFactory.Factory.get();
		RuntimeManager runtimeManager = runtimeManagerFactory.newSingletonRuntimeManager(environment);
		RuntimeEngine runtimeEngine = runtimeManager.getRuntimeEngine(EmptyContext.get());
		KieSession ksession = runtimeEngine.getKieSession();
		WebServiceWorkItemHandler webServiceWorkItemHandler = new WebServiceWorkItemHandler(ksession);
		ksession.getWorkItemManager().registerWorkItemHandler("WSTask", webServiceWorkItemHandler);
		int[] parameters = {2, 5};
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("numbers", parameters);
		ksession.startProcess("com.sample.bpmn.MyProcess", params);
		runtimeManager.disposeRuntimeEngine(runtimeEngine);
	}


}

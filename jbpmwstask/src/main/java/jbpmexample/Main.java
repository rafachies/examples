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

import com.smartbear.SampleTestClass;


public class Main {

	public static void main(String[] args) {
		new Main().launchWithPojo();
	}

//	public void launch() {
//		RuntimeEnvironment environment = RuntimeEnvironmentBuilder.Factory.get()
//    			.newEmptyBuilder()
//                .addAsset(ResourceFactory.newClassPathResource("MyProcess.bpmn2"), ResourceType.BPMN2)
//                .get();
//		
//		RuntimeManagerFactory runtimeManagerFactory = RuntimeManagerFactory.Factory.get();
//		RuntimeManager runtimeManager = runtimeManagerFactory.newSingletonRuntimeManager(environment);
//		RuntimeEngine runtimeEngine = runtimeManager.getRuntimeEngine(EmptyContext.get());
//		KieSession ksession = runtimeEngine.getKieSession();
//		WebServiceWorkItemHandler webServiceWorkItemHandler = new WebServiceWorkItemHandler(ksession);
//		ksession.getWorkItemManager().registerWorkItemHandler("WSTask", webServiceWorkItemHandler);
//		int[] parameters = {2, 5};
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("numbers", parameters);
//		ksession.startProcess("com.sample.bpmn.MyProcess", params);
//		runtimeManager.disposeRuntimeEngine(runtimeEngine);
//	}


	public void launchWithPojo() {
		RuntimeEnvironment environment = RuntimeEnvironmentBuilder.Factory.get()
    			.newEmptyBuilder()
                .addAsset(ResourceFactory.newClassPathResource("PojoProcess.bpmn2"), ResourceType.BPMN2)
                .get();
		RuntimeManagerFactory runtimeManagerFactory = RuntimeManagerFactory.Factory.get();
		RuntimeManager runtimeManager = runtimeManagerFactory.newSingletonRuntimeManager(environment);
		RuntimeEngine runtimeEngine = runtimeManager.getRuntimeEngine(EmptyContext.get());
		KieSession ksession = runtimeEngine.getKieSession();
		WebServiceWorkItemHandler webServiceWorkItemHandler = new WebServiceWorkItemHandler(ksession);
		ksession.getWorkItemManager().registerWorkItemHandler("WSTask", webServiceWorkItemHandler);
		Map<String, Object> params = new HashMap<String, Object>();
		SampleTestClass sampleTestClass = createRequest();
		params.put("request", sampleTestClass);
		ksession.startProcess("com.sample.bpmn.PojoProcess", params);
		runtimeManager.disposeRuntimeEngine(runtimeEngine);
	}

	private SampleTestClass createRequest() {
		SampleTestClass sampleTestClass = new SampleTestClass();
		sampleTestClass.setName("Chies");
		sampleTestClass.setX(2);
		sampleTestClass.setY(3);
		return sampleTestClass;
	}
}

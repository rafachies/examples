package org.rchies.demo.jmssync;

import java.util.UUID;

import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Client {

	//We expect here that we have a external broker running
	private static final String BROKER_URL = "tcp://localhost:61616";

	public static void main(String[] args) throws Exception {
		new Client().start();
	}
	
	public void start () throws Exception {
		String correlationId = UUID.randomUUID().toString();
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
		Connection connection = connectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// Create reference to the queues
		Queue requestQueue = session.createQueue("RequestQueue");
		Queue responseQueue = session.createQueue("ResponseQueue");
		MessageProducer messageProducer = session.createProducer(requestQueue);
		TextMessage requestMessage = session.createTextMessage();
		// Send the correlationId to the server
		requestMessage.setText(correlationId);
		//This is great. Consumer does not need to know the responseQueue in a static way
		requestMessage.setJMSReplyTo(responseQueue);
		requestMessage.setJMSCorrelationID(correlationId);
		messageProducer.send(requestMessage);
		//Prepare to consume the response. Always the same queue, but wait for specific correlationId
		MessageConsumer consumer = session.createConsumer(responseQueue, "JMSCorrelationID = '" + correlationId + "'");
		TextMessage responseMessage = (TextMessage) consumer.receive();
		System.out.println("[Producer] Response received: " + responseMessage.getText());
	}
}

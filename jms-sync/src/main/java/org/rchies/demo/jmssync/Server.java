package org.rchies.demo.jmssync;

import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Server {

	//We expect here that we have a external broker running
	private static final String BROKER_URL = "tcp://localhost:61616";

	public static void main(String[] args) throws Exception {
		new Server().start();
	}

	public void start () throws Exception {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
		Connection connection = connectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue requestQueue = session.createQueue("RequestQueue");
		MessageConsumer consumer = session.createConsumer(requestQueue);
		while (true) {
			//Let's wait for a message in RequestQueue
			TextMessage requestMessage = (TextMessage) consumer.receive();
			System.out.println("[Consumer] received message: " + requestMessage.getText());
			TextMessage responseMessage = session.createTextMessage();
			//Send back the same message appended with acknowledge
			responseMessage.setText(requestMessage.getText() + " ack");
			//Echo the correlationId, since client is waiting this
			responseMessage.setJMSCorrelationID(requestMessage.getJMSCorrelationID());
			//We don't need to know the response queue, let's hear what client has to say
			MessageProducer messageProducer = session.createProducer(requestMessage.getJMSReplyTo());
			messageProducer.send(responseMessage);
		}
	}
}

package org.rchies.demo.jmsexample;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Main {

	public static void main(String[] args) throws Exception {
		System.setProperty("javax.net.ssl.keyStore", "/Users/rafaelchies/development/workshop-amq/amq-leste/conf/amq-client.ks");
		System.setProperty("javax.net.ssl.keyStorePassword", "password");
		System.setProperty("javax.net.ssl.trustStore", "/Users/rafaelchies/development/workshop-amq/amq-leste/conf/amq-client.ts");
		System.setProperty("javax.net.ssl.trustStorePassword", "password");
		new Main().launch(args);
	}

	private void launch(String[] parameters) throws Exception {
		try {
			String option = parameters[0];
			String clientID = parameters[1];
			String brokerURL = parameters[2];
			String destinationName = parameters[3];
			if (isQueueConsumer(option)) {
				boolean isExclusive = ((parameters.length == 5) && (parameters[4].equals("exclusive"))) ? true : false;
				consumeQueue(clientID, brokerURL, destinationName, isExclusive);
				return;
			}
			if (isTopicSubscriber(option)) {
				boolean isDurable = ((parameters.length == 5) && (parameters[4].equals("durable"))) ? true : false;
				consumeTopic(clientID, brokerURL, destinationName, isDurable);
				return;
			}
			if (isQueueProducer(option)) {
				String messageText = parameters[4];
				sendQueue(clientID, brokerURL, destinationName, messageText);
				return;
			}
			if (isTopicProducer(option)) {
				String messageText = parameters[4];
				sendTopic(clientID, brokerURL, destinationName, messageText);
				return;
			}
			if (isDLQSimulation(option)) {
				consumeAndThrows(clientID, brokerURL, destinationName);
				return;
			}
			invalidParameter();
		} catch (IndexOutOfBoundsException e) {
			invalidParameter();
		}
	}

	private boolean isQueueProducer(String option) {
		return "sendqueue".equals(option);
	}
	
	private boolean isDLQSimulation(String option) {
		return "DLQ".equals(option);
	}
	
	private boolean isTopicProducer(String option) {
		return "sendtopic".equals(option);
	}

	private boolean isQueueConsumer(String option) {
		return "consume".equals(option);
	}
	private boolean isTopicSubscriber(String option) {
		return "subscribe".equals(option);
	}

	private void sendQueue(String producerID, String brokerURL, String destinationName, String messageText) throws JMSException {
		Connection connection = createConnection(brokerURL, producerID);
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination requestQueue = session.createQueue(destinationName + "?jms.redeliveryPolicy.maximumRedeliveries=0");
		sendMessage(messageText, connection, session, requestQueue);
	}
	
	private void sendTopic(String producerID, String brokerURL, String destinationName, String messageText) throws JMSException {
		Connection connection = createConnection(brokerURL, producerID);
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination requestTopic = session.createTopic(destinationName);
		sendMessage(messageText, connection, session, requestTopic);
	}

	private void sendMessage(String messageText, Connection connection, Session session, Destination destination) throws JMSException {
		MessageProducer messageProducer = session.createProducer(destination);
		//messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		TextMessage requestMessage = session.createTextMessage();
		requestMessage.setText(messageText);
		messageProducer.send(requestMessage);
		connection.close();
		System.out.println("[Producer] Message sent: " + messageText);
	}
	
	private void consumeAndThrows(String clientID, String brokerURL, String queueName) throws JMSException {
		Connection connection = createConnection(brokerURL, clientID);
		Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
		Queue requestQueue = session.createQueue(queueName);
		MessageConsumer consumer = session.createConsumer(requestQueue);
		System.out.println("[Consumer] Waiting message on queue " + queueName);
		consumer.receive();
		System.out.println("Message received, but rolling back to simulate DLQ ... ");
		session.rollback();
		connection.close();
	}

	private void consumeQueue(String clientID, String brokerURL, String queueName, boolean isExclusive) throws JMSException {
		Connection connection = createConnection(brokerURL, clientID);
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		if (isExclusive) {
			System.out.println("[Consumer] Registering as Exclusive ...");
			queueName = queueName.concat("?consumer.exclusive=true");
		}
		Queue requestQueue = session.createQueue(queueName);
		MessageConsumer consumer = session.createConsumer(requestQueue);
		while (true) {
			System.out.println("[Consumer] Waiting for messages on queue " + queueName);
			TextMessage requestMessage = (TextMessage) consumer.receive();
			System.out.println("[Consumer] received a message: " + requestMessage.getText());
		}
	}

	private void consumeTopic(String clientID, String brokerURL, String topicName, boolean isDurable) throws JMSException {
		Connection connection = createConnection(brokerURL, clientID);
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic(topicName);
		MessageConsumer consumer = createTopicConsumer(isDurable, session, topic, connection, clientID);
		while (true) {
			System.out.println("[Consumer] Waiting for messages on topic " + topicName);
			TextMessage requestMessage = (TextMessage) consumer.receive();
			System.out.println("[Consumer] received a message: " + requestMessage.getText());
		}
	}

	private MessageConsumer createTopicConsumer(boolean isDurable, Session session, Topic topic, Connection connection, String subscriberID) throws JMSException {
		MessageConsumer consumer;
		if (isDurable) {
			System.out.println("[Consumer] Registering as Durable with ID: " + subscriberID);
			consumer = session.createDurableSubscriber(topic, subscriberID);
		} else {
			consumer = session.createConsumer(topic);
		}
		return consumer;
	}

	private Connection createConnection(String brokerURL, String clientID) throws JMSException {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);
		Connection connection = connectionFactory.createConnection();
		connection.setClientID(clientID);
		connection.start();
		return connection;
	}

	private void invalidParameter() {
		System.out.println("Wrong parameters. Parameters should be: <sendqueue|sendtopic|consume|subscribe|DLQ clientID brokerURL destinationName [durable|text]>");
	}
}

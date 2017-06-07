package org.chies.fuse;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContractExecutionChecker {
	
	private Logger logger = LoggerFactory.getLogger(ContractExecutionChecker.class);

	public void acquireLock(Integer contractId) throws Exception {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		Connection connection = connectionFactory.createConnection("admin", "admin");
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		try {
			Queue destination = session.createQueue("CONTRACTS");
			String contractIdHeader = "ContractID = " + contractId;
			QueueBrowser queueBrowser = session.createBrowser(destination, contractIdHeader);
			if (contractIsLocked(queueBrowser)) {
				throw new ItemLockedException("Contract is already locked by other service: " + contractId);
			} else {
				lockMessage(session, destination, contractId);
			}
		} finally {
			session.close();
			connection.close();
		}
	}
	
	public void releaseLock(Integer contractId) throws Exception {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
		Connection connection = connectionFactory.createConnection("admin", "admin");
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		try {
			Queue destination = session.createQueue("CONTRACTS");
			String contractIdHeader = "ContractID = " + contractId;
			MessageConsumer messageConsumer = session.createConsumer(destination, contractIdHeader);
			Message message = messageConsumer.receive();
			if (message == null) {
				logger.warn("Message not found to release, skiping it: " + contractId);
			} else {
				logger.info("Message found to realease, releasing it: " + contractId);
			}
		} finally {
			session.close();
			connection.close();
		}
	}

	private boolean contractIsLocked(QueueBrowser queueBrowser) throws JMSException {
		return queueBrowser.getEnumeration().hasMoreElements();
	}

	private void lockMessage(Session session, Destination destination, Integer contractId) throws JMSException {
		MessageProducer producer = session.createProducer(destination);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		TextMessage message = session.createTextMessage("anyvalue");
		message.setIntProperty("ContractID", contractId);
		producer.send(message);
	}

}

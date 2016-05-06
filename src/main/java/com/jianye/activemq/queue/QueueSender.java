package com.jianye.activemq.queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class QueueSender {
	
	private static final int SEND_NUM = 5;
	
	private static final String BROKER_URL = "tcp://192.168.84.178:61616";
	
	private static final String DESTINATION = "test_queue1";
	
	public static void sendMessage(Session session, MessageProducer producer) throws JMSException {
		String message  = "";
		TextMessage textMessage = null;
		for (int i = 0; i < SEND_NUM; i++) {
			message = "{\"state\": \"ok\", \"num\":" + i + "}";
			textMessage = session.createTextMessage(message);
			System.out.println(message);
			producer.send(textMessage);
		}
	}
	
	public static void run() throws JMSException {
		Connection connection = null;
		Session session = null;
		try {
			ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
			connection = factory.createConnection();
			connection.start();
			session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(DESTINATION);
			MessageProducer producer = session.createProducer(destination);
			// 设置持久化模式
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			sendMessage(session, producer);
			session.commit();
		} catch (JMSException e) {
			throw e;
		} finally {
			if (session != null) {
				session.close();
			}
			
			if (connection != null) {
				connection.close();
			}
		}
	}
	
	public static void main(String[] args) throws JMSException {
		QueueSender.run();
	}
}

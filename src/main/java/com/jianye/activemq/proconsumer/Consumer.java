package com.jianye.activemq.proconsumer;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Consumer {
	
	private static String brokerURL = "tcp://192.168.86.198:61616";
	
	private static transient ConnectionFactory factory;
	
	private static transient Connection connection;
	
	private static transient Session session;
	
	public Consumer() throws JMSException {
		factory = new ActiveMQConnectionFactory(brokerURL);
		connection = factory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}
	
	public void close() throws JMSException {
		if (connection != null) {
			connection.close();
		}
	}
	
	public static void main(String[] args) throws JMSException {
		Consumer consumer = new Consumer();
		for (String arg : args) {
			Destination destination = consumer.getSession().createTopic("STOCKS." + arg);
			MessageConsumer messageConsumer = consumer.getSession().createConsumer(destination);
			messageConsumer.setMessageListener(new Lisener());
		}
	}
	
	public Session getSession() {
		return session;
	}
}

package com.jianye.activemq.queue;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class QueueConsumer {
	
	private static final String BROKER_URL = "tcp://192.168.84.178:61616";
	
	private static final String DESTINATION = "test_queue1";
	
	public static void run() throws JMSException, InterruptedException {
		QueueConnection connection = null;
		QueueSession session = null;
		try {
			QueueConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
			connection = factory.createQueueConnection();
			connection.start();
			session = connection.createQueueSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			// Queue queue = session.createQueue(DESTINATION);
			Destination destination = session.createQueue(DESTINATION);
			// QueueReceiver receiver = session.createReceiver(queue);
			MessageConsumer consumer = session.createConsumer(destination);
			
			consumer.setMessageListener(new MessageListener() {
				
				public void onMessage(Message message) {
					if (message != null) {
						TextMessage textMessage = (TextMessage) message;
						try {
							System.out.println(textMessage.getText());
							textMessage.acknowledge();
						} catch (JMSException e) {
							e.printStackTrace();
						}
					}
				}
			});
			
			/*receiver.setMessageListener(new MessageListener() {
				
				public void onMessage(Message message) {
					if (message != null) {
						TextMessage textMessage = (TextMessage) message;
						try {
							System.out.println(textMessage.getText());
						} catch (JMSException e) {
							e.printStackTrace();
						}
					}
				}
			});
			*/
			Thread.sleep(100 * 1000);
			
			session.commit();
		} catch (JMSException e) {
			throw e;
		} catch (InterruptedException e) {
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
	
	public static void main(String[] args) throws JMSException, InterruptedException {
		QueueConsumer.run();
	}
}
package top.baimingru.tlqMsg.rcvfiles;

import com.tongtech.jms.jni.TlqException;
//import com.tlqmsg.rcvfiles.GateConf;
import java.io.PrintStream;
import java.util.Properties;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class TlqJmsReceiver {
	public static final String TLQ_MQ_GATE_PATH = "TLQ_MQ_GATE_PATH";
	public static Logger tlqToMQGateLogger = Logger.getLogger("tlq.to.mq.gate");
	ConnectionFactory tlqFactory = null;

	Connection tlqConn = null;

	MessageConsumer tlqMsgConsumer = null;
	Session tlqSession = null;
	Context tlqContext = null;

	Queue tlqQueue = null;
	int gateConf = 0;

	String TlqJMSQue = null;
	String MqJMSQue = null;

	boolean isstop = false;

	MsgManagment msgManagment = null;

	static {
		DOMConfigurator
				.configure("D:\\CTP Developer\\workspace\\TLQ_Msg_Management\\conf\\tlq_mq_gate_log.xml");
	}

	public TlqJmsReceiver(int gateConf, String SrcQ, String DestQ) {
		this.gateConf = gateConf;
		this.TlqJMSQue = SrcQ;
		this.MqJMSQue = DestQ;
		Thread.currentThread().setName(gateConf + ":" + SrcQ + "->" + DestQ);
		this.msgManagment = new MsgManagment(gateConf, DestQ);
	}

	public void ConnectToTlq() {
		String logInfo = null;

		Properties prop = new Properties();
//		while (true) {
			try {
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				prop.put("java.naming.factory.initial",
						"tongtech.jms.jndi.JmsContextFactory");
				prop.put("java.naming.provider.url", "tlq://93.6.61.191:10039");
				tlqToMQGateLogger
						.debug("TLQ  TlqContextFactory:tongtech.jms.jndi.JmsContextFactory");
				tlqToMQGateLogger
						.debug("TLQ  Provider_URL:tlq://93.6.61.191:10039");

				this.tlqContext = new InitialContext(prop);
				tlqToMQGateLogger.info("Tlq_gate TLQ InitialContext OK!");

				tlqToMQGateLogger.debug("TLQ TlqQueueConnectionFactory:"
						+ "RemoteConnectionFactory");

				this.tlqFactory = ((ConnectionFactory) this.tlqContext.lookup("RemoteConnectionFactory"));
				//this.tlqFactory = ((QueueConnectionFactory) this.tlqContext.lookup("RemoteConnectionFactory"));

				logInfo = "Tlq_gate TLQ Lookup the QueueConnectionFactory  \""
						+ "RemoteConnectionFactory" + "\" " + "successfully";
				tlqToMQGateLogger.info(logInfo);
				
				this.tlqQueue = ((Queue) this.tlqContext.lookup("GTEST_66_191"));
				// this.tlqQueue = null;
				System.out.println("**************");

				logInfo = "Tlq_to_mq_gate TLQ lookup Queue \"" + "GTEST_66_191"
						+ "\" OK!";
				tlqToMQGateLogger.info(logInfo);

				this.tlqConn = this.tlqFactory.createConnection();
				tlqToMQGateLogger
						.info("Tlq_to_mq_gate TLQ Create the QueueConnection successfully");
				//this.tlqConn.start();
				this.tlqSession = this.tlqConn.createSession(true, 1);
				tlqToMQGateLogger
						.info("Tlq_to_mq_gate TLQ createSession para \" true AUTO_ACKNOWLEDGE \" OK!");

				

				this.tlqMsgConsumer = this.tlqSession
						.createConsumer(this.tlqQueue);
				tlqToMQGateLogger
						.info("Tlq_to_mq_gate Create the Consumer successfully");

				this.tlqMsgConsumer.setMessageListener(new TlqMessageListener(
						this));
				tlqToMQGateLogger
						.info("Tlq_to_mq_gate Set the MessageListener successfully");

				this.tlqConn.start();
				tlqToMQGateLogger
						.info("Tlq_to_mq_gate QueueConnection start successfully");
				tlqToMQGateLogger
						.info("Tlq_to_mq_gate Start the MessageListener successfully");

				tlqToMQGateLogger
						.info("Tlq_to_mq_gate All Connect to TLQ OK ...");

				// break;
			} catch (Exception e) {
				System.out.println(e);
				if ((!(e instanceof TlqException))
						|| ((((TlqException) e).getErrorCode() != "C4063")
								&& (!((TlqException) e).getMessage().contains(
										"consumer is closed")) && (!((TlqException) e)
								.getMessage().contains(" Connection reset"))))
					// continue;
					// TlqException e1 = (TlqException) e;
					// tlqToMQGateLogger.error(e1.getMessage(), e1);
					tlqToMQGateLogger
							.error("Tlq_to_mq_gate Connect to TLQ Failed, 5s later will try it again");
				try {
					Thread.sleep(5000L);
				} catch (InterruptedException localInterruptedException1) {
				}
			}//catch
			// continue;

			/*
			 * tlqToMQGateLogger.error(e.getMessage(), e); try {
			 * Thread.sleep(5000L); } catch (InterruptedException e) {
			 * e.printStackTrace(); }
			 */
//		}//while
	}

	public void startGate() {
		try {
			ConnectToTlq();
		} catch (Exception e) {
			e.printStackTrace();
			tlqToMQGateLogger.error(e.getMessage(), e);
		}
	}

	public void processMessage(Message m) throws JMSException {
		if (m == null) {
			System.out.println("Received no message");
			tlqToMQGateLogger.error("Received null message");
		}

		this.msgManagment.ManagmentMsg(m);
		System.out.println("Tlq_gate GetMessageToMq OK!");
		tlqToMQGateLogger.info("Tlq_to_mq_gate sendMessageToMq OK!");
	}

	public int Reconnect() {
		try {
			Properties prop = new Properties();

			prop.put("java.naming.factory.initial",
					"tongtech.jms.jndi.JmsContextFactory");
			prop.put("java.naming.provider.url", "tlq://93.6.61.191:10024");

			this.tlqContext = new InitialContext(prop);

			System.out.println("The JMS Prg is start to Recieve Message...");

			this.tlqFactory = ((QueueConnectionFactory) this.tlqContext
					.lookup("RemoteConnectionFactory"));
			System.out
					.println("The JMS Prg Lookup the QueueConnectionFactory successfully");

			boolean transacted = false;
			this.tlqConn = this.tlqFactory.createConnection();
			System.out
					.println("The JMS Prg Create the QueueConnection successfully");

			this.tlqSession = this.tlqConn.createSession(true, 1);
			this.tlqQueue = ((Queue) this.tlqContext.lookup("GTEST_66_191"));

			this.tlqMsgConsumer = this.tlqSession.createConsumer(this.tlqQueue);
			System.out.println("The JMS Prg Create the Consumer successfully");
			this.tlqMsgConsumer
					.setMessageListener(new TlqMessageListener(this));
			System.out
					.println("The JMS Prg Set the MessageListener successfully");

			System.out
					.println("The JMS Prg Start the MessageListener successfully");

			System.out.println("The JMS Prg is Ready to Recieve Message...");
			return 0;
		} catch (NamingException e) {
			System.out.println("The prg catch the 'NamingException'");
			System.out.println(e);
			return 1;
		} catch (JMSException e) {
			System.out.println("The prg catch the 'JMSException'");
			System.out.println(e);
			return 1;
		} catch (Exception e) {
			System.out.println("The prg catch the 'Exception'");
			System.out.println(e);
		}
		return 1;
	}
}

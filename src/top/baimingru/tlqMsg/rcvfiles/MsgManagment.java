package top.baimingru.tlqMsg.rcvfiles;

import com.tongtech.jms.FileMessage;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.directory.InitialDirContext;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class MsgManagment
{
	public static final String TLQ_MQ_GATE_PATH = "TLQ_MQ_GATE_PATH";
	public static Logger tlqToMQGateLogger;
	protected QueueConnectionFactory factory = null;
	protected QueueConnection connection;
	protected QueueSession queueSession;
	protected TextMessage outMessage;
	protected QueueSender queueSender;
	protected QueueReceiver queueReceiver;
	private int gateConf = 0;
	private String MQJmsQue = null;

	static {
		DOMConfigurator.configure("D:\\CTP Developer\\workspace\\TLQ_Msg_Management\\conf\\tlq_mq_gate_log.xml");

		tlqToMQGateLogger = Logger.getLogger("tlq.to.mq.gate");
	}

	public MsgManagment(int gateConf, String DestQ) {
		this.MQJmsQue = DestQ;
		this.gateConf = gateConf;
	}

	public void ManagmentMsg(Message m) throws JMSException {
//		if (this.queueSender == null) {
//			tlqToMQGateLogger.info("queueSender is null");
//			throw new JMSException("queueSender is null");
//		}

		if ((m instanceof FileMessage)) {
			if (m != null) {
				FileMessage fileMsg = (FileMessage) m;
				System.out.println("Receive File: " + fileMsg.getFile());
			}
		} else {
			System.out.println("-------ERROR!!!!!-------");
		} 
	}

	public void receiveMessage() throws Exception {
		Enumeration enums = null;
		TextMessage txtMsg = (TextMessage) this.queueReceiver.receive();
		String txtstr = txtMsg.getText();
		System.out.println("Received TextMessage:" + txtstr);

		enums = txtMsg.getPropertyNames();
		while (enums.hasMoreElements()) {
			String key = (String) enums.nextElement();

			String value = txtMsg.getStringProperty(key);
			System.out.println(key + "==" + value);
		}
	}

}
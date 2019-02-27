package top.baimingru.tlqMsg.rcvfiles;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class TlqMessageListener implements MessageListener {
	TlqJmsReceiver tlqJmsRceiver;
	public static final String TLQ_MQ_GATE_PATH = "TLQ_MQ_GATE_PATH";
	public static Logger tlqToMQGateLogger = Logger.getLogger("tlq.to.mq.gate");

	static {
		DOMConfigurator
				.configure("D:\\CTP Developer\\workspace\\TLQ_Msg_Management\\conf\\tlq_mq_gate_log.xml");
	}

	public TlqMessageListener(TlqJmsReceiver tjr) {
		this.tlqJmsRceiver = tjr;
	}

	public void onMessage(Message m) {
		tlqToMQGateLogger.info("It is onMessage()...started");
		try {
			this.tlqJmsRceiver.processMessage(m);
			this.tlqJmsRceiver.tlqSession.commit();
		} catch (Exception e1) {
			tlqToMQGateLogger.error(e1.getMessage(), e1);
			try {
				this.tlqJmsRceiver.tlqSession.rollback();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
}

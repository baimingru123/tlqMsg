package top.baimingru.tlqMsg.rcvfiles;

//import com.tlqmsg.rcvfiles.InitGateConf;
import top.baimingru.tlqMsg.rcvfiles.Tlq_gate;
import top.baimingru.tlqMsg.rcvfiles.TlqJmsReceiver;
//import com.tlqmsg.rcvfiles.GateConf;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class Tlq_gate {

	public static final String TLQ_MQ_GATE_PATH = "TLQ_MQ_GATE_PATH";
	public static Logger loger;
	public static String ConfFile;
	private int tlq_gate = 0;
	private String SrcQ = "GTEST_66_191";
	private String DestQ = "LLLL";
//	private InitGateConf gateConf = null;

	static {
		
		DOMConfigurator.configure("D:\\CTP Developer\\workspace\\TLQ_Msg_Management\\conf\\tlq_mq_gate_log.xml");

		loger = Logger.getLogger("tlq.mq.gate");
		ConfFile = "D:\\CTP Developer\\workspace\\TLQ_Msg_Management\\conf\\tlq_mq_gate_log.xml";
	}

	public void Tlq_gate() {
//		Map q2qMap = new ConcurrentHashMap();
		loger.info("****************Come_into_tlq_gate****************");
//		q2qMap = (ConcurrentHashMap) this.tlq_gate.getSrcDestQueue();
//		Set set = q2qMap.entrySet();
//		Iterator i = set.iterator();
//		while (i.hasNext()) {
//			Map.Entry me = (Map.Entry) i.next();
//			loger.info("SrcQ:" + (String) me.getKey() + " " + "DestQ:" + (String) me.getValue());
			TlqJmsReceiver tlqJmsReceiver = new TlqJmsReceiver(this.tlq_gate, (String) SrcQ, (String) DestQ);
			tlqJmsReceiver.startGate();
			loger.info("Strat tlq_gate " + (String) SrcQ + "->" + (String) DestQ + " OK!");
//		}
	}

	public Tlq_gate() {
		try {
			loger.info("****************Welcome****************");
//			this.gateConf = new InitGateConf(ConfFile);
//			this.tlq_gate = this.gateConf.getTlq2MqGateConf();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TlqJmsReceiver tlqJmsReceiver = new TlqJmsReceiver(0, "", "");
		tlqJmsReceiver.startGate();
		//Tlq_gate gate = new Tlq_gate();
		//gate.Tlq_gate();
	}

}

package top.baimingru.tlqMsg.rcvfiles;

import java.io.PrintStream;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;

public abstract class JMSExceptionListener implements ExceptionListener
{
  TlqJmsReceiver oo;

  public JMSExceptionListener(TlqJmsReceiver o)
  {
    this.oo = o;
  }

  public void onException(JMSException e) {
    System.out.println("It is onException");
    e.printStackTrace();

    while (this.oo.Reconnect() != 0)
    {
      try
      {
        Thread.sleep(1000L);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
}

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.concurrent.CountDownLatch;

/**
 * @author phra
 *
 */
public class SuperJumperServer implements PROTOCOL_CONSTANTS {
	
	protected static final Hashtable<Integer,User> users = new Hashtable<Integer,User>();
	private static int ID = 0;
	private static Object mutexID; 
	private static final int PORT = 9999;
	private static ServerSocket ssock;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Socket sock1, sock2;
		CountDownLatch latch = new CountDownLatch(1);
		try {
			ssock = new ServerSocket(PORT);
			sock1 = ssock.accept();
			sock2 = ssock.accept();
			MatchThread thr = new MatchThread(sock1, sock2,latch);
			thr.start();
			latch.await();

		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int getID(){
		synchronized (mutexID) {
			return ID++;
		}
	}

}

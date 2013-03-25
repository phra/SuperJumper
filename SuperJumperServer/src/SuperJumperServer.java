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
	private static Object mutexID = new Object(); 
	private static final int PORT = 10000;
	private static ServerSocket ssock;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("main()");
		Socket sock1, sock2;
		CountDownLatch latch = new CountDownLatch(1);
		try {
			System.out.println("serversocket()");
			ssock = new ServerSocket(PORT);
			System.out.println("server started on " + PORT);
			sock1 = ssock.accept();
			if (sock1 != null) System.out.println("prima accept ok");
			System.out.println("first client");
			System.out.flush();
			sock2 = ssock.accept();
			System.out.println("second client");
			MatchThread thr = new MatchThread(sock1, sock2, latch);
			System.out.println("starting matchthread, calling await");
			thr.start();
			latch.await();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("main(): IOexception");
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("main(): interruptedexception");
			e.printStackTrace();
		}
	}
	
	public static int getID(){
		synchronized (mutexID) {
			return ID++;
		}
	}

}

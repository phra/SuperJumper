import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

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
		//CountDownLatch latch = new CountDownLatch(1);
		System.out.println("serversocket()");
		try {
			ssock = new ServerSocket(PORT);
			System.out.println("server started on " + PORT);
			try {
				while (true) {
					sock1 = ssock.accept();
					if (sock1 != null) System.out.println("prima accept ok");
					System.out.println("first client");
					System.out.flush();
					sock2 = ssock.accept();
					System.out.println("second client");
					MatchThread thr = new MatchThread(sock1, sock2);
					System.out.println("starting matchthread, calling await");
					thr.start();
					//latch.await();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("main(): IOexception");
				e.printStackTrace();
			}
		}
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static int getID(){
		synchronized (mutexID) {
			return ID++;
		}
	}

}
